package org.example.model;

public class OTCProduct extends Product{


    public OTCProduct(int id, String name, double price, int stock) {
        super(id, name, price, stock);
    }

    @Override
    public boolean isPrescriptionRequired() {
        return false;
    }

    @Override
    public boolean miFarmacityDiscount() {
        return true;
    }
}
