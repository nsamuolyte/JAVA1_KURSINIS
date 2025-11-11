package com.example.budgetboltfood.fxControllers;

import com.example.budgetboltfood.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainForm
{
    public void signOutBT() throws IOException {
    }

    public void newUserAddBT(ActionEvent event) {
    }

    public void signOut(ActionEvent event) throws IOException
    {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
    }
}
