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

public class Admin extends User
{
    protected boolean isAdmin;

    public Admin( String email, String password, String name, String surname, String phoneNumber,boolean isAdmin)
    {
        super( email, password, name, surname, phoneNumber);
        this.isAdmin = isAdmin;
    }
}
