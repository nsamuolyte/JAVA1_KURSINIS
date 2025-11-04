module com.example.budgetboltfood {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires javafx.base;
    requires com.example.budgetboltfood;

    opens com.example.budgetboltfood to javafx.fxml;
    exports com.example.budgetboltfood;
}