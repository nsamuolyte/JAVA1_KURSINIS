package com.example.budgetboltfood.model;

import jakarta.persistence.Entity;
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

public class Restaurant extends User
{
    protected int restaurantId;
    protected String restaurantAddress;
    protected CuisineType cuisineType;
    protected RestaurantStatus restaurantStatus;

    public Restaurant(String email, String password, String name, String phoneNumber, String restaurantAddress, CuisineType cuisineType, RestaurantStatus restaurantStatus) {
        super(email, password, name, phoneNumber);
        this.restaurantAddress = restaurantAddress;
        this.restaurantStatus = restaurantStatus;
        this.cuisineType = cuisineType;
    }
}
