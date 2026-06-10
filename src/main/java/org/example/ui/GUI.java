package org.example.ui;

import org.example.controller.ProductController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JPanel MainPanel;
    private JTextField txtProductId;
    private JButton btnBuscar;
    private JTextArea txtResultado;
    private JPanel ProductPanel;
    private JPanel ClientPanel;
    private JPanel SalePanel;
    private JTextField txtClientDni;
    private JButton buscarButton;
    private JTextField txtClientName;
    private JButton agregarButton;
    private JTextArea textArea1;

    // La vista ahora conoce al controlador, NO al repositorio
    private ProductController controller;

    public GUI(ProductController controller) {
        this.controller = controller;

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delegamos la tarea al controlador
                String resultado = controller.searchProductInfo(txtProductId.getText());
                txtResultado.setText(resultado);
            }
        });
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }
}