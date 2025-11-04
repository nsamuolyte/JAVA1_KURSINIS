package com.example.budgetboltfood.fxControllers;


import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginForm
{
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("login");

    public void registerBT(ActionEvent actionEvent) {
    }

    public void loginBT(ActionEvent actionEvent) {
    }
}
