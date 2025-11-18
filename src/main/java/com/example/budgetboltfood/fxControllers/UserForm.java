package com.example.budgetboltfood.fxControllers;

import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.hibernateControl.GenericHibernate;
import com.example.budgetboltfood.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;

public class UserForm implements Serializable {

    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;
    @FXML
    public PasswordField pwField;

    @FXML
    public RadioButton driverRB;
    @FXML
    public RadioButton adminRB;
    @FXML
    public RadioButton userRB;
    @FXML
    public RadioButton restaurantRB;
    @FXML
    public DatePicker calendarBD;
    @FXML
    public TextField adressField;

    @FXML
    public ComboBox<VehicleType> carTypeBox;
    @FXML
    public DatePicker calendarLE;
    @FXML
    public ComboBox<VehicleModel> carMakeBox;
    @FXML
    public ComboBox<VehicleColor> carColourBox;
    @FXML
    public TextField carPatesField;
    @FXML
    public ComboBox<CuisineType> cuisineTypeField;
    public RadioButton clientRB;
    public ComboBox<RestaurantStatus> restaurantStatus;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    public void setData (EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(this.entityManagerFactory);
    }

    @FXML
    public void createUser(ActionEvent event)
    {
        if (clientRB.isSelected()) {
            Client client = new Client (emailField.getText(), pwField.getText(), nameField.getText(), surnameField.getText(), phoneField.getText(), calendarBD.getValue(), adressField.getText());
            genericHibernate.create(client);
        }
        else if  (adminRB.isSelected()) {
            Admin admin = new Admin(emailField.getText(), pwField.getText(), nameField.getText(), surnameField.getText(), phoneField.getText(), adminRB.isSelected());
            genericHibernate.create(admin);
        }
        else if  (restaurantRB.isSelected()) {
            Restaurant restaurant = new Restaurant(emailField.getText(), pwField.getText(), nameField.getText(), phoneField.getText(), adressField.getText(), cuisineTypeField.getValue(), restaurantStatus.getValue());
            genericHibernate.create(restaurant);
        }
        else if  (driverRB.isSelected()) {
            Driver driver = new Driver(emailField.getText(), pwField.getText(), nameField.getText(), surnameField.getText(), phoneField.getText(), calendarBD.getValue(), carTypeBox.getValue(), carPatesField.getText(), carMakeBox.getValue(), carColourBox.getValue());
            genericHibernate.create(driver);
        }
        // ---------- UŽDAROME REGISTRACIJOS LANGĄ ----------
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public void disableFields() {
        if (clientRB.isSelected()) {
            // enable
            nameField.setVisible(true);
            surnameField.setVisible(true);
            emailField.setVisible(true);
            phoneField.setVisible(true);
            pwField.setVisible(true);
            calendarBD.setVisible(true);
            adressField.setVisible(true);
            // disable
            cuisineTypeField.setVisible(false);
            carTypeBox.setVisible(false);
            carMakeBox.setVisible(false);
            carColourBox.setVisible(false);
            carPatesField.setVisible(false);
            calendarLE.setVisible(false);
            restaurantStatus.setVisible(false);
        }
        // Driveris
        else if (driverRB.isSelected()) {
            // enable
            nameField.setVisible(true);
            surnameField.setVisible(true);
            emailField.setVisible(true);
            phoneField.setVisible(true);
            pwField.setVisible(true);
            calendarBD.setVisible(true);
            carTypeBox.setVisible(true);
            carMakeBox.setVisible(true);
            carColourBox.setVisible(true);
            carPatesField.setVisible(true);
            calendarLE.setVisible(true);
            // disable
            adressField.setVisible(false);
            cuisineTypeField.setVisible(false);
            restaurantStatus.setVisible(false);
        }
        // Adminas
        else if (adminRB.isSelected()) {
            // enable
            nameField.setVisible(true);
            surnameField.setVisible(true);
            emailField.setVisible(true);
            phoneField.setVisible(true);
            pwField.setVisible(true);
            // disable
            adressField.setVisible(false);
            calendarBD.setVisible(false);
            carTypeBox.setVisible(false);
            carMakeBox.setVisible(false);
            carColourBox.setVisible(false);
            carPatesField.setVisible(false);
            calendarLE.setVisible(false);
            cuisineTypeField.setVisible(false);
            restaurantStatus.setVisible(false);
        }
        // Restikas
        else if (restaurantRB.isSelected())
        {
            // enable
            restaurantStatus.setVisible(true);
            cuisineTypeField.setVisible(true);
            nameField.setVisible(true);
            emailField.setVisible(true);
            phoneField.setVisible(true);
            pwField.setVisible(true);
            adressField.setVisible(true);
            // disable
            surnameField.setVisible(false);
            carTypeBox.setVisible(false);
            carMakeBox.setVisible(false);
            carColourBox.setVisible(false);
            carPatesField.setVisible(false);
            calendarLE.setVisible(false);
            calendarBD.setVisible(false);
        }
    }

    @FXML
    public void initialize()
    {
        carTypeBox.getItems().setAll(VehicleType.values());
        carMakeBox.getItems().setAll(VehicleModel.values());
        carColourBox.getItems().setAll(VehicleColor.values());
        cuisineTypeField.getItems().setAll(CuisineType.values());
        restaurantStatus.getItems().setAll(RestaurantStatus.values());
    }

    public void cancel(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
