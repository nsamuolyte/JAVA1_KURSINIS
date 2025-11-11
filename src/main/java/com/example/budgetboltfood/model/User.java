package com.example.budgetboltfood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class User implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String email;
    protected String password;
    protected String name;
    protected String surname;
    protected String phoneNumber;
    protected LocalDate dateCreated;
    protected LocalDate dateUpdated;
    protected boolean isAdmin;

    public User ( String email, String password, String name, String phoneNumber)
    {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;

    }

}
