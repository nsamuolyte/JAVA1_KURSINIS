package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class MenuTab {

    @FXML private ListView<Cuisine> cuisineView;
    @FXML private ComboBox<CuisineType> cuisineTypeBox;
    @FXML private ComboBox<Ingriedients> ingredientsBox;
    @FXML private ComboBox<Alergens> alergensBox;
    @FXML private ComboBox<PortionSize> portionSizeBox;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<Restaurant> selectRestaurant;

    private EntityManagerFactory emf;
    private User loggedUser;

    // --------- INITIALIZE ----------
    @FXML
    public void initialize() {

        // Fill dropdowns
        cuisineTypeBox.setItems(FXCollections.observableArrayList(CuisineType.values()));
        ingredientsBox.setItems(FXCollections.observableArrayList(Ingriedients.values()));
        alergensBox.setItems(FXCollections.observableArrayList(Alergens.values()));
        portionSizeBox.setItems(FXCollections.observableArrayList(PortionSize.values()));

        // Fix missing closing bracket + ListCell formatting
        cuisineView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cuisine c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    setText("#" + c.getCuisineId()
                            + " • " + c.getDescription()
                            + " • " + c.getCuisineType()
                            + " • " + c.getPortionSize());
                }
            }
        });

        // ADMIN: refresh menu when picked restaurant changes
        selectRestaurant.valueProperty().addListener((obs, oldV, newV) -> refreshMenuList());
    }

    // --------- INIT FROM MAIN FORM ----------
    public void init(EntityManagerFactory emf, User user) {
        this.emf = emf;
        this.loggedUser = user;

        loadRestaurants();

        // If Admin → auto-select FIRST restaurant
        if (loggedUser instanceof Admin) {
            if (!selectRestaurant.getItems().isEmpty()) {
                selectRestaurant.getSelectionModel().selectFirst();
            }
        }

        refreshMenuList();

        // Restaurant user → hide restaurant picker
        if (!(loggedUser instanceof Admin)) {
            selectRestaurant.setVisible(false);
            selectRestaurant.setManaged(false);
        }
    }

    private void loadRestaurants() {
        EntityManager em = emf.createEntityManager();
        List<Restaurant> list = em.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        selectRestaurant.setItems(FXCollections.observableList(list));
        em.close();
    }

    // ---------------- SAVE DISH -------------------

    @FXML
    private void saveCuisine() {

        Restaurant restaurant;

        if (loggedUser instanceof Restaurant r) {
            restaurant = r;
        } else if (loggedUser instanceof Admin) {
            restaurant = selectRestaurant.getValue();
        } else {
            return;
        }

        if (restaurant == null) return;

        CuisineType ct = cuisineTypeBox.getValue();
        PortionSize ps = portionSizeBox.getValue();
        Ingriedients ing = ingredientsBox.getValue();
        Alergens al = alergensBox.getValue();
        String name = descriptionField.getText();

        if (name == null || ct == null || ps == null) return;

        Cuisine cuisine = new Cuisine();
        cuisine.setCuisineType(ct);
        cuisine.setDescription(name);
        cuisine.setPortionSize(ps);
        cuisine.setIngriedients(ing);
        cuisine.setAlergens(al);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Restaurant managed = em.find(Restaurant.class, restaurant.getId());
            cuisine.setRestaurantManu(managed);

            em.persist(cuisine);
            em.getTransaction().commit();

            clearForm();
            refreshMenuList();
        } finally {
            em.close();
        }
    }

    private void clearForm() {
        descriptionField.clear();
        cuisineTypeBox.getSelectionModel().clearSelection();
        portionSizeBox.getSelectionModel().clearSelection();
        ingredientsBox.getSelectionModel().clearSelection();
        alergensBox.getSelectionModel().clearSelection();
    }

    private void refreshMenuList() {

        Restaurant rest;

        if (loggedUser instanceof Restaurant r) {
            rest = r;
        } else {
            rest = selectRestaurant.getValue();
        }

        if (rest == null) {
            cuisineView.getItems().clear();
            return;
        }

        EntityManager em = emf.createEntityManager();
        List<Cuisine> items = em.createQuery(
                        "SELECT c FROM Cuisine c WHERE c.restaurantManu.id = :rid ORDER BY c.cuisineId DESC",
                        Cuisine.class)
                .setParameter("rid", rest.getId())
                .getResultList();

        cuisineView.getItems().setAll(items);
        em.close();
    }

    @FXML
    public void saveCuisineBT(ActionEvent event) {
        saveCuisine();
    }

    @FXML
    public void deleteCuisine(ActionEvent event) {

        Cuisine selected = cuisineView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No dish selected");
            alert.setContentText("Please select a dish from the list before deleting.");
            alert.show();
            return;
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Cuisine managed = em.find(Cuisine.class, selected.getCuisineId());
            if (managed != null) {
                em.remove(managed);
            }

            em.getTransaction().commit();

        } finally {
            em.close();
        }

        refreshMenuList();
    }

}
