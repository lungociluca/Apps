package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.businessLogic.ProductBLL;
import org.example.dataAccessLayer.AbstractDAO;
import org.example.model.Product;

import java.io.IOException;
import java.util.List;

public class ProductWindowController extends Application {

    @FXML
    private TextField tx1;
    @FXML
    private TextField tx2;
    @FXML
    private TextField tx3;
    @FXML
    private TextField tx4;
    @FXML
    private Label label;

    private ProductBLL productBLL = new ProductBLL();
    private final TableView table = new TableView();
    private boolean seelctWithWhere = false;

    /**
     * Converts String to integer
     * @param s string to convert
     * @return the resulted integer
     */
    private int toInt(String s) {
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
        String[] values = tx1.getText().split(",");

        if(values.length != 4)
            return;

        Product product = new Product(toInt(values[0]),values[1],toInt(values[2]),toInt(values[3]));
        System.out.println("1");
        productBLL.insert(product);
        label.setText(productBLL.getMessage());
    }

    @FXML
    private void delete() {
        productBLL.delete(tx3.getText());
        label.setText(productBLL.getMessage());
    }

    @FXML
    private void update() {
        String[] values = tx2.getText().trim().split(",");
        if(values.length != 3) {
            label.setText("There should be 3 strings, for record name, field name, new value, in this order");
            return;
        }
        productBLL.update(values[0], values[1], values[2]);
    }

    @FXML
    private void find() throws  IOException {
        seelctWithWhere = true;
        start(new Stage());
    }

    @FXML
    private void view() throws IOException {
        seelctWithWhere = false;
        start(new Stage());
    }

    /**
     * Creates a view table, sets columns, adds records from the data base to it and displays the table
     * @param stage a new window for the view table
     */
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View");
        stage.setWidth(300);
        stage.setHeight(500);

        final Label label = new Label("Products");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        List<String> columnNames = AbstractDAO.getTableHeader(Product.class);

        //TableColumn<Client, Integer> idCol = new TableColumn<>(columnNames.get(0));
        TableColumn<Product, String> nameCol = new TableColumn<>(columnNames.get(0));
        TableColumn<Product, String> addressCol = new TableColumn<>(columnNames.get(1));
        TableColumn<Product, String> phoneCol = new TableColumn<>(columnNames.get(2));

        //idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>(columnNames.get(0)));
        addressCol.setCellValueFactory(new PropertyValueFactory<>(columnNames.get(1)));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>(columnNames.get(2)));

        List<Product> list;
        if(seelctWithWhere) {
            list = productBLL.findByName(tx4.getText());
        }
        else
            list = productBLL.view();

        ObservableList<Product> products = FXCollections.observableArrayList();
        for(Product product : list)
            products.add(product);
        table.setItems(products);

        table.getColumns().addAll(nameCol, addressCol, phoneCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }
}
