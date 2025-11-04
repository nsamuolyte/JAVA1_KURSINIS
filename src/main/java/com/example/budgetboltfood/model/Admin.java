package com.example.budgetboltfood.model;

import java.time.LocalDate;

public class Admin extends User
{
    protected boolean isAdmin;

    public Admin( String email, String password, String name, String surname, String phoneNumber, LocalDate birthDate, boolean isAdmin) {
        super( email, password, name, surname, phoneNumber, birthDate);
        this.isAdmin = isAdmin;
    }
}
