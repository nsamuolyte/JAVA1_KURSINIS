package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.fxControllers.UserForm;
import com.example.budgetboltfood.model.Driver;
import com.example.budgetboltfood.model.Restaurant;
import com.example.budgetboltfood.model.User;
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

    // --------- ORDER TAB ---------
    @FXML private AnchorPane userOrder;
    @FXML private ComboBox<String> atsiemimoBudas;
    @FXML private ComboBox<Driver> orderDriver;

    // --------- MAIN TABS ---------
    @FXML private TabPane mainTabPane;
    @FXML private Tab userManagement;
    @FXML private Tab orderManagement;
    @FXML private Tab menuManagement;
    @FXML private MenuTab menuTabIncludeController;


    // --------- TITLED PANES (Accordion inside UserManagement) ---------
    @FXML private TitledPane restaurantPane;
    @FXML private TitledPane adminPane;
    @FXML private TitledPane clientPane;
    @FXML private TitledPane driverPane;

    @FXML private Button atsijungti;
    @FXML private Button newUserAdd;
    @FXML private Button Edit;
    @FXML private Button Save;

    private EntityManagerFactory entityManagerFactory;
    private User loggedInUser;

    @FXML
    public void initialize() {

        entityManagerFactory = Persistence.createEntityManagerFactory("login");

        loadDrivers();
        loadAllPanes();

        atsiemimoBudas.getItems().addAll("Restorane", "Pristatymas");
    }

    // =====================================================================================
    //                                  LOAD SUB-PANELS
    // =====================================================================================

    private void loadAllPanes() {
        loadAdminPane();
        loadClientPane();
        loadDriverPane();
        loadRestaurantPane();
    }

    private void loadAdminPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/AdminTitledPane.fxml"));
            Parent content = loader.load();

            AdminTable controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            adminPane.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadClientPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/ClientTitledPane.fxml"));
            Parent content = loader.load();

            ClientTable controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            clientPane.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDriverPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/DriverTitledPane.fxml"));
            Parent content = loader.load();

            DriverTable controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            driverPane.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRestaurantPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/RestTitledPane.fxml"));
            Parent content = loader.load();

            RestaurantTable controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            restaurantPane.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================================================
    //                                  LOAD DRIVERS / RESTAURANTS
    // =====================================================================================

    private void loadDrivers() {
        EntityManager em = entityManagerFactory.createEntityManager();

        List<Driver> drivers = em.createQuery("FROM Driver", Driver.class).getResultList();
        orderDriver.getItems().setAll(drivers);

        em.close();
    }

    // =====================================================================================
    //                                  LOGIN → SET DATA
    // =====================================================================================

    public void setData(EntityManagerFactory emf, User user) {
        this.entityManagerFactory = emf;
        this.loggedInUser = user;

        restrictAccessByRole();

        // INIT MENU TAB (VERY IMPORTANT)
        if (menuTabIncludeController != null) {
            menuTabIncludeController.init(entityManagerFactory, loggedInUser);
        }
    }

    // =====================================================================================
    //                                  ROLE CONTROL
    // =====================================================================================

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

    // =====================================================================================
    //                               BUTTON ACTIONS FROM FXML
    // =====================================================================================

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
            System.out.println("Nepavyko atidaryti registracijos lango!");
        }
    }

    @FXML
    public void saveBT(ActionEvent event) {
        System.out.println("Save button pressed – not implemented yet.");
    }

    @FXML
    public void userManagementTab(Event event) {}

    @FXML
    public void orderManagementTab(Event event) {}

    @FXML
    public void menuManagementTab(Event event) {}
}
