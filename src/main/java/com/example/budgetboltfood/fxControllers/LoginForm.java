package com.example.budgetboltfood.fxControllers;


import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.hibernateControl.CustomHibernate;
import com.example.budgetboltfood.model.User;
import com.example.budgetboltfood.utils.FxUtils;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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


    public void loginBT() throws IOException{
            CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
            User user = customHibernate.getUserByUsername(emailField.getText(), passwordField.getText());
            if(user != null)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/main-form.fxml"));
                Parent parent = fxmlLoader.load();

                Scene scene = new Scene(parent);
                Stage stage = (Stage) passwordField.getScene().getWindow();
                stage.setTitle("Main Form");
                stage.setScene(scene);
                stage.show();
            } else
            {
                FxUtils.generateAlert(Alert.AlertType.ERROR, "Error!", "Login error!", "Invalid username or password!");
            }

    }



    public void registerBT() throws IOException {
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
