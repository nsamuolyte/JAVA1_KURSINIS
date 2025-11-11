package com.example.budgetboltfood.fxControllers;


import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.hibernateControl.CustomHibernate;
import com.example.budgetboltfood.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    //NEVEIKIA
    //NEVEIKIA
    //NEVEIKIA
    //NEVEIKIA
    //NEVEIKIA

    public void loginBT() {
        try {
            CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
            User user = customHibernate.getUserByUsername(emailField.getText(), passwordField.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/main-form.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //NEVEIKIA
    //NEVEIKIA
    //NEVEIKIA
    //NEVEIKIA
    //NEVEIKIA

    public void registerBT(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) passwordField.getScene().getWindow();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
