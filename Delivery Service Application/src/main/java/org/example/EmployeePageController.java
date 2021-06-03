package org.example;

import BusinessLayer.NewOrders;
import BusinessLayer.Order;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class EmployeePageController{
    @FXML
    private TextField tx;
    @FXML
    private TextArea ta;

    private List<Order> orders;

    public void setOrders(List<Order> listFromDelivery) {
        orders = listFromDelivery;
    }

    @FXML
    private void show() {
        NewOrders newOrders = new NewOrders();
        System.out.println("ceau " + newOrders.getOrdersString());
        ta.setText(newOrders.getOrdersString());
        newOrders.saveOrdersList();
    }

    @FXML
    private void back() throws IOException {
        App.setRoot("primary");
    }
    @FXML
    private void remove() {
        NewOrders newOrders = new NewOrders();
        newOrders.remove(Integer.parseInt(tx.getText()));
    }
}
