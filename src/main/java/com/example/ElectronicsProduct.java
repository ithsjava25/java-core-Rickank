package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {
    public ElectronicsProduct(UUID id, String name, Category category, BigDecimal price, double weight) {
        super(id, name, category, price);
    }

    @Override
    public String productDetails() {
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