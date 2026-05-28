package org.example.repository;
import org.example.model.Client;

public interface ClientManageable {
    void saveClient(Client client);
    Client getClientByDni(String dni);
}
