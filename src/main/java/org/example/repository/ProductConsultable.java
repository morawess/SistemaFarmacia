package org.example.repository;
import org.example.model.Product;

public interface ProductConsultable {
    Product getProductById(int id);
}