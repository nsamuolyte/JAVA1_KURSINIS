package com.example.budgetboltfood.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Cuisine
{
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
