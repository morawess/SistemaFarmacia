package org.example.ui;

import org.example.repository.PharmacyDatabaseRepository;

public class Main {
    public static void main(String[] args) {

        PharmacyDatabaseRepository repositorio = new PharmacyDatabaseRepository();

        // 2. Instanciamos el menú e inyectamos el repositorio.
        // Como PharmacyDatabaseRepository implementa las 3 interfaces, le pasamos la misma instancia 3 veces.
        Menu menu = new Menu(repositorio, repositorio, repositorio);

        menu.start();
    }
}