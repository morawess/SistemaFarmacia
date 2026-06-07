package org.example.ui;

import org.example.repository.PharmacyDatabaseRepository;
import javax.swing.*;
/* DATABASE
Host / Server: bchfxdzarqvf7eul6zrv-mysql.services.clever-cloud.com
Port: 3306
User: uhbkv3lake9esy6q
Password: 25rKZQkn5gB48l8425IO
Database: bchfxdzarqvf7eul6zrv
URI: mysql://uhbkv3lake9esy6q:25rKZQkn5gB48l8425IO@bchfxdzarqvf7eul6zrv-mysql.services.clever-cloud.com:3306/bchfxdzarqvf7eul6zrv
 */
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