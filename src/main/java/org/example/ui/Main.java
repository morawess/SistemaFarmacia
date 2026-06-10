package org.example.ui;

import org.example.controller.ClientController;
import org.example.controller.ProductController;
import org.example.controller.SaleController;
import org.example.repository.PharmacyDatabaseRepository;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        PharmacyDatabaseRepository repositorio = new PharmacyDatabaseRepository();

        ProductController productController = new ProductController(repositorio);
        ClientController clientController = new ClientController(repositorio);
        SaleController saleController = new SaleController(repositorio, repositorio, repositorio);

        //------CONSOLA

        //Menu menu = new Menu(repositorio, repositorio, repositorio);
        //menu.start();

        //------INTERFAZ GRAFICA

        JFrame mainFrame = new JFrame("Sistema de Gestión - Farmacity");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        ProductPanel productPanel = new ProductPanel(productController);
        ClientPanel clientPanel = new ClientPanel(clientController);
        SalePanel salePanel = new SalePanel(saleController);

        tabbedPane.addTab("Ventas", salePanel.getMainPanel());
        tabbedPane.addTab("Consultar Productos", productPanel.getMainPanel());
        tabbedPane.addTab("Registrar Clientes", clientPanel.getMainPanel());

        mainFrame.setContentPane(tabbedPane);
        mainFrame.setVisible(true);
    }
}