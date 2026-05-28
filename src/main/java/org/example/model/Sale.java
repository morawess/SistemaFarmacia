package org.example.model;

import java.time.LocalDateTime;

public class Sale {
    private int id;
    private int clientId;
    private LocalDateTime saleDate;
    private double total;

    public Sale() {
    }

    public Sale(int clientId, LocalDateTime saleDate, double total) {
        this.clientId = clientId;
        this.saleDate = saleDate;
        this.total = total;
    }

    public Sale(int id, int clientId, LocalDateTime saleDate, double total) {
        this.id = id;
        this.clientId = clientId;
        this.saleDate = saleDate;
        this.total = total;
    }

    //getter y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

