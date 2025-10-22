package com.example;

import java.util.*;

public class Warehouse {
    private static final Map<String, Warehouse> INSTANCES = new HashMap<>();
    private final String name;
    private final Map<UUID, Product> products = new HashMap<>();

    private Warehouse(String name) {
        this.name = name;
    }

    //singleton pattern with name parameter
    public static Warehouse getInstance(String name) {
        return INSTANCES.computeIfAbsent(name, Warehouse::new);
    }

    public static Warehouse getInstance() {
        return getInstance("DefaultWarehouse");
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (products.containsKey(product.uuid())) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        products.put(product.uuid(), product);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(new ArrayList<>(products.values()));
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public void remove(UUID id) {
        products.remove(id);
    }
}