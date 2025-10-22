package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class FoodProduct extends Product implements Perishable, Shippable {
    public FoodProduct(UUID id, String name, Category category, BigDecimal price, LocalDate expirationDate, double weight) {
        super(id, name, category, price);
    }

    @Override
    public String productDetails() {
        return null;
    }

    @Override
    public LocalDate expirationDate() {
        return null;
    }

    @Override
    public BigDecimal calculateShippingCost() {
        return null;
    }

    @Override
    public Double weight() {
        return null;
    }
}