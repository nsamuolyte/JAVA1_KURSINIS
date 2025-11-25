package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.model.Admin;
import com.example.budgetboltfood.model.Driver;
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
import javafx.scene.control.cell.TextFieldTableCell;

public class AdminTable {

    @FXML private TableView<Admin> adminTableView;

    @FXML private TableColumn<Admin, Integer> adminIdColumn;
    @FXML private TableColumn<Admin, String> adminNameColumn;
    @FXML private TableColumn<Admin, String> adminEmailColumn;
    @FXML private TableColumn<Admin, String> adminPasswordColumn;
    @FXML private TableColumn<Admin, Void> adminDeleteColumn;

    private EntityManagerFactory emf;


    @FXML
    public void initialize() {
        adminIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        adminEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        adminPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        adminTableView.setEditable(true);

        adminNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        adminNameColumn.setOnEditCommit(e -> {
            Admin d = e.getRowValue();
            d.setName(e.getNewValue());
            saveAdmin(d);   // <- čia SAUGO Į DB
        });

        adminEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        adminEmailColumn.setOnEditCommit(e -> {
            Admin d = e.getRowValue();
            d.setEmail(e.getNewValue());
            saveAdmin(d);
        });

        adminPasswordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        adminPasswordColumn.setOnEditCommit(e -> {
            Admin d = e.getRowValue();
            d.setPassword(e.getNewValue());
            saveAdmin(d);
        });

    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
        loadData();
        setupDeleteButton();
    }

    private void loadData() {
        EntityManager em = emf.createEntityManager();
        var list = em.createQuery("FROM Admin", Admin.class).getResultList();
        adminTableView.setItems(FXCollections.observableList(list));
        em.close();
    }

    private void setupDeleteButton() {
        adminDeleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("🗑");

            {
                btn.setStyle("-fx-background-color: transparent;");
                btn.setOnAction(e -> {
                    Admin selected = getTableView().getItems().get(getIndex());
                    deleteAdmin(selected);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void deleteAdmin(Admin admin) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Admin managed = em.find(Admin.class, admin.getId());
            if (managed != null) em.remove(managed);
            em.getTransaction().commit();
            adminTableView.getItems().remove(admin);

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void saveAdmin(Admin updated)
    {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Admin db = em.find(Admin.class, updated.getId());
            if (db != null) {
                db.setName(updated.getName());
                db.setEmail(updated.getEmail());
                db.setPassword(updated.getPassword());
            }

            em.getTransaction().commit();

            System.out.println("Admin updated: " + updated.getName());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}
