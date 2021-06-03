package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.businessLogic.ClientBLL;
import org.example.businessLogic.OrderBLL;
import org.example.businessLogic.ProductBLL;
import org.example.businessLogic.WriteFile;
import org.example.model.Client;
import org.example.model.Order;
import org.example.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class OrderWindowController implements Initializable {
    private OrderBLL orderBLL = new OrderBLL();
    private List<Client> list;
    private List<Product> list2;
    @FXML
    private ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("id", "name"));
    @FXML
    private ChoiceBox cb2 = new ChoiceBox(FXCollections.observableArrayList("id", "name"));
    @FXML
    private Label label;
    @FXML
    private TextField tx1;
    @FXML
    private TextField tx2;

    public int toInt(String s) {
        int number = 0, powerOfTen = 1;
        char[] c = s.toCharArray();
        for(int i = s.length() - 1; i >= 0; i--) {
            number += (c[i] - '0') * powerOfTen;
            powerOfTen *= 10;
        }
        return number;
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void add() {
        Client selectedClients = ((Client) cb.getValue());
        List<Client> searchedClients = new ClientBLL().findByName(selectedClients.getName());

        if(searchedClients.size() <= 0) {
            label.setText("Client not found");
            return;
        }

        Product selectedProducts = ((Product) cb2.getValue());
        List<Product> searchedProducts = new ProductBLL().findByName(selectedProducts.getName());
        if(searchedProducts.size() <= 0) {
            label.setText("Product not found");
            return;
        }

        if(searchedProducts.get(0).getStock() < toInt(tx1.getText())) {
            label.setText("There are not enough products for the order" + searchedProducts.get(0).getStock()  + " " +toInt(tx1.getText()));
            return;
        }

        Product modifiedProduct = searchedProducts.get(0);
        modifiedProduct.decrementStock(toInt(tx1.getText()));
        //modifiedProduct.decrementStock(-toInt(tx1.getText())); //to delete

        System.out.println(modifiedProduct.toString());
        new ProductBLL().updateStock(modifiedProduct.getName(), modifiedProduct.getStock());


        int id = toInt(tx2.getText());
        System.out.println(new Order(id, searchedClients.get(0).getID(), searchedProducts.get(0).getID(), toInt(tx1.getText())).toString());
        //orderBLL.insert(new Order(id, , searchedProducts.get(0).getID(), toInt(tx1.getText())));
        orderBLL.insert(new Order(id, searchedClients.get(0).getID(), searchedProducts.get(0).getID(), toInt(tx1.getText())));
        label.setText(orderBLL.getMessage());

        createBill(searchedClients.get(0).getName(), searchedClients.get(0).getAddress(),searchedProducts.get(0).getName(), searchedProducts.get(0).getPrice(), toInt(tx1.getText()));
    }

    private void createBill(String name, String address, String productName, int price, int units) {
        WriteFile writeFile = new WriteFile();
        writeFile.openFileW();
        writeFile.addRecord(name, address, productName, units, price * units);
        writeFile.closeFileW();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ClientBLL clientBLL = new ClientBLL();
        list = clientBLL.view();
        ObservableList<Client> clients = FXCollections.observableArrayList();

        for(Client c : list)
            clients.add(c);
        cb.setItems(clients);


        ProductBLL productBLL = new ProductBLL();
        list2 = productBLL.view();
        ObservableList<Product> products = FXCollections.observableArrayList();

        for(Product p : list2) {
            //p.setStock(Math.abs(new Random().nextInt() % 100 + 1));
            //p.setPrice(Math.abs(new Random().nextInt() % 100 + 1));
            products.add(p);
        }

        cb2.setItems(products);

    }
}
