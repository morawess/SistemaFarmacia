package org.example.ui;

import org.example.repository.PharmacyDatabaseRepository;
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

        // 2. Instanciamos el menú e inyectamos el repositorio.
        // Como PharmacyDatabaseRepository implementa las 3 interfaces, le pasamos la misma instancia 3 veces.
        Menu menu = new Menu(repositorio, repositorio, repositorio);

        menu.start();
    }
}