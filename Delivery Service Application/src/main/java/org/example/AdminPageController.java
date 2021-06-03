package org.example;

import BusinessLayer.*;
import DataLayer.LogIn;
import DataLayer.SaveAndRecover;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class AdminPageController {
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
    private TextField tx;
    @FXML
    private TextField tx_delete;
    @FXML
    private TextArea ta;
    @FXML
    private Label label;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;

    private LogIn logIn;
    private NewOrders newOrders;
    private DeliveryService deliveryService;
    private BaseProduct baseProduct;

    @FXML
    private void importSet() {
        String toPrint = "";
        List<BaseProduct> list = ImportInitialSet.read();
        for(BaseProduct product : list)
            toPrint += product.toString() + "\n";
        ta.setText(toPrint);
    }

    @FXML
    private void generateReport() throws IOException {
        App.setRoot("ReportWindow");
    }

    private void recoverData() {
        logIn = SaveAndRecover.recoverUsers();
        newOrders = new NewOrders();
        deliveryService = new DeliveryService();
    }

    private void saveData() {
        newOrders.saveOrdersList();
        deliveryService.saveDeliveryClass();
    }

    private boolean getBaseProductFromFields() {
        String name;
        float ratio;
        int calories, protein, fat, sodium, price;

        if(tx1.getText().trim().equals("")) {
            label.setText("Field name was left empty");
            System.out.println("RETURN");
            return false;
        }
        else
            name = tx1.getText();
        if(tx2.getText().trim().equals("")){
            label.setText("Field ratio was left empty");
            return false;
        }
        else
            ratio = Float.parseFloat(tx2.getText());
        if(tx3.getText().trim().equals("")){
            label.setText("Field calories was left empty");
            return false;
        }
        else
            calories = Integer.parseInt(tx3.getText());
        if(tx4.getText().trim().equals("")){
            label.setText("Field protein was left empty");
            return false;
        }
        else
            protein = Integer.parseInt(tx4.getText());
        if(tx5.getText().trim().equals("")){
            label.setText("Field fat was left empty");
            return false;
        }
        else
            fat = Integer.parseInt(tx5.getText());
        if(tx6.getText().trim().equals("")){
            label.setText("Field sodium was left empty");
            return false;
        }
        else
            sodium = Integer.parseInt(tx6.getText());
        if(tx7.getText().trim().equals("")){
            label.setText("Field price was left empty");
            return false;
        }
        else
            price = Integer.parseInt(tx7.getText());
        label.setText("OK");
        baseProduct = new BaseProduct(ratio, calories, protein, fat, sodium, name,price);

        return true;
    }

    @FXML
    private void add() {
        if(!getBaseProductFromFields())
            return;
        System.out.println(baseProduct.toString());
        recoverData();
        deliveryService.addNewProduct(baseProduct);
        saveData();
    }

    @FXML
    private void modify() {
        recoverData();
        MenuItem toUpdate = deliveryService.modifyProduct(tx.getText());
        if(toUpdate == null){
            label2.setText("No product with this name found");
            label3.setText("");
            return;
        }

        label2.setText("Product found and updated !");
        label3.setText("");

        if(!tx1.getText().trim().equals(""))
            toUpdate.setName(tx1.getText());
        if(!tx2.getText().trim().equals(""))
            toUpdate.setRating(Float.parseFloat(tx2.getText()));
        if(!tx3.getText().trim().equals(""))
            toUpdate.setCalories(Integer.parseInt(tx3.getText()));
        if(!tx4.getText().trim().equals(""))
            toUpdate.setProtein(Integer.parseInt(tx4.getText()));
        if(!tx5.getText().trim().equals(""))
            toUpdate.setFat(Integer.parseInt(tx5.getText()));
        if(!tx6.getText().trim().equals(""))
            toUpdate.setSodium(Integer.parseInt(tx6.getText()));
        if(!tx7.getText().trim().equals(""))
            toUpdate.setPrice(Integer.parseInt(tx7.getText()));
        saveData();
    }

    @FXML
    private void delete() {
        recoverData();
        deliveryService.delete(tx_delete.getText());
        label4.setText("Deleted");
        saveData();
    }

    @FXML
    private void create() throws IOException{
        App.setRoot("MenuWindow");
    }

    @FXML
    private void back() throws IOException {
        App.setRoot("primary");
    }
}
