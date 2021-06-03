package org.example;

import BusinessLayer.BaseProduct;
import BusinessLayer.DeliveryService;
import BusinessLayer.ImportInitialSet;
import BusinessLayer.MenuItem;
import DataLayer.LogIn;
import DataLayer.SaveAndRecover;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserPageController {
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
    private TextField tx_item;
    @FXML
    private TextField tx_name;
    @FXML
    private TextArea ta;

    private LogIn logIn;
    //private NewOrders newOrders;
    private DeliveryService deliveryService;

    @FXML
    private void importSet() {
        String toPrint = "";
        List<BaseProduct> list = ImportInitialSet.read();
        for(BaseProduct product : list)
            toPrint += product.toString() + "\n";
        ta.setText(toPrint);
    }

    private void recoverData() {
        logIn = SaveAndRecover.recoverUsers();
        //newOrders = new NewOrders();
        deliveryService = new DeliveryService();
    }

    private void saveData() {
        SaveAndRecover.save(logIn, null, null);
        //newOrders.saveOrdersList();
        deliveryService.saveDeliveryClass();
    }

    @FXML
    private void search() {
        String name = "\n";
        float ratio = -1;
        int calories, protein, fat, sodium, price;
        calories = protein = fat = sodium = price = -1;

        if(!tx1.getText().trim().equals(""))
            name = tx1.getText();
        if(!tx2.getText().trim().equals(""))
            ratio = Float.parseFloat(tx2.getText());
        if(!tx3.getText().trim().equals(""))
            calories = Integer.parseInt(tx3.getText());
        if(!tx4.getText().trim().equals(""))
            protein = Integer.parseInt(tx4.getText());
        if(!tx5.getText().trim().equals(""))
            fat = Integer.parseInt(tx5.getText());
        if(!tx6.getText().trim().equals(""))
            sodium = Integer.parseInt(tx6.getText());
        if(!tx7.getText().trim().equals(""))
            price = Integer.parseInt(tx7.getText());

        recoverData();
        List<MenuItem> toDisplay = deliveryService.searchProduct(name, ratio, calories, protein, fat, sodium, price);
        saveData();
        String s = "";
        for(MenuItem item : toDisplay)
            s += item.toString() + "\n";
        ta.setText(s);
    }

    @FXML
    private void back() throws IOException {
        App.setRoot("primary");
    }

    private String name;
    private List<String> stringList;
    @FXML
    private void add() {
        if(!tx_name.isDisable()) {
            name = tx_name.getText();
            tx_name.setDisable(true);
            stringList = new ArrayList<>();
        }
        stringList.add(tx_item.getText());
    }
    @FXML
    private void placeOrder() {
        try{
            tx_name.setDisable(false);
            recoverData();

        }catch (Exception e){
            System.out.println("Error in user controller while placing order");
        }
        deliveryService.addOrder(name, stringList);
        saveData();
    }
}
