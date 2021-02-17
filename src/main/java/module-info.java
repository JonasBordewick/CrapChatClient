module com.itsanoj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    opens com.itsanoj.gui to javafx.fxml, javafx.graphics;
    exports com.itsanoj.chat;
}