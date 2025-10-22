package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private static final Map<String, Warehouse> warehouseMap = new HashMap<>();

    private final String name;
    private final List<Product> products = new ArrayList<>();

    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance(String name) {
        return warehouseMap.computeIfAbsent(name, Warehouse::new);
    }

    public static Warehouse getInstance() {
        return getInstance("default");
    }

    public String getName() {
        return name;
    }

    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        boolean exists = products.stream().anyMatch(p -> p.uuid().equals(product.uuid()));
        if (exists) throw new IllegalArgumentException("Product with that id already exists.");
        products.add(product);
    }

    public void clearProducts() {
        products.clear();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public List<Product> getProducts() {
        return List.copyOf(products);
    }

    public List<Shippable> shippableProducts() {
        return products.stream()
                .filter(p -> p instanceof Shippable)
                .map(p -> (Shippable) p)
                .toList();
    }

    public Optional<Product> getProductById(UUID id) {
        return products.stream()
                .filter(p -> p.uuid().equals(id))
                .findFirst();
    }

    public void remove(UUID uuid) {
        products.removeIf(p -> p.uuid().equals(uuid));
    }
}