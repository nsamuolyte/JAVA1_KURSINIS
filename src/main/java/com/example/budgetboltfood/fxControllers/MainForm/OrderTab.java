package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class OrderTab {

    // === UI ===
    @FXML private ComboBox<Driver> orderDriver;
    @FXML private ComboBox<OrderStatus> status;
    @FXML private ComboBox<Restaurant> restaurantPicker;
    @FXML private ComboBox<PickUpMethod> atsiemimoBudas;

    @FXML private ListView<Cuisine> menu;
    @FXML private ListView<Alergens> alergenai;

    @FXML private Label kainaLbl;
    @FXML private Label kiekisLbl;

    @FXML private Button sendOrderBtn;

    // === INTERNAL ===
    private EntityManagerFactory emf;
    private User loggedUser;


    // =====================================================================
    // INIT CALLED FROM MainForm
    // =====================================================================
    public void init(EntityManagerFactory emf, User user) {
        this.emf = emf;
        this.loggedUser = user;

        loadRestaurants();
        loadDrivers();

        //cuisineTypeBox.setItems(FXCollections.observableArrayList(CuisineType.values()));

        status.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        atsiemimoBudas.setItems(FXCollections.observableArrayList(PickUpMethod.values()));

        // kai pasirenkamas restoranas – užkrauti meniu
        restaurantPicker.valueProperty().addListener((obs, old, val) -> reloadMenu());
    }


    // =====================================================================
    // LOAD RESTAURANTS
    // =====================================================================
    private void loadRestaurants() {
        EntityManager em = emf.createEntityManager();
        List<Restaurant> restaurants = em.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        restaurantPicker.setItems(FXCollections.observableArrayList(restaurants));
        em.close();
    }

    // =====================================================================
    // LOAD DRIVERS
    // =====================================================================
    private void loadDrivers() {
        EntityManager em = emf.createEntityManager();
        List<Driver> drivers = em.createQuery("FROM Driver", Driver.class).getResultList();
        orderDriver.setItems(FXCollections.observableArrayList(drivers));
        em.close();
    }


    // =====================================================================
    // LOAD MENU BASED ON RESTAURANT
    // =====================================================================
    private void reloadMenu() {

        if (restaurantPicker.getValue() == null) {
            menu.getItems().clear();
            return;
        }

        EntityManager em = emf.createEntityManager();

        List<Cuisine> cuisines = em.createQuery(
                        "SELECT c FROM Cuisine c WHERE c.restaurant Manu.id = :id", Cuisine.class)
                .setParameter("id", restaurantPicker.getValue().getId())
                .getResultList();

        menu.setItems(FXCollections.observableArrayList(cuisines));

        em.close();
    }


    // =====================================================================
    // SEND ORDER
    // =====================================================================
    @FXML
    private void sendOrder() {

        Cuisine dish = menu.getSelectionModel().getSelectedItem();
        Restaurant rest = restaurantPicker.getValue();
        PickUpMethod pm = atsiemimoBudas.getValue();

        if (dish == null || rest == null || pm == null) {
            showAlert("Please select dish, restaurant and pick-up method.");
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Cart cart = new Cart();
            cart.setDateCreated(LocalDate.now());
            cart.setOrderStatus(OrderStatus.PENDING);
            cart.setQuantity(1);
            cart.setTotalPrice(dish.getPortionSize().getPrice());
            cart.setRestaurant(rest);
            cart.setUser(loggedUser);
            cart.setMenu(List.of(dish));
            cart.setPickUpMethod(pm);

            // Admin can assign driver
            if (loggedUser instanceof Admin && orderDriver.getValue() != null) {
                cart.setDriver(orderDriver.getValue());
            }

            em.persist(cart);
            em.getTransaction().commit();

            showAlert("Order created!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error creating order.");
        } finally {
            em.close();
        }
    }


    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }
}
