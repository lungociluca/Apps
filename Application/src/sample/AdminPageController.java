package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class AdminPageController {
    @FXML
    private Button Done;
    @FXML
    private TextField textField;
    @FXML
    private TextField textField2;
    @FXML
    private Label label;
    @FXML
    private RadioButton sizeSmall;
    @FXML
    private RadioButton sizeMedium;
    @FXML
    private RadioButton sizeBig;
    @FXML
    private RadioButton service1;
    @FXML
    private RadioButton service2;
    @FXML
    private RadioButton service3;
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;

    private int newTime;

    public boolean check(){
        String password = textField.getText();
        if(password.equals("999")){
            enableAll(false);
            return true;
        }
        else{
            enableAll(true);
            return false;
        }

    }

    private void enableAll(boolean enable){
        service1.setDisable(enable);
        service2.setDisable(enable);
        sizeSmall.setDisable(enable);
        sizeMedium.setDisable(enable);
        sizeBig.setDisable(enable);
        b1.setDisable(enable);
        b2.setDisable(enable);
        b3.setDisable(enable);
        textField2.setDisable(enable);
        if(!enable)
            label.setText("Welcome !");
    }

    public void b1Pressed(){
        if(!check())
            return;
        label.setText(Controller.car.printAppointmentsList(0,1)+Controller.car.printAppointmentsList(1,1)+Controller.car.printAppointmentsList(2,1));
    }

    public void b2Pressed(){
        if(!check())
            return;
        label.setText(Controller.car.printAppointmentsList(0,2)+Controller.car.printAppointmentsList(1,2)+Controller.car.printAppointmentsList(2,2));
    }

    private boolean validTime(){
        String text = textField2.getText();
        int length = text.length();
        char[] textAsChar = text.toCharArray();

        newTime = 0;
        for (int i = 0; i < length; i++) {
            if(textAsChar[i] < '0' || textAsChar[i] > '9')
                return false;
            newTime += Math.pow(10, length - 1 - i) * (textAsChar[i] - '0');
        }
        System.out.println(newTime);
        return true;
    }

    public void b3Pressed(){
        if(!check()){
            label.setText("Incorrect password");
            return;
        }

        if(!validTime()){
            label.setText("Invalid time entered");
            return;
        }

        if(service1.isSelected()){
            System.out.println("time before "+Controller.car.getTimeToWashInteriorSmall());
            Controller.car.setTimeToWashInteriorSmall(newTime);
            System.out.println("time after "+Controller.car.getTimeToWashInteriorSmall());
        }
        else if(service2.isSelected()){
            Controller.car.setTimeToWashInteriorMedium(newTime);
        }
        else if(service3.isSelected()){
            Controller.car.setTimeToWashInteriorBig(newTime);
        }
        else if(sizeSmall.isSelected()){
            Controller.car.setTimeToWashSmall(newTime);
        }
        else if(sizeMedium.isSelected()){
            Controller.car.setTimeToWashMedium(newTime);
        }
        else{
            Controller.car.setTimeToWashBig(newTime);
        }

        label.setText("Change applied");
    }
}
