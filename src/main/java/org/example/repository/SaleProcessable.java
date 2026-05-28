package org.example.repository;
import org.example.model.Sale;

public interface SaleProcessable {
    void processSale(Sale sale, int productId, int quantity);
}