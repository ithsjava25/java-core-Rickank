package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class FoodProduct extends Product implements Perishable, Shippable {
    private final LocalDate expirationDate;
    private final double weight;

    public FoodProduct(UUID id, String name, Category category, BigDecimal price, LocalDate expirationDate, double weight) {
        super(id, name, category, price);
        if (weight < 0) throw new IllegalArgumentException("Weight cannot be negative.");
        if (expirationDate == null) throw new IllegalArgumentException("Expiration date cannot be null.");
        this.expirationDate = expirationDate;
        this.weight = weight;
    }

    @Override
    public String productDetails() {
        return "Food: " + name() + ", Expires: " + expirationDate.toString();
    }

    @Override
    public LocalDate expirationDate() {
        return expirationDate;
    }

    @Override
    public BigDecimal calculateShippingCost() {
        return BigDecimal.valueOf(weight * 1.0);
    }

    @Override
    public Double weight() {
        return weight;
    }
}