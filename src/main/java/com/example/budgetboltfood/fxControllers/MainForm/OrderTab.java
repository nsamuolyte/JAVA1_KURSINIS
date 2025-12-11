package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.fxControllers.ChatTab;
import com.example.budgetboltfood.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class OrderTab {

    public Button addBT;
    public Button editBT;
    private ChatTab chatTab;
    // === UI ===
    @FXML
    private ComboBox<Driver> orderDriver;
    @FXML
    private ComboBox<OrderStatus> status;
    @FXML
    private ComboBox<Restaurant> restaurantPicker;
    @FXML
    private ComboBox<PickUpMethod> atsiemimoBudas;

    @FXML
    private ListView<Cuisine> menu;
    @FXML
    private ListView<Cuisine> order;
    @FXML
    private ListView<Alergens> alergenai;
    @FXML
    private ListView<Cart> allOrders;

    @FXML
    private Label kainaLbl;
    @FXML
    private Label kiekisLbl;
    @FXML
    private Label priceValueLbl;
    @FXML
    private Label quantityValueLbl;
    @FXML
    private CheckBox cutleryCheck;
    @FXML
    private ComboBox<Client> clientPicker;

    @FXML
    private ListView<Cart> orderList;


    @FXML
    private Button sendOrderBtn;

    private final ObservableList<Cuisine> cartItems = FXCollections.observableArrayList();

    private EntityManagerFactory emf;
    private User loggedUser;

    public void init(EntityManagerFactory emf, User user, ChatTab chatTab) {
        this.emf = emf;
        this.loggedUser = user;
        this.chatTab = chatTab;

        loadRestaurants();
        loadDrivers();
        loadAllOrders();

        status.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        atsiemimoBudas.setItems(FXCollections.observableArrayList(PickUpMethod.values()));

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

        menu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        menu.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cuisine c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) setText(null);
                else setText(c.getDescription() + " — " + c.getPortionSize().getPrice() + " €");
            }
        });

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

    private void loadRestaurants() {
        EntityManager em = emf.createEntityManager();
        List<Restaurant> restaurants =
                em.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        restaurantPicker.setItems(FXCollections.observableArrayList(restaurants));
        em.close();
    }

    private void loadAllOrders() {

        EntityManager em = emf.createEntityManager();
        List<Cart> orders;


        if (loggedUser instanceof Admin || loggedUser instanceof Driver) {
            orders = em.createQuery(
                    "FROM Cart ORDER BY cartId DESC", Cart.class
            ).getResultList();
        } else if (loggedUser instanceof Restaurant) {
            orders = em.createQuery(
                            "SELECT c FROM Cart c WHERE c.restaurant.id = :restId ORDER BY c.cartId DESC",
                            Cart.class
                    )
                    .setParameter("restId", loggedUser.getId())   // Restaurant user ID = restaurant ID
                    .getResultList();
        } else {
            orders = em.createQuery(
                            "SELECT c FROM Cart c WHERE c.user.id = :uid ORDER BY c.cartId DESC",
                            Cart.class
                    )
                    .setParameter("uid", loggedUser.getId())
                    .getResultList();
        }

        em.close();

        allOrders.setItems(FXCollections.observableArrayList(orders));

        allOrders.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cart c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(
                            "#" + c.getCartId() + " | " +
                                    c.getUser().getName() + " " + c.getUser().getSurname() +
                                    " | " + String.format("%.2f€", c.getTotalPrice()) +
                                    " | " + c.getOrderStatus()
                    );

                    if (c.getOrderStatus() == OrderStatus.CANCELLED) {
                        setStyle("-fx-text-fill: red;");
                    } else if (c.getOrderStatus() == OrderStatus.ON_THE_WAY) {
                        setStyle("-fx-text-fill: blue;");
                    } else if (c.getOrderStatus() == OrderStatus.ACCEPTED) {
                        setStyle("-fx-text-fill: yellow;");
                    } else if (c.getOrderStatus() == OrderStatus.IN_PROGRESS) {
                        setStyle("-fx-text-fill: pink;");
                    } else if (c.getOrderStatus() == OrderStatus.DELIVERED) {
                        setStyle("-fx-text-fill: green;");
                    } else if (c.getOrderStatus() == OrderStatus.READY) {
                        setStyle("-fx-text-fill: purple;");
                    } else {
                        setStyle(""); // normal
                    }

                }
            }
        });
    }

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
        cartItems.clear();
    }

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

            cart.setMenu(cartItems); // visi patiekalai!
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
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Order failed.");
        } finally {
            em.close();
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    @FXML
    private void addDriverToOrder() {

        Cart selectedOrder = allOrders.getSelectionModel().getSelectedItem();
        Driver selectedDriver = orderDriver.getValue();
        OrderStatus selectedStatus = status.getValue();

        if (selectedOrder == null) {
            showAlert("Please select an order first.");
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Cart cart = em.find(Cart.class, selectedOrder.getCartId());

            boolean hadNoDriverBefore = (cart.getDriver() == null);

            if (selectedDriver != null) {
                cart.setDriver(selectedDriver);

                if (hadNoDriverBefore) {
                    cart.setOrderStatus(OrderStatus.ON_THE_WAY);
                }
            }

            if (selectedStatus != null) {
                cart.setOrderStatus(selectedStatus);
            }

            em.merge(cart);
            em.getTransaction().commit();

            showAlert("Order updated!");

            loadAllOrders(); // refresh

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Could not update order.");
        } finally {
            em.close();
        }
    }


    private void reloadOrdersList() {
        EntityManager em = emf.createEntityManager();
        List<Cart> carts = em.createQuery("FROM Cart", Cart.class).getResultList();
        orderList.setItems(FXCollections.observableArrayList(carts));
        em.close();
    }


    private void hideControlsByRole() {

        // Client rodo tik meniu + cart funkcijas
        if (loggedUser instanceof Client) {
            orderDriver.setVisible(false);
            orderDriver.setManaged(false);

            status.setVisible(false);
            status.setVisible(false);
            status.setManaged(false);

            clientPicker.setVisible(false);
            clientPicker.setManaged(false);

            addBT.setVisible(false);
            addBT.setManaged(false);

            editBT.setVisible(false);
            editBT.setManaged(false);


        }

        if (loggedUser instanceof Driver) {

            orderDriver.setVisible(false);
            orderDriver.setManaged(false);

            status.setVisible(false);
            status.setManaged(false);
        }

    }

    private void updateCartSummary() {
        int quantity = cartItems.size();
        double total = cartItems.stream()
                .mapToDouble(c -> c.getPortionSize().getPrice())
                .sum();

        if (cutleryCheck.isSelected()) {
            double cutleryCost = quantity * 0.50;
            total += cutleryCost;
        }

        quantityValueLbl.setText(String.valueOf(quantity));
        priceValueLbl.setText(String.format("%.2f €", total));
    }

    @FXML
    private void canceling() {

        Cart selected = allOrders.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select an order to cancel.");
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Cart cart = em.find(Cart.class, selected.getCartId());
            cart.setOrderStatus(OrderStatus.CANCELLED);

            em.merge(cart);
            em.getTransaction().commit();

            showAlert("Order cancelled!");

            loadAllOrders(); // reload list

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Could not cancel order.");
        } finally {
            em.close();
        }
    }


    @FXML
    private void reloadOrders() {
        loadAllOrders();
        showAlert("Orders reloaded!");
    }

    @FXML
    private void editOrder() {

        Cart selected = allOrders.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select an order to edit.");
            return;
        }

        // set driver if exists
        if (selected.getDriver() != null) {
            orderDriver.setValue(selected.getDriver());
        } else {
            orderDriver.setValue(null);
        }

        status.setValue(selected.getOrderStatus());

        showAlert("Order loaded. Make changes and press ADD to save.");
    }

    @FXML
    public void deleted(ActionEvent event) {

        Cart selected = allOrders.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select an order to delete.");
            return;
        }
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Cart managed = em.find(Cart.class, selected.getCartId());
            if (managed == null) {
                showAlert("Order no longer exists.");
                em.getTransaction().rollback();
                return;
            }

            int cartId = managed.getCartId();
            em.createQuery("DELETE FROM Message m WHERE m.order.cartId = :oid")
                    .setParameter("oid", cartId)
                    .executeUpdate();
            em.createNativeQuery("DELETE FROM Cart_Cuisine WHERE orderList_cartId = ?")
                    .setParameter(1, cartId)
                    .executeUpdate();

            em.remove(managed);

            em.getTransaction().commit();

            showAlert("Order deleted successfully!");

            loadAllOrders();

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            showAlert("Could not delete order.");
        } finally {
            em.close();
        }
    }


}
