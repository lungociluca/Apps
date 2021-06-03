package org.example;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import javafx.geometry.Insets;

import javafx.scene.text.Font;
import org.example.businessLogic.ClientBLL;
import org.example.dataAccessLayer.AbstractDAO;
import org.example.model.Client;


public class ClientWindowController extends Application {

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

    private ClientBLL clientBLL = new ClientBLL();
    private final TableView table = new TableView();
    private boolean selectWithWhere = false;

    /**
     * Gets the fields of a class written in a string and displays it on the GUI
     */
    public void setLabel() {
        List<String> list = AbstractDAO.getTableHeader(Client.class);
        String labelValue = "The fields of a record are: ";
        for(String s : list)
            labelValue += s + ", ";
        labelValue += "-";
        labelValue = labelValue.replaceAll(", -","");
        label.setText(labelValue);
    }

    /**
     * Converts String to integer
     * @param s string to convert
     * @return the resulted integer
     */
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
        String[] values = tx1.getText().split(",");

        if(values.length != 4)
            return;

        Client toAdd = new Client(toInt(values[0]),values[1],values[2],values[3]);
        clientBLL.insert(toAdd);
        label.setText(clientBLL.getMessage());
    }

    @FXML
    private void delete() {
        clientBLL.delete(tx3.getText());
        label.setText(clientBLL.getMessage());
    }

    @FXML
    private void update() {
        String[] values = tx2.getText().trim().split(",");

        if(values.length != 3) {
            label.setText("There should be 3 strings, for: record name, field to update, new value. Strings separated by a coma.");
            return;
        }

        clientBLL.update(values[0], values[1], values[2]);
    }

    @FXML
    private void find() throws  IOException {
        selectWithWhere = true;
        start(new Stage());
    }

    @FXML
    private void view() throws IOException {
        selectWithWhere = false;
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

        final Label label = new Label("Clients");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        table.setPrefWidth(300);

        List<String> columnNames = AbstractDAO.getTableHeader(Client.class);

        //TableColumn<Client, Integer> idCol = new TableColumn<>(columnNames.get(0));
        TableColumn<Client, String> nameCol = new TableColumn<>(columnNames.get(0));
        TableColumn<Client, String> addressCol = new TableColumn<>(columnNames.get(1));
        TableColumn<Client, String> phoneCol = new TableColumn<>(columnNames.get(2));

        //idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>(columnNames.get(0)));
        addressCol.setCellValueFactory(new PropertyValueFactory<>(columnNames.get(1)));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>(columnNames.get(2)));

        List<Client> list;
        if(selectWithWhere) {
            list = clientBLL.findByName(tx4.getText());
        }
        else
            list = clientBLL.view();

        ObservableList<Client> clients = FXCollections.observableArrayList();
        for(Client element : list)
            clients.add(element);
        table.setItems(clients);

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