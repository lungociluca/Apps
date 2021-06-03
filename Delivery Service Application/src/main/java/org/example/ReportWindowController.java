package org.example;

import BusinessLayer.DeliveryService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ReportWindowController {
    @FXML
    private TextField tx1;
    @FXML
    private TextField tx2;
    @FXML
    private TextField tx3;
    @FXML
    private TextField tx4;
    @FXML
    private TextField tx5;
    @FXML
    private TextField tx6;
    @FXML
    private TextField tx7;
    @FXML
    private TextField tx8;
    @FXML
    private TextField tx9;

    private static int timeToMinutes(String hour, String minute) {
        return 60 * Integer.parseInt(hour) + Integer.parseInt(minute);
    }

    @FXML
    private void generateReport() {
        new DeliveryService().generateReport(timeToMinutes(tx1.getText(), tx2.getText()),timeToMinutes(tx3.getText(), tx4.getText()), Integer.parseInt(tx5.getText()),Integer.parseInt(tx6.getText()),Integer.parseInt(tx7.getText()),Integer.parseInt(tx8.getText()), Integer.parseInt(tx9.getText()));
    }
}
