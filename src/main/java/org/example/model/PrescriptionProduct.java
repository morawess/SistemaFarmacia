package org.example.model;

public class PrescriptionProduct extends Product{
    public PrescriptionProduct(int id, String name, double price, int stock) {
        super(id, name, price, stock);
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
