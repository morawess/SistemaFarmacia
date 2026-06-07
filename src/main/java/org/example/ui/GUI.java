package org.example.ui;

import org.example.model.Product;
import org.example.repository.ProductConsultable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JPanel mainPanel;
    private JTextField txtProductId;
    private JButton btnBuscar;
    private JTextArea txtResultado;

    private ProductConsultable productConsultant;

    public GUI(ProductConsultable productConsultant) {
        this.productConsultant = productConsultant;

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });
    }

    private void buscarProducto() {
        try {
            int id = Integer.parseInt(txtProductId.getText().trim());
            Product product = productConsultant.getProductById(id);
            if (product != null) {
                String info = "Producto encontrado:\n\n" +
                        "- Nombre: " + product.getName() + "\n" +
                        "- Precio: $" + String.format("%.2f", product.getPrice()) + "\n" +
                        "- Stock: " + product.getStock() + " unidades\n\n";

                if (product.miFarmacityDiscount()) {
                    info += "Promoción: Acepta descuento de Mi Farmacity";
                } else {
                    info += "⚕Promoción: NO acepta descuentos (Venta bajo receta)";
                }

                txtResultado.setText(info);
            } else {
                txtResultado.setText("No se encontró ningún producto con el ID " + id);
            }
        } catch (NumberFormatException ex) {
            txtResultado.setText("Por favor, ingrese un número válido.");
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
