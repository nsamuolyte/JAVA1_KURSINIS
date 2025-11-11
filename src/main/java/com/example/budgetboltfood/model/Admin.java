package com.example.budgetboltfood.model;

import jakarta.persistence.DiscriminatorValue;
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
@DiscriminatorValue("Admin") // bus įrašyta į DTYPE stulpelį

public class Admin extends User
{
    protected boolean isAdmin;

    public Admin( String email, String password, String name, String surname, String phoneNumber,boolean isAdmin)
    {
        super( email, password, name, phoneNumber);
        this.isAdmin = isAdmin;
    }
}
