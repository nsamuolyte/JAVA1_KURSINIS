package com.example.budgetboltfood.fxControllers;

import com.example.budgetboltfood.model.User;
import com.example.budgetboltfood.model.VehicleColor;
import com.example.budgetboltfood.model.VehicleModel;
import com.example.budgetboltfood.model.VehicleType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class UserForm {

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

    // ==============================================================
    // CREATE NEW USER + REDIRECT TO LOGIN FORM
    // ==============================================================

    @FXML
    public void createUser(ActionEvent event) {
        try {
            // Create different user types depending on selection
            if (userRB.isSelected()) {
                User user = new User(
                        nameField.getText(),
                        surnameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        pwField.getText()
                );
                System.out.println("Created user: " + user);

            } else if (driverRB.isSelected()) {
                System.out.println("Created driver: "
                        + nameField.getText() + " " + surnameField.getText()
                        + " - " + carTypeBox.getValue() + " / " + carMakeBox.getValue()
                        + " / " + carColourBox.getValue() + " plate: " + carPatesField.getText());
            } else if (adminRB.isSelected()) {
                System.out.println("Created admin: " + nameField.getText() + " " + surnameField.getText());
            } else if (restaurantRB.isSelected()) {
                System.out.println("Created restaurant account: " + emailField.getText() + " / address: " + adressField.getText());
            }

            // =============================
            // After creation → go to login
            // =============================
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/login-form.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==============================================================
    // CANCEL BUTTON
    // ==============================================================

    @FXML
    public void cancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/login-form.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==============================================================
    // FIELD VISIBILITY HANDLING
    // ==============================================================

    public void disableFields() {
        if (userRB.isSelected()) {
            // enable
            nameField.setVisible(true);
            surnameField.setVisible(true);
            emailField.setVisible(true);
            phoneField.setVisible(true);
            pwField.setVisible(true);
            calendarBD.setVisible(true);
            adressField.setVisible(true);
            // disable
            carTypeBox.setVisible(false);
            carMakeBox.setVisible(false);
            carColourBox.setVisible(false);
            carPatesField.setVisible(false);
            calendarLE.setVisible(false);
        } else if (driverRB.isSelected()) {
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
        } else if (adminRB.isSelected()) {
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
        } else if (restaurantRB.isSelected()) {
            // enable
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

    // ==============================================================
    // COMBOBOX INITIALIZATION
    // ==============================================================

    @FXML
    public void initialize() {
        carTypeBox.getItems().setAll(VehicleType.values());
        carMakeBox.getItems().setAll(VehicleModel.values());
        carColourBox.getItems().setAll(VehicleColor.values());
    }
}
