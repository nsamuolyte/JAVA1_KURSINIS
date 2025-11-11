package com.example.budgetboltfood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("Client")

public class Client extends User
{
    protected String address;
    protected LocalDate BirthDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Cart> myOrders = new ArrayList<>();

    /*@OneToMany(mappedBy = "commentOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Review> myReviews = new ArrayList<>();

    @OneToMany(mappedBy = "feedbackReceiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Review> feedback = new ArrayList<>();*/

    public Client( String email, String password, String name, String surname, String phoneNumber, LocalDate birthDate, String address)
    {
        super( email, password, name, phoneNumber);
        this.surname = surname;
        this.address = address;
        this.BirthDate = birthDate;
        //this.myReviews = new ArrayList<>();
        this.myOrders = new ArrayList<>();
        //this.feedback = new ArrayList<>();
    }
}
