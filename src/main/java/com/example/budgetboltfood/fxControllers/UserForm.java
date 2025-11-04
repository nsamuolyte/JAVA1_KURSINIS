package com.example.budgetboltfood.fxControllers;

import com.example.budgetboltfood.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    public void createUser(ActionEvent actionEvent)
    {
        User user = new User(nameField.getText(), surnameField.getText(), emailField.getText(), phoneField.getText(), pwField.getText());
    }
}
