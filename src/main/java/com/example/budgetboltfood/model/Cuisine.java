package com.example.budgetboltfood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @Enumerated(EnumType.STRING)
    protected CuisineType cuisineType;
    protected String description;
    @Enumerated(EnumType.STRING)
    protected Ingriedients ingriedients;
    @Enumerated(EnumType.STRING)
    protected Alergens alergens;
    @Enumerated(EnumType.STRING)
    protected PortionSize portionSize;

    @ManyToMany (mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> orderList;

    @ManyToOne
    private Restaurant restaurantManu;

    public Cuisine(CuisineType cuisineType, String description, Ingriedients ingriedients, Alergens alergens, PortionSize portionSize) {
        this.cuisineType = cuisineType;
        this.description = description;
        this.ingriedients = ingriedients;
        this.alergens = alergens;
        this.portionSize = portionSize;
    }
}
