package com.example.budgetboltfood.model;

import java.time.LocalDate;

public class Cart
{
    protected int cartId;
    protected int userId;
    protected int restaurantId;
    protected int quantity;
    protected double totalPrice;
    protected LocalDate dateCreated;
    protected CartState cartState;

    public Cart(int quantity, double totalPrice, LocalDate dateCreated, CartState cartState) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.dateCreated = dateCreated;
        this.cartState = cartState;
    }
}
