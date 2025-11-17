package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientTable {

    @FXML private TableView<Client> clientTableView;

    @FXML private TableColumn<Client, Integer> clientIdColumn;
    @FXML private TableColumn<Client, String> clientNameColumn;
    @FXML private TableColumn<Client, String> clientEmailColumn;
    @FXML private TableColumn<Client, String> clientPasswordColumn;
    @FXML private TableColumn<Client, String> clientPhoneColumn;
    @FXML private TableColumn<Client, Void> clientDeleteColumn;

    private EntityManagerFactory emf;

    /**
     * ŠITA DALIS TIK LENTELĖS KOLONOMS!
     * JOKIO DB ČIA NEGALI BŪTI!
     */
    @FXML
    public void initialize() {
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }

    /**
     * DB INICIJAVIMAS TIK ČIA!
     */
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
}
