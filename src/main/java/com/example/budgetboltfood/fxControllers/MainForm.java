package com.example.budgetboltfood.fxControllers;

import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.hibernateControl.GenericHibernate;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainForm
{
    @FXML
    public ListView<Cart> menu;
    @FXML
    public ComboBox<CuisineType> cuisineTypeBox;
    @FXML
    public TextField descriptionTexField;
    @FXML
    public ComboBox<Ingriedients> ingrediantsBox;
    @FXML
    public ComboBox<Alergens> alergensBox;
    @FXML
    public ComboBox<PortionSize> portionSizeBox;
    @FXML
    public ListView<Cuisine> cuisineVidew;
    @FXML
    public ComboBox<Restaurant> selectRest;
    @FXML
    public ComboBox<RestaurantStatus> status;

    @FXML
    private AnchorPane userOrder;
    @FXML
    private ComboBox<String> atsiemimoBudas;
    public ComboBox <Driver> orderDriver;
    @FXML
    private TabPane mainTabPane;
    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;
    private User loggedInUser;
    @FXML
    private javafx.scene.control.Button atsijungtiBT;


    @FXML private TableColumn<Admin, Integer> adminIdColumn;
    @FXML private TableColumn<Admin, String> adminNameColumn;
    @FXML private TableColumn<Admin, String> adminEmailColumn;
    @FXML private TableColumn<Admin, String> adminPasswordColumn;
    @FXML private TableColumn<Admin, String> adminPhoneColumn;
    @FXML private TableColumn<Admin, String> adminOrdersColumn;
    @FXML private TableColumn<Admin, String> adminDeleteColumn;

    @FXML private TableColumn<Client, Integer> clientIdColumn;
    @FXML private TableColumn<Client, String> clientNameColumn;
    @FXML private TableColumn<Client, String> clientEmailColumn;
    @FXML private TableColumn<Client, String> clientPasswordColumn;
    @FXML private TableColumn<Client, String> clientPhoneColumn;
    @FXML private TableColumn<Client, String> clientOrdersColumn;
    @FXML private TableColumn<Client, String> clientDeleteColumn;

    @FXML private TableColumn<Driver, Integer> driverIdColumn;
    @FXML private TableColumn<Driver, String> driverNameColumn;
    @FXML private TableColumn<Driver, String> driverEmailColumn;
    @FXML private TableColumn<Driver, String> driverPasswordColumn;
    @FXML private TableColumn<Driver, String> driverPhoneColumn;
    @FXML private TableColumn<Driver, String> driverOrdersColumn;
    @FXML private TableColumn<Driver, String> driverDeleteColumn;

    @FXML private TableColumn<Restaurant, Integer> restaurantIdColumn;
    @FXML private TableColumn<Restaurant, String> restaurantNameColumn;
    @FXML private TableColumn<Restaurant, String> restaurantEmailColumn;
    @FXML private TableColumn<Restaurant, String> restaurantPasswordColumn;
    @FXML private TableColumn<Restaurant, String> restaurantPhoneColumn;
    @FXML private TableColumn<Restaurant, String> restaurantOrdersColumn;
    @FXML private TableColumn<Restaurant, Void> restaurantDeleteColumn;



    @FXML
    public Tab menuManagement;
    @FXML
    public Tab orderManagement;
    @FXML
    public Tab userManagement;
    @FXML
    public TableView<Restaurant> restaurantTableView;
    @FXML
    public TableView<Admin> adminTableView;
    @FXML
    public TableView<Client> clientTableView;
    @FXML
    public TableView<Driver> driverTableView;

    public void DeleteBT(ActionEvent event){}
    public void userManagementTab(Event event){}
    public void orderManagementTab(Event event){}
    public void menuManagementTab(Event event){}

    @FXML
    public void signOut(ActionEvent event) throws IOException
    {
        
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }

        
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();

        System.out.println("Vartotojas atsijungė ir grįžo į login ekraną.");
    }
    public void atsijungtiBT(ActionEvent event) throws IOException { signOut(event); }



    public void initialize()
    {
        cuisineTypeBox.setItems(FXCollections.observableArrayList(CuisineType.values()));
        ingrediantsBox.setItems(FXCollections.observableArrayList(Ingriedients.values()));
        alergensBox.setItems(FXCollections.observableArrayList(Alergens.values()));
        portionSizeBox.setItems(FXCollections.observableArrayList(PortionSize.values()));

        cuisineVidew.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cuisine c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    //String r = (c.getRestaurant() != null) ? c.getRestaurant().getName() : "—";
                    setText("#" + c.getCuisineId() + " • " + c.getDescription()
                            + " • " + c.getCuisineType()
                            + " • " + c.getPortionSize()
                            + " • " + c.getIngriedients()
                            + " • " + c.getAlergens());
                }
            }
        });

        atsiemimoBudas.getItems().addAll("Restorane", "Pristatymas");
        entityManagerFactory = Persistence.createEntityManagerFactory("login");
        loadAllData();

        
        EntityManager em = entityManagerFactory.createEntityManager();
        List<User> users = em.createQuery("from User", User.class).getResultList();
        System.out.println("Found users: " + users.size());
        for (User u : users) { System.out.println(u.getClass().getSimpleName() + " -> " + u.getEmail()); }

        List<Driver> drivers = em.createQuery("FROM Driver", Driver.class).getResultList();
        orderDriver.getItems().setAll(drivers);

        List<Restaurant> restaurants = em.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        selectRest.setItems(FXCollections.observableList(restaurants));
        em.close();


        selectRest.valueProperty().addListener((obs, oldRest, newRest) -> {
            if (loggedInUser instanceof Admin) {
                refreshMenuList(newRest);
            }
        });

        adminIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        adminEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        adminPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        adminPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

     
        driverIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        driverNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        driverEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        driverPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        driverPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

     
        restaurantIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        restaurantNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        restaurantEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        restaurantPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        restaurantPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        setupDeleteColumn();
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.loggedInUser = user;

        restrictAccessByRole();
        refreshMenuList();
    }

    private void loadAllData()
    {
        EntityManager em = entityManagerFactory.createEntityManager();

        List<Admin> admins = em.createQuery("from Admin", Admin.class).getResultList();
        adminTableView.setItems(FXCollections.observableList(admins));

        List<Client> clients = em.createQuery("FROM Client", Client.class).getResultList();
        clientTableView.getItems().setAll(clients);

        List<Driver> drivers = em.createQuery("FROM Driver", Driver.class).getResultList();
        driverTableView.getItems().setAll(drivers);

        List<Restaurant> restaurants = em.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        restaurantTableView.getItems().setAll(restaurants);

        em.close();

    }

   private void restrictAccessByRole() {
        if (loggedInUser == null) return;

        String role = loggedInUser.getClass().getSimpleName();

        if (role.equals("Admin"))
        {
            mainTabPane.getTabs().setAll(userManagement, orderManagement, menuManagement);
            userOrder.setVisible(false);
            userOrder.setManaged(false);

            selectRest.setVisible(true);
            selectRest.setManaged(true);
            return;
        }
        if (role.equals("Client"))
        {
            mainTabPane.getTabs().clear();
            mainTabPane.getTabs().add(orderManagement);
            userOrder.setVisible(true);
            userOrder.setManaged(true);
            selectRest.setVisible(false);
            selectRest.setManaged(false);
            return;
        }
        if (role.equals("Driver"))
        {
            mainTabPane.getTabs().clear();
            mainTabPane.getTabs().add(orderManagement);
            selectRest.setVisible(false);
            selectRest.setManaged(false);
            return;
        }
        if (role.equals("Restaurant"))
        {
            mainTabPane.getTabs().clear();
            mainTabPane.getTabs().add(orderManagement);
            mainTabPane.getTabs().add(menuManagement);
            selectRest.setVisible(false);
            selectRest.setManaged(false);
        }
    }


    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void saveCuisineBT(ActionEvent event) {
        Restaurant restaurant = null;

        if (loggedInUser instanceof Restaurant rest) {
            restaurant = rest;
        } else if (loggedInUser instanceof Admin) {
            restaurant = selectRest.getValue();
            if (restaurant == null) {
                return;
            }
        } else {
            return;
        }

        CuisineType ct = cuisineTypeBox.getValue();
        Ingriedients ing = ingrediantsBox.getValue();
        Alergens al = alergensBox.getValue();
        PortionSize ps = portionSizeBox.getValue();
        String name = descriptionTexField.getText();

        if (name == null || name.isEmpty() || ct == null || ps == null) {
            return;
        }

        Cuisine cuisine = new Cuisine();
        cuisine.setCuisineType(ct);
        cuisine.setDescription(name);
        cuisine.setIngriedients(ing);
        cuisine.setAlergens(al);
        cuisine.setPortionSize(ps);

        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();

            Restaurant managedRest = em.find(Restaurant.class, restaurant.getId());
            cuisine.setRestaurantManu(managedRest);

            em.persist(cuisine);
            em.getTransaction().commit();

            clearCuisineForm();
            refreshMenuList();
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (em != null) em.close();
        }
    }


    private void clearCuisineForm() {
        cuisineTypeBox.getSelectionModel().clearSelection();
        ingrediantsBox.getSelectionModel().clearSelection();
        alergensBox.getSelectionModel().clearSelection();
        portionSizeBox.getSelectionModel().clearSelection();
        descriptionTexField.clear();
    }

    private void refreshMenuList(Restaurant restaurant) {
        if (restaurant == null) {
            cuisineVidew.getItems().clear();
            return;
        }

        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            List<Cuisine> items = em.createQuery(
                            "SELECT c FROM Cuisine c WHERE c.restaurantManu.id = :rid ORDER BY c.cuisineId DESC",
                            Cuisine.class)
                    .setParameter("rid", restaurant.getId())
                    .getResultList();
            cuisineVidew.getItems().setAll(items);
        } finally {
            em.close();
        }
    }

    private void refreshMenuList() {
        if (loggedInUser instanceof Restaurant restaurant) {
            refreshMenuList(restaurant);
        } else {
            cuisineVidew.getItems().clear();
        }
    }

    @FXML
    public void newUserAddBT(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Register New User");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Nepavyko atidaryti registracijos lango!");
        }
    }

 // new
    private void setupDeleteColumn() {
        restaurantDeleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();

            {
                // 🔸 Uždedam ikoną (emoji arba FontAwesome jei nori)
                deleteButton.setText("🗑");
                deleteButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14px;");

                deleteButton.setOnAction(event -> {
                    Restaurant selected = getTableView().getItems().get(getIndex());
                    showDeleteConfirmation(selected);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }
    private void showDeleteConfirmation(Restaurant restaurant) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Ar tikrai norite ištrinti " + restaurant.getName() + "?");

        ButtonType yesButton = new ButtonType("Taip", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                deleteRestaurant(restaurant);
            }
        });
    }
    private void deleteRestaurant(Restaurant restaurant) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();

            // Pirmiausia atnaujinam reference (jei reikia)
            Restaurant managed = em.find(Restaurant.class, restaurant.getId());
            if (managed != null) {
                em.createQuery("DELETE FROM Cuisine c WHERE c.restaurantManu.id = :rid")
                        .setParameter("rid", restaurant.getId())
                        .executeUpdate();
                em.remove(managed);
            }

            em.getTransaction().commit();

            restaurantTableView.getItems().remove(restaurant);

            showAlert(Alert.AlertType.INFORMATION, "Restoranas \"" + restaurant.getName() + "\" sėkmingai ištrintas!");
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Nepavyko ištrinti restorano: " + e.getMessage());
        } finally {
            if (em != null) em.close();
        }
    }


    @FXML
    public void saveBT(ActionEvent event) {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();

            // --- RESTAURANTS ---
            for (Restaurant r : restaurantTableView.getItems()) {
                if (r.getId() == 0) { // naujas
                    em.persist(r);
                } else {
                    em.merge(r); // atnaujintas
                }
            }

            // --- ADMINS ---
            for (Admin a : adminTableView.getItems()) {
                if (a.getId() == 0) {
                    em.persist(a);
                } else {
                    em.merge(a);
                }
            }

            // --- CLIENTS ---
            for (Client c : clientTableView.getItems()) {
                if (c.getId() == 0) {
                    em.persist(c);
                } else {
                    em.merge(c);
                }
            }

            // --- DRIVERS ---
            for (Driver d : driverTableView.getItems()) {
                if (d.getId() == 0) {
                    em.persist(d);
                } else {
                    em.merge(d);
                }
            }

            em.getTransaction().commit();
            showAlert(Alert.AlertType.INFORMATION, "Visi pakeitimai sėkmingai išsaugoti!");

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Nepavyko išsaugoti pakeitimų: " + e.getMessage());
        } finally {
            if (em != null) em.close();
        }
    }



    //CRAZZY???
    //I WAS CRAZY ONCE
    //THEY LOCKED ME IN A ROOM
    //IN A RUBBER ROOM
    //IN A RUBBER ROOM FILLED WITH RATS

}
