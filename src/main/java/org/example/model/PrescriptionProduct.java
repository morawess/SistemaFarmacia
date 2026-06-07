package org.example.model;

public class PrescriptionProduct extends Product {
    private double healthInsuranceDiscount;

    public PrescriptionProduct(int id, String name, double price, int stock, double healthInsuranceDiscount) {
        super(id, name, price, stock);
        this.healthInsuranceDiscount = healthInsuranceDiscount;
    }

    public double getHealthInsuranceDiscount() {
        return healthInsuranceDiscount;
    }

    @Override
    public boolean isPrescriptionRequired() {
        return true;
    }

    @Override
    public boolean miFarmacityDiscount() {
        return false;
    }
}