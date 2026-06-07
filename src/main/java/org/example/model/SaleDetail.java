package org.example.model;

public class SaleDetail {
    private Product product;
    private int quantity;
    private double unitPrice;
    private double subtotal;
    private double discountAmount;
    private double finalTotal;
    private String affiliateNumber;
    private static final double miFarmacityDiscount = 0.10; //descuento del club 'Mi farmacity'

    public SaleDetail(Product product, int quantity, Client client, String affiliateNumber) {
        this.product = product;
        this.quantity = quantity;
        this.affiliateNumber = affiliateNumber;
        this.unitPrice = product.getPrice();
        this.subtotal = this.unitPrice * this.quantity;

        calculateDiscount(client); //se calcula el descuento
    }

    private void calculateDiscount(Client client) {

        if (product.miFarmacityDiscount() && client != null) { //si el cliente tiene miFarmacity y el producto es de venta libre, se aplica descuento de mifarmacity
            this.discountAmount = this.subtotal * miFarmacityDiscount;
        } else if (product.isPrescriptionRequired() && affiliateNumber != null && !affiliateNumber.isEmpty()) { //si el producto es de venta bajo receta, se aplica el descuento de la obra social

            PrescriptionProduct pProduct = (PrescriptionProduct) product;
            this.discountAmount = this.subtotal * pProduct.getHealthInsuranceDiscount();
        } else {
            this.discountAmount = 0.0; //si no tiene ni miFarmacity ni obra social, no se aplica ningun descuento
        }

        this.finalTotal = this.subtotal - this.discountAmount;
    }

    // Getters
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalTotal() { return finalTotal; }
    public String getAffiliateNumber() { return affiliateNumber; }
}