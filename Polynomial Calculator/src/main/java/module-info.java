module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;

    opens GUI to javafx.fxml;
    exports  test to junit;
    exports GUI;
}