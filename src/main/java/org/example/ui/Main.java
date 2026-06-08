package org.example.ui;

import org.example.repository.PharmacyDatabaseRepository;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        PharmacyDatabaseRepository repositorio = new PharmacyDatabaseRepository();
        Menu menu = new Menu(repositorio, repositorio, repositorio);

        menu.start();

        //PARA IMPLEMENTAR LA GUI (solo esta para buscar un producto)

//        JFrame frame = new JFrame("Consulta de Productos - Sistema Farmacia");
//
//        GUI pantalla = new GUI(repositorio);
//
//        frame.setContentPane(pantalla.getMainPanel());

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(450, 350);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
    }
}