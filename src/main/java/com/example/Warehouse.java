package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final Map<String, Warehouse> INSTANCES = new HashMap<>();
    private final Map<UUID, Product> products = new HashMap<>();
    private final Set<UUID> changedProducts = new HashSet<>();
    private final String name;

    private Warehouse(String name) {
        this.name = name;
    }

    //singleton pattern: returns unique Warehouse instance per name
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

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        Product product = products.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        product.price(newPrice);
        changedProducts.add(id); //track price changes
    }

    public Set<UUID> getChangedProducts() {
        return Collections.unmodifiableSet(new HashSet<>(changedProducts));
    }

    public List<Perishable> expiredProducts() {
        return products.values().stream()
                .filter(p -> p instanceof Perishable)
                .map(p -> (Perishable) p)
                .filter(Perishable::isExpired)
                .collect(Collectors.toList());
    }

    public List<Shippable> shippableProducts() {
        return products.values().stream()
                .filter(p -> p instanceof Shippable)
                .map(p -> (Shippable) p)
                .collect(Collectors.toList());
    }

    public void remove(UUID id) {
        products.remove(id);
        changedProducts.remove(id);
    }

    public void clearProducts() {
        products.clear();
        changedProducts.clear();
    }

    public Map<Category, List<Product>> getProductsGroupedByCategories() {
        return products.values().stream()
                .collect(Collectors.groupingBy(Product::category));
    }
}