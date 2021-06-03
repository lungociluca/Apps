package org.example;

import java.io.IOException;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void openClientWindow() throws IOException {
        App.setRoot("clientWindow");
    }
    @FXML
    private void openProductWindow() throws IOException {
        App.setRoot("productWindow");
    }
    @FXML
    private void openOrderWindow() throws IOException {
        App.setRoot("orderWindow");
    }
}

