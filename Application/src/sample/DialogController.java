package sample;

import Classes.ReadFile;
import Classes.SignIn;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class DialogController {
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private Label obs;
    @FXML
    private BorderPane secondaryBorderPane;

    /** See if the email and password respect the rules displayed on the screen, save changes if so
     *  This is called when the user wanted to make an appointment */
    public void processResults(){
        if(Controller.command == 1){
            processResults_CancelAppointment();
            return;
        }

        String email = this.email.getText();
        String password = this.password.getText();

        SignIn sign = new SignIn();
        sign.setEmail(email);
        sign.setPassword(password);
        String returned = sign.addUser();
        obs.setText(returned);
        Controller.emailDialog = email;

    }

    /** Same as above, but if the user wanted to cancel an appointment */
    public void processResults_CancelAppointment(){
        String email = this.email.getText();
        String password = this.password.getText();


        SignIn sign = new SignIn();
        sign.setEmail(email);
        sign.setPassword(password);

        boolean exists = sign.exists();

        if(exists){
            Controller.emailDialog = email;
            Controller.passwordDialog = password;
            Controller.found = true;
            obs.setText("Appointment found, click cancel");

            try{
                File file = new File("inp");
                FileWriter fr = new FileWriter(file, true);
                fr.write("delete "+email);
                fr.close();
            }catch (Exception e){
                System.out.println("file not found");
            }
        }
        else{
            Controller.found = false;
            obs.setText("Couldn't find your appointment");
        }
    }
}
