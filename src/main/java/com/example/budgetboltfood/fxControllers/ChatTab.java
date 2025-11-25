package com.example.budgetboltfood.fxControllers;

import com.example.budgetboltfood.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.List;

public class ChatTab {

    @FXML private ComboBox<Cart> orderPicker;
    @FXML private ListView<String> chatList;
    @FXML private TextField messageField;

    private EntityManagerFactory emf;
    private User loggedUser;

    public void init(EntityManagerFactory emf, User user) {
        this.emf = emf;
        this.loggedUser = user;

        loadOrders();
        orderPicker.valueProperty().addListener((obs, oldV, newV) -> loadMessages());
    }

    private void loadOrders() {
        EntityManager em = emf.createEntityManager();

        List<Cart> orders;

        if (loggedUser instanceof Client c) {
            orders = em.createQuery(
                            "SELECT c FROM Cart c WHERE c.user.id = :uid", Cart.class)
                    .setParameter("uid", c.getId())
                    .getResultList();
        } else if (loggedUser instanceof Restaurant r) {
            orders = em.createQuery(
                            "SELECT c FROM Cart c WHERE c.restaurant.id = :rid", Cart.class)
                    .setParameter("rid", r.getId())
                    .getResultList();
        } else if (loggedUser instanceof Driver d) {
            orders = em.createQuery(
                            "SELECT c FROM Cart c WHERE c.driver.id = :did", Cart.class)
                    .setParameter("did", d.getId())
                    .getResultList();
        }else {
            orders = List.of();
        }

        em.close();
        orderPicker.getItems().setAll(orders);
    }

    private void loadMessages() {
        chatList.getItems().clear();
        Cart order = orderPicker.getValue();
        if (order == null) return;

        EntityManager em = emf.createEntityManager();
        List<Message> msgs = em.createQuery(
                        "SELECT m FROM Message m WHERE m.order.cartId = :oid ORDER BY m.timestamp",
                        Message.class)
                .setParameter("oid", order.getCartId())
                .getResultList();
        em.close();

        for (Message m : msgs) {
            chatList.getItems().add(
                    m.getSenderType() + ": " + m.getText()
            );
        }
    }

    @FXML
    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (msg.isEmpty()) return;

        Cart order = orderPicker.getValue();
        if (order == null) return;

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Message m = new Message();
            m.setText(msg);
            m.setTimestamp(LocalDateTime.now());
            m.setOrder(order);

            if (loggedUser instanceof Client c) {
                m.setSenderType(SenderType.CLIENT);
                m.setClient(c);
                m.setRestaurant(order.getRestaurant());
                m.setDriver(order.getDriver());
            } else if (loggedUser instanceof Restaurant r) {
                m.setSenderType(SenderType.RESTAURANT);
                m.setRestaurant(r);
                m.setClient(order.getUser());
                m.setDriver(order.getDriver());
            } else if (loggedUser instanceof Driver d) {
                m.setSenderType(SenderType.DRIVER);
                m.setDriver(d);
                m.setClient(order.getUser());
                m.setRestaurant(order.getRestaurant());
            }
            em.persist(m);
            em.getTransaction().commit();

        } finally {
            em.close();
        }

        messageField.clear();
        loadMessages();
    }
}
