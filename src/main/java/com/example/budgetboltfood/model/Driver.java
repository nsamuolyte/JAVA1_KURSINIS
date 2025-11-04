package com.example.budgetboltfood.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter

public class Driver extends User
{
    protected List<Review> myReviews;
    protected double rating;
    protected LocalDate BirthDate;
    protected VehicleType vehicleType;
    protected String vehiclePlate;
    protected VehicleModel vehicleModel;
    protected VehicleColor vehicleColor;
    protected int DriverLicenseNumber;
    protected LocalDate LicenseExpiration;

    public Driver( String email, String password, String name, String surname, String phoneNumber, LocalDate birthDate, List<Review> myReviews, double rating, VehicleType vehicleType, String vehiclePlate, VehicleModel vehicleModel, VehicleColor vehicleColor, int driverLicenseNumber, LocalDate licenseExpiration) {
        super( email, password, name, surname, phoneNumber);
        this.myReviews = myReviews;
        this.rating = rating;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
        this.vehicleModel = vehicleModel;
        this.vehicleColor = vehicleColor;
        DriverLicenseNumber = driverLicenseNumber;
        LicenseExpiration = licenseExpiration;
    }
}
