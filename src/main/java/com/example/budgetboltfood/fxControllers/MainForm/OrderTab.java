package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private ListView<Cuisine> order;  // krepšelis
    @FXML private ListView<Alergens> alergenai;

    @FXML private Label kainaLbl;
    @FXML private Label kiekisLbl;
    @FXML private Label priceValueLbl;
    @FXML private Label quantityValueLbl;
    @FXML private CheckBox cutleryCheck;
    @FXML private ComboBox<Client> clientPicker;



    @FXML private Button sendOrderBtn;

    // === UI krepšelis (tik ekrane, ne DB) ===
    private final ObservableList<Cuisine> cartItems = FXCollections.observableArrayList();

    // === INTERNAL ===
    private EntityManagerFactory emf;
    private User loggedUser;


    // =====================================================================
    // INIT
    // =====================================================================
    public void init(EntityManagerFactory emf, User user) {
        this.emf = emf;
        this.loggedUser = user;

        loadRestaurants();
        loadDrivers();

        status.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        atsiemimoBudas.setItems(FXCollections.observableArrayList(PickUpMethod.values()));

        // kai pasirenkamas restoranas – užkrauti meniu
        restaurantPicker.valueProperty().addListener((obs, old, val) -> reloadMenu());

        if (loggedUser instanceof Admin || loggedUser instanceof Restaurant) {
            // sendOrderBtn disabled until client is selected
            sendOrderBtn.setDisable(true);

            clientPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
                sendOrderBtn.setDisable(newVal == null);
            });
        }

        menu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (loggedUser instanceof Admin || loggedUser instanceof Restaurant) {
            loadClients();
        }
        hideControlsByRole();

        // meniui leisti multi select
        menu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        menu.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cuisine c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) setText(null);
                else setText(c.getDescription() + " — " + c.getPortionSize().getPrice() + " €");
            }
        });

        // ORDER LIST VIEW = CART UI
        order.setItems(cartItems);
        order.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cuisine c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) setText(null);
                else setText(c.getDescription() + " — " + c.getPortionSize().getPrice() + " €");
            }
        });
        cutleryCheck.selectedProperty().addListener((obs, oldVal, newVal) -> updateCartSummary());

        updateCartSummary();
    }


    // =====================================================================
    // LOAD RESTAURANTS
    // =====================================================================
    private void loadRestaurants() {
        EntityManager em = emf.createEntityManager();
        List<Restaurant> restaurants =
                em.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        restaurantPicker.setItems(FXCollections.observableArrayList(restaurants));
        em.close();
    }

    // =====================================================================
    // LOAD DRIVERS
    // =====================================================================
    private void loadDrivers() {
        EntityManager em = emf.createEntityManager();
        List<Driver> drivers =
                em.createQuery("FROM Driver", Driver.class).getResultList();
        orderDriver.setItems(FXCollections.observableArrayList(drivers));
        em.close();
    }
    private void loadClients() {
        EntityManager em = emf.createEntityManager();
        List<Client> clients = em.createQuery("FROM Client", Client.class).getResultList();
        clientPicker.setItems(FXCollections.observableArrayList(clients));
        em.close();

        // --- RODYMAS DROPDOWNE ---
        clientPicker.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Client c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(c.getId() + "  " + c.getName() + " " + c.getSurname() +
                            " — " + c.getEmail());
                }
            }
        });

        // --- RODYMAS PASIRINKTAME LANGELYJE ---
        clientPicker.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Client c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(c.getId() + "  " + c.getName() + " " + c.getSurname() +
                            " — " + c.getEmail());
                }
            }
        });
    }


    // =====================================================================
    // LOAD MENU FOR SELECTED RESTAURANT
    // =====================================================================
    private void reloadMenu() {
        Restaurant rest = restaurantPicker.getValue();

        if (rest == null) {
            menu.getItems().clear();
            return;
        }

        EntityManager em = emf.createEntityManager();
        List<Cuisine> cuisines = em.createQuery(
                        "SELECT c FROM Cuisine c WHERE c.restaurantManu.id = :restId",
                        Cuisine.class)
                .setParameter("restId", rest.getId())
                .getResultList();

        menu.setItems(FXCollections.observableArrayList(cuisines));

        em.close();

        // keičiant restoraną – išvalyti krepšelį
        cartItems.clear();
    }


    // =====================================================================
    // ADD TO CART (UI ONLY, no DB)
    // =====================================================================
    @FXML
    private void addToCart() {

        List<Cuisine> selected = menu.getSelectionModel().getSelectedItems();

        if (selected == null || selected.isEmpty()) {
            showAlert("Please select at least one dish.");
            return;
        }

        cartItems.addAll(selected);
        updateCartSummary();

        showAlert("Added to cart!");
    }


    // =====================================================================
    // SEND ORDER (SAVE CART WITH MULTI ITEMS)
    // =====================================================================
    @FXML
    private void sendOrder() {

        if (cartItems.isEmpty()) {
            showAlert("Cart is empty.");
            return;
        }

        Restaurant rest = restaurantPicker.getValue();
        PickUpMethod pm = atsiemimoBudas.getValue();

        if (rest == null || pm == null) {
            showAlert("Restaurant and pick-up method must be selected.");
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Cart cart = new Cart();

            cart.setDateCreated(LocalDate.now());
            cart.setOrderStatus(OrderStatus.PENDING);
            cart.setPickUpMethod(pm);
            cart.setRestaurant(rest);
            if (loggedUser instanceof Admin || loggedUser instanceof Restaurant) {
                Client selectedClient = clientPicker.getValue();
                if (selectedClient == null) {
                    showAlert("Please select a client for this order.");
                    return;
                }
                cart.setUser(selectedClient);
            } else {
                cart.setUser(loggedUser);
            }

            cart.setMenu(cartItems);                    // visi patiekalai!
            cart.setQuantity(cartItems.size());
            cart.setTotalPrice(
                    cartItems.stream()
                            .mapToDouble(c -> c.getPortionSize().getPrice())
                            .sum()
            );

            if (loggedUser instanceof Admin && orderDriver.getValue() != null)
                cart.setDriver(orderDriver.getValue());

            em.persist(cart);
            em.getTransaction().commit();

            showAlert("Order sent!");

            cartItems.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
            showAlert("Order failed.");
        }
        finally {
            em.close();
        }
    }


    // =====================================================================
    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    private void hideControlsByRole() {

        // Client rodo tik meniu + cart funkcijas
        if (loggedUser instanceof Client) {
            orderDriver.setVisible(false);
            orderDriver.setManaged(false);

            status.setVisible(false);
            status.setManaged(false);

            clientPicker.setVisible(false);
            clientPicker.setManaged(false);


            // Paslėpti role pasirinkimą (CLIENT)
            // jei pas tave toks yra
            // pvz.: clientPickerBox.setVisible(false);

        }

        // Driver (kurjeris)
        if (loggedUser instanceof Driver) {
            // kurjeris negali rinktis driver pats
            orderDriver.setVisible(false);
            orderDriver.setManaged(false);

            // negali pasirinkti status MANUALLY (nebent nori palikti)
            status.setVisible(false);
            status.setManaged(false);

            // nerodo CLIENT pasirinkimo
            // clientCombo.setVisible(false);
        }

    }
    private void updateCartSummary() {
        int quantity = cartItems.size();
        double total = cartItems.stream()
                .mapToDouble(c -> c.getPortionSize().getPrice())
                .sum();

        // cutlery kaina × patiekalų kiekis
        if (cutleryCheck.isSelected()) {
            double cutleryCost = quantity * 0.50;
            total += cutleryCost;
        }

        quantityValueLbl.setText(String.valueOf(quantity));
        priceValueLbl.setText(String.format("%.2f €", total));
    }



}
