package GUI;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import BusinessLogic.SimulationManager;

public class PrimaryController {

    private class Struct {
        public int nrClients;
        public int nrQueues;
        public int simulationInterval;
        public int minArrivalTime;
        public int maxArrivalTime;
        public int minServiceTime;
        public int maxServiceTime;

        public Struct(int nrClients, int nrQueues, int simulationInterval, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
            this.nrClients = nrClients;
            this.nrQueues = nrQueues;
            this.simulationInterval = simulationInterval;
            this.minArrivalTime = minArrivalTime;
            this.maxArrivalTime = maxArrivalTime;
            this.minServiceTime = minServiceTime;
            this.maxServiceTime = maxServiceTime;
        }
    }

    @FXML
    private Label label;
    @FXML
    private Label strategy;
    @FXML
    private TextField T1;
    @FXML
    private TextField T2;
    @FXML
    private TextField T3;
    @FXML
    private TextField T4;
    @FXML
    private TextField T5;
    @FXML
    private TextField T6;
    @FXML
    private TextField T7;
    @FXML
    private TextArea area;

    private Struct struct;
    public boolean strateqyIsTime = true;
    private SimulationManager gen;

    private static int toInt(String s) { //also used in SimulationManager
        int number = 0, powerOfTen = 1;
        char[] c = s.toCharArray();
        for(int i = s.length() - 1; i >= 0; i--) {
            number += (c[i] - '0') * powerOfTen;
            powerOfTen *= 10;
        }
        return number;
    }

    private void setValuesForEmptyFields() {
        if(T1.getText().trim().equals(""))
            T1.setText("4");
        if(T2.getText().trim().equals(""))
            T2.setText("2");
        if(T3.getText().trim().equals(""))
            T3.setText("60");
        if(T4.getText().trim().equals(""))
            T4.setText("2");
        if(T5.getText().trim().equals(""))
            T5.setText("30");
        if(T6.getText().trim().equals(""))
            T6.setText("2");
        if(T7.getText().trim().equals(""))
            T7.setText("4");
    }

    private boolean checkIfInts() {
        return  true;
    }

    private boolean checkInputValues() {
        label.setTextFill(Color.color(1.0D, 0.0D, 0.0D));
        if(struct.minArrivalTime >= struct.maxArrivalTime)
            label.setText("Min arrival time not grater than max arrival time");
        else if(struct.minServiceTime >= struct.maxServiceTime)
            label.setText("Min service time not grater than max service time");
        else if(struct.nrQueues == 0 || struct.nrClients == 0)
            label.setText("There can't be 0 clients or queues");
        else if(struct.maxServiceTime >= struct.simulationInterval)
            label.setText("Service time greater than simulation time");
        else if(struct.maxArrivalTime >= struct.simulationInterval)
            label.setText("Arrival time greater than simulation time");
        else {
            label.setText(" ");
            return true;
        }
        return false;
    }

    @FXML
    private void start() throws IOException {
        setValuesForEmptyFields();

        struct = new Struct(toInt(T1.getText()), toInt(T2.getText()), toInt(T3.getText()), toInt(T4.getText()), toInt(T5.getText()), toInt(T6.getText()), toInt(T7.getText()));

        if(!checkInputValues())
            return;

        gen = new SimulationManager(struct.simulationInterval, struct.maxServiceTime, struct.minServiceTime, struct.nrQueues, struct.nrClients, struct.minArrivalTime, struct.maxArrivalTime, this);
        Thread t = new Thread(gen);
        t.start();

        //App.setRoot("secondary");
    }

    @FXML
    private void endSimulation() {
        gen.getScheduler().endServers();
        gen.currentTime = gen.getSimulationInterval();
    }

    public void addText(String s) {
        area.setText(s);
    }

    @FXML
    private void changeStrategy() {
        if(strateqyIsTime) {
            strateqyIsTime = false;
            gen.changeStrategy();
            strategy.setText("Choose queue with minimum time");
        }
        else {
            strateqyIsTime = true;
            gen.changeStrategy();
            strategy.setText("Choose queue with min. nr. of clients clients");
        }
    }
}
