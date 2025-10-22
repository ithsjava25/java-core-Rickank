package com.example;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class Product {
    protected Product(UUID id, String name, Category category, BigDecimal price) {
    }

    public UUID uuid() {
        return null;
    }

    public String name() {
        return null;
    }

    public Category category() {
        return null;
    }

    public BigDecimal price() {
        return null;
    }

    public void price(BigDecimal price) {
    }

    public abstract String productDetails();
}