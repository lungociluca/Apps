package GUI;

import java.io.IOException;

import BusinessLogic.Operations;
import BusinessLogic.Operations.DivisionResult;
import DataModels.ProcessPolynomials2;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrimaryController {
    @FXML
    private Label outputLabel ;
    @FXML
    private Label outputLabel2;

    @FXML
    private TextField field1;
    @FXML
    private TextField field2;

    ProcessPolynomials2 p1;
    ProcessPolynomials2 p2;

    @FXML
    private void printResult(String s) {
        if(s.equals("")){
            outputLabel.setText("Result is 0");
        }
        else
            outputLabel.setText("Result is "+s);
        outputLabel2.setText(" ");
    }

    @FXML
    private boolean processPolynomials(boolean twoOperands) {
        p1 = new ProcessPolynomials2();
        p2 = new ProcessPolynomials2();

        boolean goodInput;

        goodInput = p1.process(field1.getText());
        if(twoOperands)
            goodInput = goodInput && p2.process(field2.getText());

        if(!goodInput) {
            outputLabel.setText("Your input does not respect the requested format");
            return false;
        }


        return true;
    }

    @FXML
    private void addition() {
        if(!processPolynomials(true)) {
            return;
        }
        printResult(Operations.addition(p1,p2,false));

    }
    @FXML
    private void substraction() {
        if(!processPolynomials(true))
            return;
        printResult(Operations.addition(p1,p2,true));
    }
    @FXML
    private void multiplication() {
        if(!processPolynomials(true))
            return;
        printResult(Operations.multiply(p1, p2));
    }
    @FXML
    private void division() {
        if(!processPolynomials(true))
            return;
        DivisionResult toPrint = Operations.division(p1, p2);
        outputLabel.setText("Quotient is "+toPrint.quotient);
        outputLabel2.setText("Reminder is "+toPrint.reminder);
    }
    @FXML
    private void differation() {
        if(!processPolynomials(false))
            return;
        printResult(Operations.deriv(p1));
    }
    @FXML
    private void integration() {
        if(!processPolynomials(false))
            return;
        printResult(Operations.integration(p1));
    }
}
