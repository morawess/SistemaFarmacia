package org.example.ui;

import org.example.controller.ClientController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class ClientPanel {
    private JPanel mainPanel;
    private JLabel lblStatus;
    private JTextField txtClientDni;
    private JTextField txtClientName;
    private JButton guardarButton;
    private final ClientController controller;


    public ClientPanel(ClientController controller) {
        this.controller = controller;
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mensaje = controller.registerClient(txtClientDni.getText(), txtClientDni.getText());
                lblStatus.setText(mensaje);
                if (!mensaje.startsWith("Error")) {
                    txtClientName.setText("");
                    txtClientDni.setText("");
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}