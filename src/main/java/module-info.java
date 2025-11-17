module com.example.budgetboltfood {
        requires javafx.controls;
        requires javafx.fxml;

        requires lombok;
        requires org.hibernate.orm.core;
        requires java.sql;
        requires java.naming;
        requires mysql.connector.j;
        requires jakarta.persistence;
        requires org.controlsfx.controls;
        requires com.dlsc.formsfx;
        requires org.kordamp.bootstrapfx.core;
        requires java.desktop;
        requires javafx.graphics;
        requires javafx.base;


    opens com.example.budgetboltfood to javafx.fxml, org.hibernate.orm.core, jakarta.persistence;
        exports com.example.budgetboltfood;
        opens com.example.budgetboltfood.fxControllers to javafx.fxml, org.hibernate.orm.core;
        exports com.example.budgetboltfood.fxControllers;
        opens com.example.budgetboltfood.model to org.hibernate.orm.core;
        exports com.example.budgetboltfood.model;
    exports com.example.budgetboltfood.fxControllers.MainForm;
    opens com.example.budgetboltfood.fxControllers.MainForm to javafx.fxml, org.hibernate.orm.core;
}
