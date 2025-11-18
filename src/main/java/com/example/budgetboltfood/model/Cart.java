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
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    private int quantity;
    private double totalPrice;
    private LocalDate dateCreated;

    @Enumerated(EnumType.STRING)
    private CartState cartState;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;  // <<< N A U J A I

    @Enumerated(EnumType.STRING)
    private PickUpMethod pickUpMethod; // <<< N A U J A I

    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    private User user;

    @ManyToOne
    private Driver driver;

    @ManyToMany
    private List<Cuisine> menu;


    public Cart(int quantity, double totalPrice, LocalDate dateCreated, CartState cartState) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.dateCreated = dateCreated;
        this.cartState = cartState;
    }
}
