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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainForm
{
    @FXML
    private TabPane mainTabPane;
    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;
    private User loggedInUser;


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
    @FXML private TableColumn<Restaurant, String> restaurantDeleteColumn;



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

    public void newUserAddBT(ActionEvent event) {}
    public void DeleteBT(ActionEvent event){}

    public void userManagementTab(Event event){}
    public void orderManagementTab(Event event){}
    public void menuManagementTab(Event event){}

    @FXML
    public void signOut(ActionEvent event) throws IOException {
        // Uždaryti seną EntityManagerFactory, jei dar atidarytas
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }

        // Grįžti į login langą
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();

        System.out.println("Vartotojas atsijungė ir grįžo į login ekraną.");
    }



    public void initialize() {
        entityManagerFactory = Persistence.createEntityManagerFactory("login");
        loadAllData();

        //patikra a kur gryba pjauam
        EntityManager em = entityManagerFactory.createEntityManager();
        List<User> users = em.createQuery("from User", User.class).getResultList();
        System.out.println("Found users: " + users.size());
        for (User u : users) {
            System.out.println(u.getClass().getSimpleName() + " -> " + u.getEmail());
        }

        // --- ADMIN TABLE COLUMNS ---
        adminIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        adminEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        adminPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        adminPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // --- CLIENT TABLE COLUMNS ---
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // --- DRIVER TABLE COLUMNS ---
        driverIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        driverNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        driverEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        driverPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        driverPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // --- RESTAURANT TABLE COLUMNS ---
        restaurantIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        restaurantNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        restaurantEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        restaurantPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        restaurantPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }

    private void loadAllData() {
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

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.loggedInUser = user;

        restrictAccessByRole(); // automatiškai pritaikys teises pagal tipą
    }
    private void restrictAccessByRole() {
        if (loggedInUser == null) return;

        // Jei vartotojas nėra adminas — paslepiam tabus
        if (!(loggedInUser.getClass().getSimpleName().equals("Admin")))
        {
            mainTabPane.getTabs().removeAll(userManagement, orderManagement, menuManagement);
        }
    }




    //CRAZZY???
    //I WAS CRAZY ONCE
    //THEY LOCKED ME IN A ROOM
    //IN A RUBBER ROOM
    //IN A RUBBER ROOM FILLED WITH RATS

}
