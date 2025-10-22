package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Warehouse {
    private static Warehouse instance = null;
    private final List<Product> products = new ArrayList<>();

    private Warehouse() {
    }

    public static Warehouse getInstance(String name) {
        if (instance == null) {
            instance = new Warehouse();
        }
        return instance;
    }

    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        products.add(product);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void clearProducts() {
        products.clear();
    }
}