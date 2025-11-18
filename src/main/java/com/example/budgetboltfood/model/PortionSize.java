package com.example.budgetboltfood.model;

public enum PortionSize {

    SMALL(5.99),
    MEDIUM(8.99),
    LARGE(12.99);

    private final double price;

    PortionSize(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
