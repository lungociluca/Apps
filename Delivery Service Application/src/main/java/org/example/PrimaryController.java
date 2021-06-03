package org.example;

import java.io.IOException;

import BusinessLayer.DeliveryService;
import BusinessLayer.NewOrders;
import DataLayer.LogIn;
import DataLayer.SaveAndRecover;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrimaryController {
    @FXML
    private TextField tx1;
    @FXML
    private TextField tx2;
    @FXML
    private Label label;
    private LogIn logIn;
    private NewOrders newOrders;
    private DeliveryService deliveryService;

    private void recoverData() {
        logIn =  SaveAndRecover.recoverUsers();
    }

    @FXML
    private void singIn() throws IOException {
        recoverData();
        String user = logIn.signIn(tx1.getText(), tx2.getText());
        if(user.equals("Admin")) {
            App.setRoot("AdminPage");
        }
        else if(user.equals("Client")) {
            App.setRoot("UserPage");
        }
        else if(user.equals("Employee")) {
            App.setRoot("EmployeePage");
        }
        else {
            label.setText("No such account exists");
        }
    }

    @FXML
    private void singUp() {
        recoverData();
        String s = logIn.addUser(tx1.getText(), tx2.getText());
        label.setText(s);
        SaveAndRecover.save(logIn, null, null);
    }

    @FXML
    private void getAdmin() {
        tx1.setText("Lungoci Luca");
        tx2.setText("Password");
    }

    @FXML
    private void getEmployee() {
        tx1.setText("Toma");
        tx2.setText("a1_a2_a3");
    }

    @FXML
    private void getUser() {
        tx1.setText("Victor Mihoc");
        tx2.setText("cactus");
    }
}
