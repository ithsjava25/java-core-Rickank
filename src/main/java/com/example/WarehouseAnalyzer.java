package com.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Analyzer class that provides advanced warehouse operations.
 * Students must implement these methods for the advanced tests to pass.
 */
class WarehouseAnalyzer {
    private final Warehouse warehouse;

    public WarehouseAnalyzer(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public List<Product> findProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> result = new ArrayList<>();
        for (Product p : warehouse.getProducts()) {
            BigDecimal price = p.price();
            if (price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <= 0) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Perishable> findProductsExpiringWithinDays(int days) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(days);
        List<Perishable> result = new ArrayList<>();
        for (Product p : warehouse.getProducts()) {
            if (p instanceof Perishable per) {
                LocalDate exp = per.expirationDate();
                if (!exp.isBefore(today) && !exp.isAfter(end)) {
                    result.add(per);
                }
            }
        }
        return result;
    }

    public List<Product> searchProductsByName(String searchTerm) {
        String term = searchTerm.toLowerCase(Locale.ROOT);
        List<Product> result = new ArrayList<>();
        for (Product p : warehouse.getProducts()) {
            if (p.name().toLowerCase(Locale.ROOT).contains(term)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> findProductsAbovePrice(BigDecimal price) {
        List<Product> result = new ArrayList<>();
        for (Product p : warehouse.getProducts()) {
            if (p.price().compareTo(price) > 0) {
                result.add(p);
            }
        }
        return result;
    }

    public Map<Category, BigDecimal> calculateWeightedAveragePriceByCategory() {
        Map<Category, List<Product>> byCat = warehouse.getProducts().stream()
                .collect(Collectors.groupingBy(Product::category));
        Map<Category, BigDecimal> result = new HashMap<>();
        for (Map.Entry<Category, List<Product>> e : byCat.entrySet()) {
            Category cat = e.getKey();
            List<Product> items = e.getValue();
            BigDecimal weightedSum = BigDecimal.ZERO;
            double weightSum = 0.0;
            for (Product p : items) {
                if (p instanceof Shippable s) {
                    double w = Optional.ofNullable(s.weight()).orElse(0.0);
                    if (w > 0) {
                        BigDecimal wBD = BigDecimal.valueOf(w);
                        weightedSum = weightedSum.add(p.price().multiply(wBD));
                        weightSum += w;
                    }
                }
            }
            BigDecimal avg;
            if (weightSum > 0) {
                avg = weightedSum.divide(BigDecimal.valueOf(weightSum), 2, RoundingMode.HALF_UP);
            } else {
                BigDecimal sum = items.stream().map(Product::price).reduce(BigDecimal.ZERO, BigDecimal::add);
                avg = sum.divide(BigDecimal.valueOf(items.size()), 2, RoundingMode.HALF_UP);
            }
            result.put(cat, avg);
        }
        return result;
    }

    public List<Product> findPriceOutliers(double deviationFactor) {
        List<Product> products = warehouse.getProducts();
        int n = products.size();
        if (n == 0) return List.of();

        //sortera priser
        var sortedProducts = products.stream()
                .sorted(Comparator.comparing(Product::price))
                .toList();

        //räkna ut vart första och tredje kvartilen ligger
        int q1Index = n / 4;
        int q3Index = (3 * n) / 4;

        //priserna på 25% och 75% positionerna
        BigDecimal q1 = sortedProducts.get(q1Index).price();
        BigDecimal q3 = sortedProducts.get(q3Index).price();
        BigDecimal iqr = q3.subtract(q1);

        //räkna ut när ett pris är för lågt eller högt
        BigDecimal lowerBound = q1.subtract(iqr.multiply(BigDecimal.valueOf(deviationFactor)));
        BigDecimal upperBound = q3.add(iqr.multiply(BigDecimal.valueOf(deviationFactor)));

        //hitta outliers
        return sortedProducts.stream()
                .filter(product -> product.price().compareTo(lowerBound) < 0 ||
                        product.price().compareTo(upperBound) > 0)
                .toList();
    }

    public List<ShippingGroup> optimizeShippingGroups(BigDecimal maxWeightPerGroup) {
        double maxW = maxWeightPerGroup.doubleValue();
        List<Shippable> items = warehouse.shippableProducts();
        items.sort((a, b) -> Double.compare(Objects.requireNonNullElse(b.weight(), 0.0), Objects.requireNonNullElse(a.weight(), 0.0)));
        List<List<Shippable>> bins = new ArrayList<>();
        for (Shippable item : items) {
            double w = Objects.requireNonNullElse(item.weight(), 0.0);
            boolean placed = false;
            for (List<Shippable> bin : bins) {
                double binWeight = bin.stream().map(Shippable::weight).reduce(0.0, Double::sum);
                if (binWeight + w <= maxW) {
                    bin.add(item);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                List<Shippable> newBin = new ArrayList<>();
                newBin.add(item);
                bins.add(newBin);
            }
        }
        List<ShippingGroup> groups = new ArrayList<>();
        for (List<Shippable> bin : bins) groups.add(new ShippingGroup(bin));
        return groups;
    }

    public Map<Product, BigDecimal> calculateExpirationBasedDiscounts() {
        Map<Product, BigDecimal> result = new HashMap<>();
        LocalDate today = LocalDate.now();
        for (Product p : warehouse.getProducts()) {
            BigDecimal discounted = p.price();
            if (p instanceof Perishable per) {
                LocalDate exp = per.expirationDate();
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(today, exp);
                if (daysBetween == 0) {
                    discounted = p.price().multiply(new BigDecimal("0.50"));
                } else if (daysBetween == 1) {
                    discounted = p.price().multiply(new BigDecimal("0.70"));
                } else if (daysBetween > 1 && daysBetween <= 3) {
                    discounted = p.price().multiply(new BigDecimal("0.85"));
                } else {
                    discounted = p.price();
                }
                discounted = discounted.setScale(2, RoundingMode.HALF_UP);
            }
            result.put(p, discounted);
        }
        return result;
    }

    public InventoryValidation validateInventoryConstraints() {
        List<Product> items = warehouse.getProducts();
        if (items.isEmpty()) return new InventoryValidation(0.0, 0);
        BigDecimal highValueThreshold = new BigDecimal("1000");
        long highValueCount = items.stream().filter(p -> p.price().compareTo(highValueThreshold) >= 0).count();
        double percentage = (highValueCount * 100.0) / items.size();
        int diversity = (int) items.stream().map(Product::category).distinct().count();
        return new InventoryValidation(percentage, diversity);
    }

    public InventoryStatistics getInventoryStatistics() {
        List<Product> items = warehouse.getProducts();
        int totalProducts = items.size();
        BigDecimal totalValue = items.stream().map(Product::price).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averagePrice = totalProducts == 0 ? BigDecimal.ZERO : totalValue.divide(BigDecimal.valueOf(totalProducts), 2, RoundingMode.HALF_UP);
        int expiredCount = 0;
        for (Product p : items) {
            if (p instanceof Perishable per && per.expirationDate().isBefore(LocalDate.now())) {
                expiredCount++;
            }
        }
        int categoryCount = (int) items.stream().map(Product::category).distinct().count();
        Product mostExpensive = items.stream().max(Comparator.comparing(Product::price)).orElse(null);
        Product cheapest = items.stream().min(Comparator.comparing(Product::price)).orElse(null);
        return new InventoryStatistics(totalProducts, totalValue, averagePrice, expiredCount, categoryCount, mostExpensive, cheapest);
    }
}

class ShippingGroup {
    private final List<Shippable> products;
    private final Double totalWeight;
    private final BigDecimal totalShippingCost;

    public ShippingGroup(List<Shippable> products) {
        this.products = new ArrayList<>(products);
        this.totalWeight = products.stream()
                .map(Shippable::weight)
                .reduce(0.0, Double::sum);
        this.totalShippingCost = products.stream()
                .map(Shippable::calculateShippingCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Shippable> getProducts() { return new ArrayList<>(products); }
    public Double getTotalWeight() { return totalWeight; }
    public BigDecimal getTotalShippingCost() { return totalShippingCost; }
}

class InventoryValidation {
    private final double highValuePercentage;
    private final int categoryDiversity;
    private final boolean highValueWarning;
    private final boolean minimumDiversity;

    public InventoryValidation(double highValuePercentage, int categoryDiversity) {
        this.highValuePercentage = highValuePercentage;
        this.categoryDiversity = categoryDiversity;
        this.highValueWarning = highValuePercentage > 70.0;
        this.minimumDiversity = categoryDiversity >= 2;
    }

    public double getHighValuePercentage() { return highValuePercentage; }
    public int getCategoryDiversity() { return categoryDiversity; }
    public boolean isHighValueWarning() { return highValueWarning; }
    public boolean hasMinimumDiversity() { return minimumDiversity; }
}

class InventoryStatistics {
    private final int totalProducts;
    private final BigDecimal totalValue;
    private final BigDecimal averagePrice;
    private final int expiredCount;
    private final int categoryCount;
    private final Product mostExpensiveProduct;
    private final Product cheapestProduct;

    public InventoryStatistics(int totalProducts, BigDecimal totalValue, BigDecimal averagePrice,
                               int expiredCount, int categoryCount,
                               Product mostExpensiveProduct, Product cheapestProduct) {
        this.totalProducts = totalProducts;
        this.totalValue = totalValue;
        this.averagePrice = averagePrice;
        this.expiredCount = expiredCount;
        this.categoryCount = categoryCount;
        this.mostExpensiveProduct= mostExpensiveProduct;
        this.cheapestProduct = cheapestProduct;
    }

    public int getTotalProducts() { return totalProducts; }
    public BigDecimal getTotalValue() { return totalValue; }
    public BigDecimal getAveragePrice() { return averagePrice; }
    public int getExpiredCount() { return expiredCount; }
    public int getCategoryCount() { return categoryCount; }
    public Product getMostExpensiveProduct() { return mostExpensiveProduct; }
    public Product getCheapestProduct() { return cheapestProduct; }
}