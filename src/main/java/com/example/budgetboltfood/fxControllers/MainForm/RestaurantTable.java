package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.model.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RestaurantTable {

    @FXML private TableView<Restaurant> restaurantTableView;
    @FXML private TableColumn<Restaurant, Integer> restaurantIdColumn;
    @FXML private TableColumn<Restaurant, String> restaurantNameColumn;
    @FXML private TableColumn<Restaurant, String> restaurantEmailColumn;
    @FXML private TableColumn<Restaurant, String> restaurantPasswordColumn;
    @FXML private TableColumn<Restaurant, String> restaurantPhoneColumn;
    @FXML private TableColumn<Restaurant, Void> restaurantDeleteColumn;

    private EntityManagerFactory emf;


    @FXML
    public void initialize()
    {

        restaurantIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        restaurantNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        restaurantEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        restaurantPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        restaurantPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


    }

    public void setEntityManagerFactory(EntityManagerFactory emf)
    {
        this.emf = emf;
        loadData();
        setupDeleteColumn();
    }
    private void loadData() {
        EntityManager em = emf.createEntityManager();
        var list = em.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        restaurantTableView.setItems(FXCollections.observableList(list));
        em.close();
    }

    private void setupDeleteColumn() {
        restaurantDeleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("🗑");

            {
                btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btn.setOnAction(e -> {
                    Restaurant selected = getTableView().getItems().get(getIndex());
                    deleteRestaurant(selected);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteRestaurant(Restaurant restaurant) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Restaurant managed = em.find(Restaurant.class, restaurant.getId());

            if (managed != null) {
                em.createQuery("DELETE FROM Cuisine c WHERE c.restaurantManu.id = :rid")
                        .setParameter("rid", restaurant.getId())
                        .executeUpdate();
                em.remove(managed);
            }

            em.getTransaction().commit();
            restaurantTableView.getItems().remove(restaurant);

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }


}
