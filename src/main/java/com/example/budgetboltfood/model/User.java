package com.example.budgetboltfood.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class User
{
    protected int id;
    protected String email;
    protected String password;
    protected String name;
    protected String surname;
    protected String phoneNumber;
    protected LocalDate BirthDate;
    protected LocalDate dateCreated;
    protected LocalDate dateUpdated;
    protected boolean isAdmin;

    public User ( String email, String password, String name, String surname, String phoneNumber, LocalDate birthDate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        BirthDate = birthDate;
    }

}
