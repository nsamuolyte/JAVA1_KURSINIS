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

    @FXML private ListView<Cart> menu;
    @FXML private ComboBox<CuisineType> cuisineTypeBox;
    @FXML private TextField descriptionTexField;
    @FXML private ComboBox<Ingriedients> ingrediantsBox;
    @FXML private ComboBox<Alergens> alergensBox;
    @FXML private ComboBox<PortionSize> portionSizeBox;
    @FXML private ListView<Cuisine> cuisineVidew;
    @FXML private ComboBox<Restaurant> selectRest;

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

    private EntityManagerFactory entityManagerFactory;
    private User loggedInUser;

    @FXML
    public void initialize() {

        // -------------------- UI ELEMENTS ---------------------
        cuisineTypeBox.setItems(FXCollections.observableArrayList(CuisineType.values()));
        ingrediantsBox.setItems(FXCollections.observableArrayList(Ingriedients.values()));
        alergensBox.setItems(FXCollections.observableArrayList(Alergens.values()));
        portionSizeBox.setItems(FXCollections.observableArrayList(PortionSize.values()));

        cuisineVidew.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cuisine c, boolean empty) {
                super.updateItem(c, empty);
                if (!empty && c != null)
                    setText("#" + c.getCuisineId() + " • " + c.getDescription()
                            + " • " + c.getCuisineType()
                            + " • " + c.getPortionSize()
                            + " • " + c.getIngriedients()
                            + " • " + c.getAlergens());
                else
                    setText(null);
            }
        });

        atsiemimoBudas.getItems().addAll("Restorane", "Pristatymas");

        // ------------ CRITICAL: INITIALIZE DB FIRST -----------------
        entityManagerFactory = Persistence.createEntityManagerFactory("login");

        loadOrderDriverAndRestaurants();
        loadAllDataIntoMainTables();

        // ------------ LOAD TitledPane CONTENTS ----------------------
        loadRestaurantPane();
        loadAdminPane();
        loadClientPane();
        loadDriverPane();
    }

    private void loadOrderDriverAndRestaurants() {
        EntityManager em = entityManagerFactory.createEntityManager();

        orderDriver.getItems().setAll(
                em.createQuery("FROM Driver", Driver.class).getResultList()
        );

        selectRest.setItems(FXCollections.observableList(
                em.createQuery("FROM Restaurant", Restaurant.class).getResultList()
        ));

        em.close();
    }

    private void loadAllDataIntoMainTables() {
        EntityManager em = entityManagerFactory.createEntityManager();

        em.close();
    }

    // ---------------------- TITLED PANES LOAD --------------------------

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
        restrictAccessByRole();
        refreshMenuList();
    }

    private void restrictAccessByRole() {
        if (loggedInUser == null) return;

        String r = loggedInUser.getClass().getSimpleName();

        switch (r) {
            case "Admin" -> {
                mainTabPane.getTabs().setAll(userManagement, orderManagement, menuManagement);
                userOrder.setVisible(false);
                selectRest.setVisible(true);
            }
            case "Client" -> {
                mainTabPane.getTabs().setAll(orderManagement);
                userOrder.setVisible(true);
                selectRest.setVisible(false);
            }
            case "Driver" -> {
                mainTabPane.getTabs().setAll(orderManagement);
                selectRest.setVisible(false);
            }
            case "Restaurant" -> {
                mainTabPane.getTabs().setAll(orderManagement, menuManagement);
                selectRest.setVisible(false);
            }
        }
    }

    // ---------------------- MENU ACTIONS -----------------------------

    private void refreshMenuList(Restaurant rest) {
        EntityManager em = entityManagerFactory.createEntityManager();

        List<Cuisine> items = em.createQuery(
                        "SELECT c FROM Cuisine c WHERE c.restaurantManu.id = :rid ORDER BY c.cuisineId DESC",
                        Cuisine.class)
                .setParameter("rid", rest.getId())
                .getResultList();

        cuisineVidew.getItems().setAll(items);

        em.close();
    }



    private void refreshMenuList() {
        if (loggedInUser instanceof Restaurant r) {
            refreshMenuList(r);
        }
    }

    public void atsijungtiBT(ActionEvent event) {
    }

    public void newUserAddBT(ActionEvent event) {
    }

    public void saveBT(ActionEvent event) {
    }

    public void orderManagementTab(Event event) {
    }

    public void saveCuisineBT(ActionEvent event) {
    }

    public void userManagementTab(Event event) {
    }

    public void menuManagementTab(Event event) {
    }
}
