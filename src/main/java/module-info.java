module com.example.piscevic {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;

    opens com.example.piscevic to javafx.fxml;
    exports com.example.piscevic;
    exports production.model;
    exports production.exception;
    opens production.model to javafx.fxml;
}