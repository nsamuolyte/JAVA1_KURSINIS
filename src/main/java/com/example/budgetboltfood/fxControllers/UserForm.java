package com.example.budgetboltfood.fxControllers;

import com.example.budgetboltfood.model.User;
import com.example.budgetboltfood.model.VehicleColor;
import com.example.budgetboltfood.model.VehicleModel;
import com.example.budgetboltfood.model.VehicleType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.PrintStream;

public class UserForm
{
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
    private ComboBox<VehicleModel> carMakeBox;
    @FXML
    private ComboBox<VehicleColor> carColourBox;
    @FXML
    public TextField carPatesField;

    public void createUser()
    {
        User user = new User(nameField.getText(), surnameField.getText(), emailField.getText(), phoneField.getText(), pwField.getText());
        PrintStream out = System.out;
        out.println(user);
    }
    public void cancel() {}

    public void disableFields()
    {
        if (userRB.isSelected())
        {
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
        }
        else if (driverRB.isSelected())
        {
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
        }
        else if (adminRB.isSelected())
        {
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
        }
        else if (restaurantRB.isSelected())
        {
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


    public void initialize() //droplistai
    {
        carTypeBox.getItems().setAll(VehicleType.values());
        carMakeBox.getItems().setAll(VehicleModel.values());
        carColourBox.getItems().setAll(VehicleColor.values());
    }
}
