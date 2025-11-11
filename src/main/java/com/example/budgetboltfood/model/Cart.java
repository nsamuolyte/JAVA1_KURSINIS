package com.example.budgetboltfood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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

    protected int quantity;
    protected double totalPrice;
    protected LocalDate dateCreated;

    @Enumerated(EnumType.STRING)
    protected CartState cartState;

    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private User user;
    @ManyToOne
    private Driver driver;
    @ManyToMany
    private List<Cuisine> menu;

    public Cart(int quantity, double totalPrice, LocalDate dateCreated, CartState cartState)
    {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.dateCreated = dateCreated;
        this.cartState = cartState;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public CartState getCartState() {
        return cartState;
    }

    public void setCartState(CartState cartState) {
        this.cartState = cartState;
    }
}
