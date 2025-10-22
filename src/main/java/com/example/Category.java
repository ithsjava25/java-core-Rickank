package com.example;

public final class Category {
    private final String name;

    private Category(String name) {
        this.name = name;
    }

    public static Category of(String name) {
        return null;
    }

    public String getName() {
        return name;
    }
}