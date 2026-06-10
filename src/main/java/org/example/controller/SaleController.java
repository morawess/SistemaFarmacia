package org.example.controller;

import org.example.model.*;
import org.example.repository.*;

public class SaleController {
    private final SaleProcessable saleProcessor;
    private final ProductConsultable productConsultant;
    private final ClientManageable clientManager;

    private Sale currentSale;

    public SaleController(SaleProcessable saleProcessor, ProductConsultable productConsultant, ClientManageable clientManager) {
        this.saleProcessor = saleProcessor;
        this.productConsultant = productConsultant;
        this.clientManager = clientManager;
    }

    public void startNewSale() {
        this.currentSale = new Sale();
    }

    public String verifyAndSetClient(String dni) {
        if (dni.isEmpty()) { //en caso de que
            currentSale.setClient(null);
            return "Venta a Consumidor Final.";
        }
        Client client = clientManager.getClientByDni(dni);
        if (client == null) {
            return "NOT_FOUND";
        }
        currentSale.setClient(client);
        return "Cliente asignado: " + client.getName();
    }

    public String quickRegisterClient(String name, String dni) {
        Client newClient = new Client(name, dni);
        clientManager.saveClient(newClient);

        Client persisted = clientManager.getClientByDni(dni);
        currentSale.setClient(persisted);
        return "Cliente registrado y asignado: " + persisted.getName();
    }

    public String addProduct(String idText, String quantityText, String affiliateNumber) {
        try {
            int id = Integer.parseInt(idText.trim());
            int quantity = Integer.parseInt(quantityText.trim());

            if (quantity <= 0) return "La cantidad debe ser mayor a 0.";

            Product product = productConsultant.getProductById(id);
            if (product == null) return "El producto no existe.";
            if (quantity > product.getStock()) return "Stock insuficiente. Disponible: " + product.getStock();

            //lógica para productos bajo receta
            if (product.isPrescriptionRequired() && affiliateNumber == null) {
                return "NEED_AFFILIATE_NUMBER"; //esto le avisa a la vista que requiere este dato
            }

            String numeroAfiliadoFinal = (affiliateNumber != null) ? affiliateNumber.trim() : null;

            currentSale.addProductToSale(product, quantity, numeroAfiliadoFinal);
            return "SUCCESS";

        } catch (NumberFormatException e) {
            return "Por favor, ingrese números válidos en ID y Cantidad.";
        }
    }

    public Sale getCurrentSale() {
        return currentSale;
    }

    public boolean confirmAndSaveSale() {
        if (currentSale == null || currentSale.getDetails().isEmpty()) {
            return false;
        }

        saleProcessor.processSale(currentSale);
        return true;
    }
}