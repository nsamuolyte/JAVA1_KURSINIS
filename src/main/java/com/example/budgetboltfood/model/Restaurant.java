package com.example.budgetboltfood.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class Restaurant extends User
{
    protected int restaurantId;
    protected String restaurantName;
    protected String restaurantAddress;
    protected String restaurantPhone;
    protected LocalDate dateCreated;
    protected RestaurantStatus restaurantStatus;

    public Restaurant(String email, String password, String name, String surname, String phoneNumber, String restaurantName, String restaurantAddress, String restaurantPhone, LocalDate dateCreated, RestaurantStatus restaurantStatus) {
        super(email, password, name, surname, phoneNumber);
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantPhone = restaurantPhone;
        this.dateCreated = dateCreated;
        this.restaurantStatus = restaurantStatus;
    }
}
