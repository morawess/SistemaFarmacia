package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private int id;
    private Client client;
    private LocalDateTime saleDate;
    private List<SaleDetail> details; //lista de los saleDetails, es decir, los distintos productos comprados
    private double total; //suma todos los renglones/saleDetails

    public Sale() {
        this.details = new ArrayList<>();
        this.saleDate = LocalDateTime.now();
    }

    public void addProductToSale(Product product, int quantity, String affiliateNumber) {//este metodo agrega un producto a la venta y actualiza el total automáticamente
        SaleDetail newDetail = new SaleDetail(product, quantity, this.client, affiliateNumber);
        this.details.add(newDetail);
        
        calculateTotal();
    }

    private void calculateTotal() {
        this.total = 0.0;
        for (SaleDetail detail : details) {
            this.total += detail.getFinalTotal();
        }
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
    public List<SaleDetail> getDetails() { return details; }
    public double getTotal() { return total; }
}