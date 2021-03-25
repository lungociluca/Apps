package sample;

import Classes.Appointments;
import Classes.Car;
import Classes.ReadFile;
import Classes.SignIn;
import javafx.css.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private Label label1;
    @FXML
    private Button makeAppointment;
    @FXML
    private Button cancelAppointment;
    @FXML
    private ToggleGroup size;
    @FXML
    private RadioButton size1;
    @FXML
    private RadioButton size2;
    @FXML
    private RadioButton size3;
    @FXML
    private ToggleGroup service;
    @FXML
    private RadioButton service1;
    @FXML
    private RadioButton service2;
    @FXML
    private TextField hour;
    @FXML
    private Label label2;
    @FXML
    private RadioButton parking1;
    @FXML
    private Button fileButton;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Label SizeLabel;
    @FXML
    private  Label ServiceLabel;

    private boolean fileContentAdded = false;
    public String s; //used for printing purposes, concatenation of data
    private int timeToWash;
    private int intHour;
    private int intMin;
    /**Am pus car static**/
    static Car car = new Car(3,5,9,16);

    static int command; // used by other classes to see if the new window was open in order to make or cancel an appointment
    static String emailDialog; // used by other class to return the email and password from another window
    static String passwordDialog;
    static boolean found;

    private boolean validHour(int value){
        return value >= 0 && value <= 23;
    }

    private boolean validMinute(int value){
        return value >= 0 && value <= 60;
    }

    /** If the size is changed  => new time to wash => update intervals when the customer can come */
    public void  sizeSelected(){
        if(!fileContentAdded) {
            fileContentAdded = true;
            addFileCommands();
        }

        if(service1.isSelected() || service2.isSelected()){
            computeTimeToWash();
            label2.setText(car.showAvaillableHours(timeToWash));
        }

        System.out.println("size changed");
    }

    /** If the service required is changed  => new time to wash => update intervals when the customer can come */
    public void serviceSelected(){
        if(!fileContentAdded) {
            fileContentAdded = true;
            addFileCommands();
        }

        if(size1.isSelected() || size2.isSelected() || size3.isSelected()){
            computeTimeToWash();
            label2.setText(car.showAvaillableHours(timeToWash));
        }
        label2.setTextFill(Color.color(0, 0, 1));
    }

    /** Redo all the commands that the previous users performed, commands stored in a text file, needs to be done when the app is opened */
    public void addFileCommands(){
        ReadFile file = new ReadFile();
        file.openFile();
        file.readFile(car);
        file.closeFile();
    }

    /** Check if the time entered by the user is valid, is of format hour:minute */
    private boolean checkIfValidHour(){
        String text = hour.getText();
        char[] textAsChar = text.toCharArray();
        int size = text.length();
        int indexBetweenHourAndMinute = text.indexOf(':');

        if(size<3 || size>5 || indexBetweenHourAndMinute < 1 || indexBetweenHourAndMinute > 2)
            return false;

        if(indexBetweenHourAndMinute == 1){
            intHour = textAsChar[0] - '0';
            if(size == 3){
                intMin = textAsChar[2] - '0';
            }
            else if(size == 4){
                intMin = (textAsChar[2] - '0')*10 + textAsChar[3] - '0';
            }
            else
                return false;
        }
        else{
            intHour = (textAsChar[0] - '0')*10 + textAsChar[1] - '0';
            if(size == 4){
                intMin = textAsChar[3] - '0';
            }
            else if(size == 5){
                intMin = (textAsChar[3] - '0')*10 + textAsChar[4] - '0';
            }
            else
                return false;
        }
        return validHour(intHour) && validMinute(intMin);
    }

    /** Calculates the time required to wash a car, time depends on the size of the car and the service the user requires */
    private boolean computeTimeToWash(){
        if(size1.isSelected()){
            timeToWash = car.getTimeToWashSmall();
            SizeLabel.setTextFill(Color.color(0, 0, 0));
        }
        else if(size2.isSelected()){
            timeToWash = car.getTimeToWashMedium();
            SizeLabel.setTextFill(Color.color(0, 0, 0));
        }
        else if(size3.isSelected()){
            timeToWash = car.getTimeToWashBig();
            SizeLabel.setTextFill(Color.color(0, 0, 0));
        }
        else{
            label1.setText("Select the size of your vehicle");
            SizeLabel.setTextFill(Color.color(1, 0, 0));
            label1.setTextFill(Color.color(1, 0, 0));
            return false;
        }

        ServiceLabel.setTextFill(Color.color(0, 0, 0));
        if(service2.isSelected()){
            if(timeToWash == car.getTimeToWashSmall())
                timeToWash += car.getTimeToWashInteriorSmall();
            else if(timeToWash == car.getTimeToWashMedium())
                timeToWash += car.getTimeToWashInteriorMedium();
            else
                timeToWash += car.getTimeToWashInteriorBig();
        }
        else if(!service1.isSelected()){
            label1.setText("Select the service you require");
            ServiceLabel.setTextFill(Color.color(1, 0, 0));
            label1.setTextFill(Color.color(1, 0, 0));
            return false;
        }


        return true;
    }

    /** Saves a new appointment IF the time is valid and if it fits in the intervals when the car wash is free to make appointments.
     *  Opens a new window where the costumer can enter an email and a password by calling showNewItemDialog */
    public void setMakeAppointment(){
        if(!computeTimeToWash())
            return;
        label2.setTextFill(Color.color(0, 0, 1));

        s = car.showAvaillableHours(timeToWash);
        label2.setText(s);

        if(!checkIfValidHour()){
            label1.setText("Invalid time entered");
            return;
        }

        //car.setTimeToWash(timeToWash);

        boolean parking = false;
        if(parking1.isSelected())
            parking = true;

        if(car.getNrOfFreeParkingSpots() == 0 && parking1.isSelected()){
            label1.setText("Can't make appointment, not enough parking spots");
            return;
        }

        command = 0;
        showNewItemDialog();

        if(car.addAppointment(intHour,intMin,timeToWash,parking,emailDialog,true)){
            label1.setText("Appointment made !");
            label1.setTextFill(Color.color(1, 0, 1));
        }
        else{
            label1.setText("This time is unavailable");
            label1.setTextFill(Color.color(1, 0, 0));
        }

        label2.setText(car.showAvaillableHours(timeToWash));
    }

    /**  Opens a new window (calls showNewItemDialog() ) where the user can enter the email and password on which the appointment was made, in order to cancel the appointment */
    public void setCancelAppointment(){
        command = 1;
        showNewItemDialog();

        if(found){
            car.deleteAppointment(emailDialog,true);
            label1.setText("Appointment deleted");
        }
        else{
            label1.setText("Appointment does not exist");
        }
    }

    /** Opens new window where an email and password and be entered */
    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Dialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            System.out.println("COULDN T LOAD");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("Ok pressed");
        }
        else {
            System.out.println("cancel pressed");
        }
    }

    /** Opens a new window for employees use only */
    public void show() {
        //label1.setText(car.printAppointmentsList(0)+car.printAppointmentsList(1)+car.printAppointmentsList(2));
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AdminPage.fxml"));
        dialog.getDialogPane().setMinHeight(500);
        dialog.getDialogPane().setMinWidth(1000);
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            System.out.println("COULDN T LOAD");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
    }
}
