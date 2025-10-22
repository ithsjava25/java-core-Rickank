package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {
    private final double weight;

    public ElectronicsProduct(UUID id, String name, Category category, BigDecimal price, double weight) {
        super(id, name, category, price);
        if (weight < 0) throw new IllegalArgumentException("Weight cannot be negative.");
        this.weight = weight;
    }

    @Override
    public String productDetails() {
        return "Electronics: " + name() + ", Warranty: 12 months";
    }

    @Override
    public BigDecimal calculateShippingCost() {
        return BigDecimal.valueOf(weight * 2.0);
    }

    @Override
    public Double weight() {
        return weight;
    }
}