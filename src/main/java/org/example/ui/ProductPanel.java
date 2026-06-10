package org.example.ui;

import org.example.controller.ProductController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductPanel {
    private JPanel mainPanel;
    private JTextField txtProductId;
    private JButton buscarButton;
    private JTextArea txtResults;
    private final ProductController controller;


    public ProductPanel(ProductController controller) {
        this.controller = controller;
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String resultado = controller.searchProductInfo(txtProductId.getText());
                txtResults.setText(resultado);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}