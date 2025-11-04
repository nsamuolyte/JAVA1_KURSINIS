package com.example.budgetboltfood.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class Client extends User
{
    protected String address;
    protected List<Cart> myOrders;
    protected List<Review> myReviews;
    protected List<Review> feedback;

    public Client( String email, String password, String name, String surname, String phoneNumber, LocalDate birthDate, String address) {
        super( email, password, name, surname, phoneNumber, birthDate); // TEVINES KLASES INSTRUKTORIUS
        this.address = address;
        this.myReviews = new ArrayList<>();
        this.myOrders = new ArrayList<>();
        this.feedback = new ArrayList<>();
    }
}
