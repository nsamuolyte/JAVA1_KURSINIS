package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.fxControllers.UserForm;
import com.example.budgetboltfood.model.Driver;
import com.example.budgetboltfood.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainForm {

    // === MAIN TABS ===
    @FXML private TabPane mainTabPane;
    @FXML private Tab userManagement;
    @FXML private Tab orderManagement;
    @FXML private Tab menuManagement;

    // === USER MANAGEMENT TITLED PANES ===
    @FXML private TitledPane restaurantPane;
    @FXML private TitledPane adminPane;
    @FXML private TitledPane clientPane;
    @FXML private TitledPane driverPane;

    // === MENU TAB injected by fx:include (controller) ===
    @FXML private MenuTab menuTabIncludeController;

    // === ORDER TAB root (fx:include gives only Node!) ===

    @FXML private AnchorPane orderTabInclude;

    // === UI BUTTONS ===
    @FXML private Button atsijungti;
    @FXML private Button newUserAdd;

    private EntityManagerFactory entityManagerFactory;
    private User loggedInUser;


    // =====================================================================
    // INITIALIZE — called AFTER FXML loads
    // =====================================================================
    @FXML
    public void initialize() {
        entityManagerFactory = Persistence.createEntityManagerFactory("login");

        loadManagementPanes();  // Admin/Client/Driver/Restaurant
        loadOrderTab();         // Load Order Tab controller manually
    }


    // =====================================================================
    // LOAD TITLED PANES
    // =====================================================================
    private void loadManagementPanes() {
        loadAdminPane();
        loadClientPane();
        loadDriverPane();
        loadRestaurantPane();
    }

    private void loadAdminPane() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/budgetboltfood/AdminTitledPane.fxml")
            );
            Parent content = loader.load();
            AdminTable ctrl = loader.getController();
            ctrl.setEntityManagerFactory(entityManagerFactory);
            adminPane.setContent(content);
        } catch (Exception ignored) {}
    }

    private void loadClientPane() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/budgetboltfood/ClientTitledPane.fxml")
            );
            Parent content = loader.load();
            ClientTable ctrl = loader.getController();
            ctrl.setEntityManagerFactory(entityManagerFactory);
            clientPane.setContent(content);
        } catch (Exception ignored) {}
    }

    private void loadDriverPane() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/budgetboltfood/DriverTitledPane.fxml")
            );
            Parent content = loader.load();
            DriverTable ctrl = loader.getController();
            ctrl.setEntityManagerFactory(entityManagerFactory);
            driverPane.setContent(content);
        } catch (Exception ignored) {}
    }

    private void loadRestaurantPane() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/budgetboltfood/RestTitledPane.fxml")
            );
            Parent content = loader.load();
            RestaurantTable ctrl = loader.getController();
            ctrl.setEntityManagerFactory(entityManagerFactory);
            restaurantPane.setContent(content);
        } catch (Exception ignored) {}
    }


    // =====================================================================
    // LOAD ORDER TAB MANUALLY BECAUSE fx:include ONLY LOADS NODE!
    // =====================================================================
    private void loadOrderTab() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/budgetboltfood/OrderManagment.fxml")
            );

            AnchorPane pane = loader.load();
            OrderTab orderController = loader.getController();
            orderController.init(entityManagerFactory, loggedInUser);

            orderTabInclude.getChildren().setAll(pane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // =====================================================================
    // LOGIN → PASSES ENTITY MANAGER FACTORY + LOGGED-IN USER
    // =====================================================================
    public void setData(EntityManagerFactory emf, User logged) {

        this.entityManagerFactory = emf;
        this.loggedInUser = logged;

        if (menuTabIncludeController != null)
            menuTabIncludeController.init(emf, loggedInUser);
        loadOrderTab();

        restrictAccessByRole();
    }


    // =====================================================================
    // ROLE: WHICH TABS YOU SEE
    // =====================================================================
    private void restrictAccessByRole() {

        if (loggedInUser == null) return;

        String role = loggedInUser.getClass().getSimpleName();

        switch (role) {
            case "Admin" -> {
                mainTabPane.getTabs().setAll(userManagement, orderManagement, menuManagement);
            }

            case "Client" -> {
                mainTabPane.getTabs().setAll(orderManagement);
            }

            case "Driver" -> {
                mainTabPane.getTabs().setAll(orderManagement);
            }

            case "Restaurant" -> {
                mainTabPane.getTabs().setAll(orderManagement, menuManagement);
            }
        }
    }


    // =====================================================================
    // BUTTON ACTIONS
    // =====================================================================

    @FXML
    public void atsijungtiBT(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) atsijungti.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void newUserAddBT(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
            Parent root = loader.load();

            UserForm form = loader.getController();
            form.setData(entityManagerFactory);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Register New User");
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void userManagementTab(Event e) {}
    @FXML public void orderManagementTab(Event e) {}
    @FXML public void menuManagementTab(Event e) {}

}
