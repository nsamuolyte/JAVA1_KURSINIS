package com.example.budgetboltfood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("Driver")

public class Driver extends User
{
    protected double rating;
    protected LocalDate BirthDate;
    protected String surname;
    @Enumerated
    protected VehicleType vehicleType;
    protected String vehiclePlate;
    @Enumerated
    protected VehicleModel vehicleModel;
    @Enumerated
    protected VehicleColor vehicleColor;
    protected String VehiclesPlates;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> orders;


    public Driver( String email, String password, String name, String surname, String phoneNumber,
                   LocalDate birthDate, VehicleType vehicleType, String vehiclesPlates,
                   VehicleModel vehicleModel, VehicleColor vehicleColor) {
        super( email, password, name, phoneNumber);
        this.surname = surname;
        this.BirthDate = birthDate;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
        this.vehicleModel = vehicleModel;
        this.vehicleColor = vehicleColor;
        this.VehiclesPlates = vehiclesPlates;
    }
    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
