package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
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

    // ----------- UI ELEMENTS ------------

    @FXML private AnchorPane userOrder;
    @FXML private ComboBox<String> atsiemimoBudas;
    @FXML private ComboBox<Driver> orderDriver;
    @FXML private TabPane mainTabPane;

    @FXML private TitledPane restaurantPane;
    @FXML private TitledPane adminPane;
    @FXML private TitledPane clientPane;
    @FXML private TitledPane driverPane;

    @FXML private Tab menuManagement;
    @FXML private Tab orderManagement;
    @FXML private Tab userManagement;
    @FXML private MenuTab menuController;


    // DB + user
    private EntityManagerFactory entityManagerFactory;
    private User loggedInUser;

    // -------------------------------------------
    @FXML
    public void initialize() {

        atsiemimoBudas.getItems().addAll("Restorane", "Pristatymas");

        // DB init
        entityManagerFactory = Persistence.createEntityManagerFactory("login");

        loadOrderDriverAndRestaurants();
        loadAllDataIntoMainTables();

        loadRestaurantPane();
        loadAdminPane();
        loadClientPane();
        loadDriverPane();
    }

    // -------------------------------------------
    private void loadOrderDriverAndRestaurants() {
        EntityManager em = entityManagerFactory.createEntityManager();

        orderDriver.getItems().setAll(
                em.createQuery("FROM Driver", Driver.class).getResultList()
        );

        // Restaurant dropdown jau išmestas iš Main, todėl čia nieko daugiau nereikia.
        em.close();
    }

    private void loadAllDataIntoMainTables() {
        // Tuščias, nes viskas iškelta į atskirus Controller
        EntityManager em = entityManagerFactory.createEntityManager();
        em.close();
    }

    // ---------------------- LOAD TITLED PANES ------------------------

    private void loadRestaurantPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/RestTitledPane.fxml"));
            Parent content = loader.load();

            RestaurantTable controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            restaurantPane.setContent(content);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadAdminPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/AdminTitledPane.fxml"));
            Parent content = loader.load();

            AdminTable controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            adminPane.setContent(content);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadClientPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/ClientTitledPane.fxml"));
            Parent content = loader.load();

            ClientTable controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            clientPane.setContent(content);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadDriverPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/DriverTitledPane.fxml"));
            Parent content = loader.load();

            DriverTable controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            driverPane.setContent(content);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ------------------------- ROLES -------------------------------

    public void setData(EntityManagerFactory emf, User user) {
        this.entityManagerFactory = emf;
        this.loggedInUser = user;

        restrictAccessByRole(); // valdymas TabPane
    }

    private void restrictAccessByRole() {
        if (loggedInUser == null) return;

        String role = loggedInUser.getClass().getSimpleName();

        switch (role) {
            case "Admin" -> {
                mainTabPane.getTabs().setAll(userManagement, orderManagement, menuManagement);
                userOrder.setVisible(false);
            }
            case "Client" -> {
                mainTabPane.getTabs().setAll(orderManagement);
                userOrder.setVisible(true);
            }
            case "Driver" -> {
                mainTabPane.getTabs().setAll(orderManagement);
                userOrder.setVisible(false);
            }
            case "Restaurant" -> {
                mainTabPane.getTabs().setAll(orderManagement, menuManagement);
                userOrder.setVisible(false);
            }
        }
    }

    // ----------------------------------------------------


    public void atsijungtiBT(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newUserAddBT(ActionEvent event) {}
    public void saveBT(ActionEvent event) {}
    public void orderManagementTab(Event event) {}
    public void saveCuisineBT(ActionEvent event) {}
    public void userManagementTab(Event event) {}
    public void menuManagementTab(Event event) {
        if (menuController != null) {
            menuController.init(entityManagerFactory, loggedInUser);
        }
    }


}
