package org.example.model;

public class SaleDetail {
    private Product product;
    private int quantity;
    private double unitPrice;
    private double subtotal;
    private double discountAmount;
    private double finalTotal;
    private static final double miFarmacityDiscount = 0.10; //descuento del club 'Mi farmacity'

    public SaleDetail(Product product, int quantity, Client client) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.subtotal = this.unitPrice * this.quantity;

        calculateDiscount(client); //se calcula el descuento
    }

    private void calculateDiscount(Client client) {

        boolean isRegisteredClient = (client != null);//aca pregunta si el ciente esta registrado (si client es null, da false)
        boolean productAllowsDiscount = product.miFarmacityDiscount();// si es de venta libre el producto recibe el descuento

        if (isRegisteredClient && productAllowsDiscount) {
            this.discountAmount = this.subtotal * miFarmacityDiscount; //si el cliente esta registrado y el producto es otc le hace el descuento
        } else {
            this.discountAmount = 0.0;
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
}