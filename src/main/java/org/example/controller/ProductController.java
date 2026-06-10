package org.example.controller;

import org.example.model.Product;
import org.example.repository.ProductConsultable;

public class ProductController {
    private ProductConsultable productConsultant;

    public ProductController(ProductConsultable productConsultant) {
        this.productConsultant = productConsultant;
    }

    public String searchProductInfo(String idText) {
        try {
            int id = Integer.parseInt(idText.trim());
            Product product = productConsultant.getProductById(id);

            if (product != null) {
                String info = "Producto encontrado:\n\n" +
                        "- Nombre: " + product.getName() + "\n" +
                        "- Precio: $" + String.format("%.2f", product.getPrice()) + "\n" +
                        "- Stock: " + product.getStock() + " unidades\n\n";

                if (product.miFarmacityDiscount()) {
                    info += "Promoción: Acepta descuento de Mi Farmacity";
                } else {
                    info += "Promoción: No acepta descuentos de Mi Farmacity (Venta bajo receta)";
                }
                return info;
            } else {
                return "No se encontró ningún producto con el ID " + id;
            }
        } catch (NumberFormatException ex) {
            return "Por favor, ingrese un número válido.";
        }
    }
}