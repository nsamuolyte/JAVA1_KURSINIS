package com.example.budgetboltfood.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class Restaurant
{
    protected int restaurantId;
    protected String restaurantName;
    protected String restaurantAddress;
    protected String restaurantPhone;
    protected LocalDate dateCreated;
    protected RestaurantStatus restaurantStatus;

    public Restaurant( String restaurantName, String restaurantAddress, String restaurantPhone, RestaurantStatus restaurantStatus) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantStatus = restaurantStatus;
    }
}
