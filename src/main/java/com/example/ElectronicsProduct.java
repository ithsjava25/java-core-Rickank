package com.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class ElectronicsProduct extends Product implements Shippable {
    private final int warrantyMonths;
    private final BigDecimal weight;

    //shipping cost constants
    private static final BigDecimal BASE_SHIPPING = BigDecimal.valueOf(79);
    private static final BigDecimal ADD_SHIPPING = BigDecimal.valueOf(49);
    private static final BigDecimal WEIGHT_THRESHOLD = BigDecimal.valueOf(5.0);

    public ElectronicsProduct(UUID id, String name, Category category, BigDecimal price, int warrantyMonths, BigDecimal weight) {
        super(id, name, category, price);

        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Warranty months cannot be negative.");
        }
        if (weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }

        this.warrantyMonths = warrantyMonths;
        this.weight = weight;
    }

    public int warrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public double weight() {
        return weight.doubleValue();
    }

    @Override
    public String productDetails() {
        return "Electronics: " + name() + ", Warranty: " + warrantyMonths + " months";
    }

    @Override
    public BigDecimal calculateShippingCost() {
        //higher cost for heavy items (> 5kg)
        return (weight.compareTo(WEIGHT_THRESHOLD) > 0
                ? BASE_SHIPPING.add(ADD_SHIPPING)
                : BASE_SHIPPING)
                .setScale(2, RoundingMode.HALF_UP);
    }
}