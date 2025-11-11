package com.example.budgetboltfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Cuisine
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    protected int cuisineId;
    protected CuisineType cuisineType;
    protected String description;
    protected Ingriedients ingriedients;
    protected Alergens alergens;
    protected PortionSize portionSize;

    public Cuisine(CuisineType cuisineType, String description, Ingriedients ingriedients, Alergens alergens, PortionSize portionSize) {
        this.cuisineType = cuisineType;
        this.description = description;
        this.ingriedients = ingriedients;
        this.alergens = alergens;
        this.portionSize = portionSize;
    }
}
