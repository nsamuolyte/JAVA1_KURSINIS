package com.example.budgetboltfood.fxControllers.MainForm;

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

public class DriverTable {

    @FXML private TableView<Driver> driverTableView;

    @FXML private TableColumn<Driver, Integer> driverIdColumn;
    @FXML private TableColumn<Driver, String> driverNameColumn;
    @FXML private TableColumn<Driver, String> driverEmailColumn;
    @FXML private TableColumn<Driver, String> driverPasswordColumn;
    @FXML private TableColumn<Driver, String> driverPhoneColumn;
    @FXML private TableColumn<Driver, Void> driverDeleteColumn;

    private EntityManagerFactory emf;

    @FXML
    public void initialize() {
        driverIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        driverNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        driverEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        driverPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        driverPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
        loadData();
        setupDeleteButton();
    }

    private void loadData() {
        EntityManager em = emf.createEntityManager();
        var list = em.createQuery("FROM Driver", Driver.class).getResultList();
        driverTableView.setItems(FXCollections.observableList(list));
        em.close();
    }

    private void setupDeleteButton() {
        driverDeleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("🗑");

            {
                btn.setStyle("-fx-background-color: transparent;");
                btn.setOnAction(e -> {
                    Driver selected = getTableView().getItems().get(getIndex());
                    deleteDriver(selected);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteDriver(Driver driver) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Driver managed = em.find(Driver.class, driver.getId());
            if (managed != null) em.remove(managed);
            em.getTransaction().commit();

            driverTableView.getItems().remove(driver);

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }
}
