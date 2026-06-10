package org.example.controller;

import org.example.model.Client;
import org.example.repository.ClientManageable;

public class ClientController {
    private final ClientManageable clientManager;

    public ClientController(ClientManageable clientManager) {
        this.clientManager = clientManager;
    }

    public String registerClient(String name, String dni) {
        if (name.isEmpty() || dni.isEmpty()) {
            return "Error: Todos los campos son obligatorios.";
        }
        Client existingClient = clientManager.getClientByDni(dni);//chequea que no existe en la bd
        if (existingClient != null) {
            return "Error: Ya existe un cliente registrado con el DNI " + dni;
        }

        Client newClient = new Client(name, dni);
        clientManager.saveClient(newClient);
        return "Cliente " + name + " registrado con éxito.";
    }
}