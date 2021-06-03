package org.example;

import BusinessLayer.CompositeProduct;
import BusinessLayer.DeliveryService;
import BusinessLayer.MenuItem;
import BusinessLayer.NewOrders;
import DataLayer.LogIn;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuWindowController {
    @FXML
    private TextField tx1;
    @FXML
    private TextField tx2;
    @FXML
    private TextArea ta;
    @FXML
    private Label label;

    private LogIn logIn;
    private NewOrders newOrders;
    private DeliveryService deliveryService;

    private String menuName;
    private List<String> items;

    private void recoverData() {
        newOrders = new NewOrders();
        deliveryService = new DeliveryService();
    }

    private void saveData() {
        newOrders.saveOrdersList();
        deliveryService.saveDeliveryClass();
    }

    @FXML
    private void back() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void add() {
        if(!tx1.isDisable()) {
            menuName = tx1.getText();
            tx1.setDisable(true);
            items = new ArrayList<>();
        }
        items.add(tx2.getText());
    }

    @FXML
    private void registerMenu() {
        label.setText("Your menu was registered");
        tx1.setDisable(false);
        CompositeProduct compositeProduct = new CompositeProduct(0, 0, 0, 0, 0, menuName);
        recoverData();
        try {
            for(String name : items) {

                MenuItem toAdd = deliveryService.searchProduct(name, -1, -1, -1, -1, -1 ,-1).get(0);
                compositeProduct.addItem(toAdd);
            }
            compositeProduct.computePrice();
            deliveryService.addNewCompositeProduct(compositeProduct);

        }catch (Exception e){
            System.out.println("Error while creating a new menu");
        }
        saveData();
    }

    @FXML
    private void find() {

    }
}
