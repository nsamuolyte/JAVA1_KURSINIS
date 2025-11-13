package com.example.budgetboltfood.fxControllers;

import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.hibernateControl.CustomHibernate;
import com.example.budgetboltfood.model.User;
import com.example.budgetboltfood.utils.FxUtils;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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


    public void loginBT() throws IOException {
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        User user = customHibernate.getUserByUsername(emailField.getText(), passwordField.getText());

        if (user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/main-form.fxml"));
            Parent parent = fxmlLoader.load();

            MainForm mainFormController = fxmlLoader.getController();
            mainFormController.setData(entityManagerFactory, user); // <- svarbu

            Scene scene = new Scene(parent);
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error!", "Login error!", "Invalid username or password!");
        }
    }

    public void registerBT() throws IOException
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
            Parent root = fxmlLoader.load();
            UserForm userForm = fxmlLoader.getController();
            userForm.setData(entityManagerFactory);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Register New User");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Nepavyko atidaryti registracijos lango!");
        }
    }


}
