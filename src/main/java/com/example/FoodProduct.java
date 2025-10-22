package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class FoodProduct extends Product implements Perishable, Shippable {
    protected FoodProduct(UUID id, String name, Category category, BigDecimal price, LocalDate expirationDate, double weight) {
        super(id, name, category, price);
    }

    @Override
    public String productDetails() {
    }

    @Override
    public LocalDate expirationDate() {
    }

    @Override
    public BigDecimal calculateShippingCost() {
    }

    @Override
    public Double weight() {
    }
}