package com.example.budgetboltfood.fxControllers;


import com.example.budgetboltfood.HelloApplication;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginForm
{
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("login");

    public void loginBT(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) passwordField.getScene().getWindow();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void registerBT(ActionEvent actionEvent)
    {
    }
}
