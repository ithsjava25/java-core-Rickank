package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {
    protected ElectronicsProduct(UUID id, String name, Category category, BigDecimal price, double weight) {
        super(id, name, category, price);
    }

    @Override
    public String productDetails() {
    }

    @Override
    public BigDecimal calculateShippingCost() {
    }

    @Override
    public Double weight() {
    }
}