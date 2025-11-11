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

public class Restaurant extends User
{
    protected int restaurantId;
    protected String restaurantAddress;

    @Enumerated
    protected CuisineType cuisineType;
    @Enumerated
    protected RestaurantStatus restaurantStatus;

   /* @OneToMany (mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> orders;
    //@OneToMany (mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<Review> reviews;
    @OneToMany (mappedBy = "restaurantMenu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuisine> menu;*/

    public Restaurant(String email, String password, String name, String phoneNumber, String restaurantAddress, CuisineType cuisineType, RestaurantStatus restaurantStatus) {
        super(email, password, name, phoneNumber);
        this.restaurantAddress = restaurantAddress;
        this.restaurantStatus = restaurantStatus;
        this.cuisineType = cuisineType;
    }
}
