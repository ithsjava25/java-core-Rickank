package com.example;

import java.time.LocalDate;

public interface Perishable {
    LocalDate expirationDate();

    //see if product is expired
    default boolean isExpired() {
        return expirationDate().isBefore(LocalDate.now());
    }
}