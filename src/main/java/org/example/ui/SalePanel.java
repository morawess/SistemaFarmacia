package org.example.ui;

import org.example.controller.SaleController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalePanel {
    private JPanel mainPanel; // Vinculado en el SalePanel.form
    private JLabel txt;
    private JTextField txtClientDni;
    private JButton btnIniciarVenta;
    private JTextField txtProductId;
    private JTextField txtCantidad;
    private JButton btnAgregarProducto;
    private JTextArea txtDetalleVenta;
    private JButton btnConfirmarVenta;

    private final SaleController controller;

    public SalePanel(SaleController controller) {
        this.controller = controller;
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        txtProductId.setEnabled(false);
        txtCantidad.setEnabled(false);
        btnAgregarProducto.setEnabled(false);
        btnConfirmarVenta.setEnabled(false);

        btnIniciarVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dni = txtClientDni.getText().trim();

                controller.startNewSale();

                String res = controller.verifyAndSetClient(dni);//se verifica el dni del cliente

                if (res.equals("NOT_FOUND")) {
                    int opcion = JOptionPane.showConfirmDialog(
                            mainPanel,
                            "El cliente con DNI " + dni + " no existe.\n¿Desea registrarlo en este momento?",
                            "Cliente No Encontrado",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (opcion == JOptionPane.YES_OPTION) {
                        String nombre = JOptionPane.showInputDialog(mainPanel, "Ingrese el nombre del nuevo cliente:");

                        if (nombre != null && !nombre.trim().isEmpty()) {
                            String regRes = controller.quickRegisterClient(nombre.trim(), dni);
                            txtDetalleVenta.setText("Venta Iniciada.\n" + regRes + "\n----------------\n");
                        } else {
                            JOptionPane.showMessageDialog(mainPanel, "Operación cancelada. El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        txtDetalleVenta.setText("Inicio de venta cancelado. Ingrese un DNI válido.");
                        return;
                    }
                } else {
                    txtDetalleVenta.setText("Venta Iniciada.\n" + res + "\n----------------\n");
                }

                txtProductId.setEnabled(true);
                txtCantidad.setEnabled(true);
                btnAgregarProducto.setEnabled(true);
                btnConfirmarVenta.setEnabled(true);

                btnIniciarVenta.setEnabled(false);
                txtClientDni.setEnabled(false);
            }
        });

        btnAgregarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = txtProductId.getText();
                String cantText = txtCantidad.getText();

                String resultado = controller.addProduct(idText, cantText, null);

                if (resultado.equals("NEED_AFFILIATE_NUMBER")) {
                    String afiliado = JOptionPane.showInputDialog(
                            mainPanel,
                            "[ATENCIÓN] Este producto requiere receta médica.\nIngrese el número de afiliado de la Obra Social (o deje vacío si no tiene):",
                            "Requiere Obra Social",
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (afiliado == null) {
                        return;
                    }

                    resultado = controller.addProduct(idText, cantText, afiliado);
                }

                if (resultado.equals("SUCCESS")) {
                    txtProductId.setText("");
                    txtCantidad.setText("");

                    actualizarTicketVisual();

                } else {
                    JOptionPane.showMessageDialog(mainPanel, resultado, "Error al agregar", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnConfirmarVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean ventaExitosa = controller.confirmAndSaveSale();

                if (ventaExitosa) {
                    JOptionPane.showMessageDialog(mainPanel, "Venta procesada y guardada con éxito en la base de datos.", "Venta Confirmada", JOptionPane.INFORMATION_MESSAGE);
                    txtProductId.setEnabled(false);
                    txtCantidad.setEnabled(false);
                    btnAgregarProducto.setEnabled(false);
                    btnConfirmarVenta.setEnabled(false);

                    btnIniciarVenta.setEnabled(true);
                    txtClientDni.setEnabled(true);

                    txtClientDni.setText("");
                    txtDetalleVenta.setText("");
                    txtProductId.setText("");
                    txtCantidad.setText("");

                } else {
                    JOptionPane.showMessageDialog(mainPanel, "No hay productos cargados.\nAgregue al menos un producto antes de cobrar.", "Ticket Vacío", JOptionPane.WARNING_MESSAGE);
                }

            }
        });

    }

    private void actualizarTicketVisual() {
        org.example.model.Sale currentSale = controller.getCurrentSale();

        if (currentSale == null || currentSale.getDetails().isEmpty()) {
            txtDetalleVenta.setText("No hay productos en el ticket.");
            return;
        }

        StringBuilder ticket = new StringBuilder();
        ticket.append("========================================\n");
        ticket.append("             TICKET DE VENTA            \n");
        ticket.append("========================================\n");

        for (org.example.model.SaleDetail detail : currentSale.getDetails()) {
            ticket.append(String.format("- %s (x%d)\n", detail.getProduct().getName(), detail.getQuantity()));
            ticket.append(String.format("  Subtotal: $%.2f | Descuento: $%.2f | A Pagar: $%.2f\n",
                    detail.getSubtotal(), detail.getDiscountAmount(), detail.getFinalTotal()));
        }

        ticket.append("----------------------------------------\n");
        ticket.append(String.format("TOTAL FINAL: $%.2f\n", currentSale.getTotal()));
        ticket.append("========================================\n");

        txtDetalleVenta.setText(ticket.toString());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}