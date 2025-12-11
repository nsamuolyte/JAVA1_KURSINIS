package com.example.budgetboltfood.fxControllers.MainForm;

import com.example.budgetboltfood.HelloApplication;
import com.example.budgetboltfood.fxControllers.ChatTab;
import com.example.budgetboltfood.fxControllers.UserForm;
import com.example.budgetboltfood.model.Driver;
import com.example.budgetboltfood.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainForm {

    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab userManagement;
    @FXML
    private Tab orderManagement;
    @FXML
    private Tab menuManagement;
    @FXML
    private Tab chatManagement;

    @FXML
    private TitledPane restaurantPane;
    @FXML
    private TitledPane adminPane;
    @FXML
    private TitledPane clientPane;
    @FXML
    private TitledPane driverPane;

    @FXML
    private MenuTab menuTabIncludeController;
    @FXML
    private ChatTab chatTabIncludeController;
    @FXML
    private AnchorPane orderTabInclude;

    @FXML
    private Button atsijungti;
    @FXML
    private Button newUserAdd;

    private EntityManagerFactory entityManagerFactory;
    private User loggedInUser;

    @FXML
    public void initialize() {
        entityManagerFactory = Persistence.createEntityManagerFactory("login");
        loadManagementPanes();
        loadOrderTab();
    }

    private void loadManagementPanes() {
        loadAdminPane();
        loadClientPane();
        loadDriverPane();
        loadRestaurantPane();
    }

    private void loadAdminPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/AdminTitledPane.fxml"));
            Parent content = loader.load();
            AdminTable ctrl = loader.getController();
            ctrl.setEntityManagerFactory(entityManagerFactory);
            adminPane.setContent(content);
        } catch (Exception ignored) {
        }
    }

    private void loadClientPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/ClientTitledPane.fxml"));
            Parent content = loader.load();
            ClientTable ctrl = loader.getController();
            ctrl.setEntityManagerFactory(entityManagerFactory);
            clientPane.setContent(content);
        } catch (Exception ignored) {
        }
    }

    private void loadDriverPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/DriverTitledPane.fxml"));
            Parent content = loader.load();
            DriverTable ctrl = loader.getController();
            ctrl.setEntityManagerFactory(entityManagerFactory);
            driverPane.setContent(content);
        } catch (Exception ignored) {
        }
    }

    private void loadRestaurantPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/RestTitledPane.fxml"));
            Parent content = loader.load();
            RestaurantTable ctrl = loader.getController();
            ctrl.setEntityManagerFactory(entityManagerFactory);
            restaurantPane.setContent(content);
        } catch (Exception ignored) {
        }
    }

    private void loadOrderTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/budgetboltfood/OrderManagment.fxml"));
            AnchorPane pane = loader.load();
            OrderTab orderController = loader.getController();
            orderController.init(entityManagerFactory, loggedInUser, chatTabIncludeController);
            orderTabInclude.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData(EntityManagerFactory emf, User logged) {
        this.entityManagerFactory = emf;
        this.loggedInUser = logged;
        if (menuTabIncludeController != null) menuTabIncludeController.init(emf, loggedInUser);
        loadOrderTab();
        chatTabIncludeController.init(emf, loggedInUser);
        restrictAccessByRole();
    }

    private void restrictAccessByRole() {
        if (loggedInUser == null) return;
        String role = loggedInUser.getClass().getSimpleName();

        boolean isAdmin = role.equals("Admin");
        newUserAdd.setVisible(isAdmin);
        newUserAdd.setManaged(isAdmin);

        switch (role) {
            case "Admin" -> {
                mainTabPane.getTabs().setAll(userManagement, orderManagement, menuManagement);
            }
            case "Client" -> {
                mainTabPane.getTabs().setAll(orderManagement, chatManagement);
            }
            case "Driver" -> {
                mainTabPane.getTabs().setAll(orderManagement, chatManagement);
            }
            case "Restaurant" -> {
                mainTabPane.getTabs().setAll(orderManagement, menuManagement, chatManagement);
            }
        }
    }

    @FXML
    public void atsijungtiBT(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) atsijungti.getScene().getWindow();
            stage.setTitle("LOG IN");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void newUserAddBT(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
            Parent root = loader.load();
            UserForm form = loader.getController();
            form.setData(entityManagerFactory);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Register New User");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void userManagementTab(Event e) {
    }

    @FXML
    public void orderManagementTab(Event e) {
    }
    @FXML
    public void menuManagementTab(Event e) {
    }

}
