package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class FoodProduct extends Product implements Perishable, Shippable {
    private final LocalDate expirationDate;
    private final BigDecimal weight;

    public FoodProduct(UUID id, String name, Category category, BigDecimal price, LocalDate expirationDate, BigDecimal weight) {
        super(id, name, category, price);
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (weight == null || weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }
        this.expirationDate = expirationDate;
        this.weight = weight;
    }

    @Override
    public LocalDate expirationDate() {
        return expirationDate;
    }

    @Override
    public double weight() {
        return weight.doubleValue();
    }

    @Override
    public BigDecimal calculateShippingCost() {
        //food shipping cost based on weight - (50 per unit)
        return weight.multiply(new BigDecimal("50")).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String productDetails() {
        return String.format("Food: %s, Expires: %s", name(), expirationDate.toString());
    }
}