package com.example.budgetboltfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Cart
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int cartId;
    protected int userId;
    protected int restaurantId;
    protected int quantity;
    protected double totalPrice;
    protected LocalDate dateCreated;
    protected CartState cartState;

    public Cart(int quantity, double totalPrice, LocalDate dateCreated, CartState cartState)
    {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.dateCreated = dateCreated;
        this.cartState = cartState;
    }
}
