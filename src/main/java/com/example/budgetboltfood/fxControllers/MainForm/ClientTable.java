package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.model.Client;
import com.example.budgetboltfood.model.Driver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class ClientTable {

    @FXML
    private TableView<Client> clientTableView;
    @FXML
    private TableColumn<Client, Integer> clientIdColumn;
    @FXML
    private TableColumn<Client, String> clientNameColumn;
    @FXML
    private TableColumn<Client, String> clientEmailColumn;
    @FXML
    private TableColumn<Client, String> clientPasswordColumn;
    @FXML
    private TableColumn<Client, String> clientPhoneColumn;
    @FXML
    private TableColumn<Client, Void> clientDeleteColumn;

    private EntityManagerFactory emf;

    @FXML
    public void initialize() {
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        clientTableView.setEditable(true);

        clientNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientNameColumn.setOnEditCommit(e -> {
            Client d = e.getRowValue();
            d.setName(e.getNewValue());
            saveClient(d);
        });

        clientEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientEmailColumn.setOnEditCommit(e -> {
            Client d = e.getRowValue();
            d.setEmail(e.getNewValue());
            saveClient(d);
        });

        clientPasswordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientPasswordColumn.setOnEditCommit(e -> {
            Client d = e.getRowValue();
            d.setPassword(e.getNewValue());
            saveClient(d);
        });

        clientPhoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        clientPhoneColumn.setOnEditCommit(e -> {
            Client d = e.getRowValue();
            d.setPhoneNumber(e.getNewValue());
            saveClient(d);
        });
    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
        loadData();
        setupDeleteButton();
    }

    private void loadData() {
        EntityManager em = emf.createEntityManager();
        var list = em.createQuery("FROM Client", Client.class).getResultList();
        clientTableView.setItems(FXCollections.observableList(list));
        em.close();
    }

    private void setupDeleteButton() {
        clientDeleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("🗑");

            {
                btn.setStyle("-fx-background-color: transparent;");
                btn.setOnAction(e -> {
                    Client selected = getTableView().getItems().get(getIndex());
                    deleteClient(selected);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteClient(Client client) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Client managed = em.find(Client.class, client.getId());
            if (managed != null) em.remove(managed);
            em.getTransaction().commit();
            clientTableView.getItems().remove(client);

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void saveClient(Client updated) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Client db = em.find(Client.class, updated.getId());
            if (db != null) {
                db.setName(updated.getName());
                db.setEmail(updated.getEmail());
                db.setPassword(updated.getPassword());
                db.setPhoneNumber(updated.getPhoneNumber());
            }
            em.getTransaction().commit();
            System.out.println("Client updated: " + updated.getName());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
