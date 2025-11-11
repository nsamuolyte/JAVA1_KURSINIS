package com.example.budgetboltfood.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
    protected LocalDate BirthDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Cart> myOrders = new ArrayList<>();

    @OneToMany(mappedBy = "commentOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Review> myReviews = new ArrayList<>();

    @OneToMany(mappedBy = "feedbackReceiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Review> feedback = new ArrayList<>();

    public Client( String email, String password, String name, String surname, String phoneNumber, LocalDate birthDate, String address)
    {
        super( email, password, name, phoneNumber);
        this.surname = surname;
        this.address = address;
        this.BirthDate = birthDate;
        this.myReviews = new ArrayList<>();
        this.myOrders = new ArrayList<>();
        this.feedback = new ArrayList<>();
    }
}
