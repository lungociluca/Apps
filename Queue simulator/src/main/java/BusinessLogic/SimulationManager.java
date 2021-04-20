package BusinessLogic;

import DataModels.Task;
import GUI.PrimaryController;
import DataModels.Scheduler;

import java.util.*;

public class SimulationManager implements Runnable{
    public int simulationInterval;
    public int maxServiceTime;
    public int minServiceTime;
    public int nrQueues;
    public int nrClients;
    private int minArrivalTime;
    private int maxArrivalTime;
    //private Label output;
    private PrimaryController primaryController;
    private int avgWaitingTime, avgServiceTime;

    private Scheduler scheduler;
    private List<Task> generatedTasks;

    public SimulationManager(int simulationInterval, int maxServiceTime, int minServiceTime, int nrQueues, int nrClients, int minArrivalTime, int maxArrivalTime, PrimaryController primaryController) {
        this.simulationInterval = simulationInterval;
        this.maxServiceTime = maxServiceTime;
        this.minServiceTime = minServiceTime;
        this.nrQueues = nrQueues;
        this.nrClients = nrClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.generatedTasks = new ArrayList<>();
        this.primaryController = primaryController;
        this.scheduler = new Scheduler(nrQueues, 5);
        avgServiceTime = generateNRandomTasks(nrClients) / nrClients;
    }

    public void changeStrategy() {
        scheduler.changeStrategy();
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    private int generateNRandomTasks(int n) {
        int totalServiceTime = 0;
        System.out.println("Generated tasks");
        Random random = new Random();
        if(n > nrClients)
            n = 100;
        for(int i = 0; i < n; i++) {
            int ID = i + 1, arrivalTime, processingTime;
            arrivalTime = Math.abs(random.nextInt())  % (maxArrivalTime);
            if(arrivalTime < minArrivalTime)
                arrivalTime = (arrivalTime + minArrivalTime) % maxArrivalTime;

            processingTime = Math.abs(random.nextInt())  % maxServiceTime;
            if(processingTime < minServiceTime)
                processingTime = (processingTime + minServiceTime) % maxServiceTime;
            generatedTasks.add(new Task(ID, arrivalTime, processingTime));
            totalServiceTime += processingTime;
        }
        Collections.sort(generatedTasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if(o1.getArrivalTime() <= o2.getArrivalTime())
                    return -1;
                else
                    return 0;
            }
        });
        return totalServiceTime;
    }

    private String listToString(int nr) {
        String s = "";
        for(Task t : generatedTasks) {
            if(t.getArrivalTime() > nr) {
                s += "(" + t.getID() + ", " + t.getArrivalTime() + ", " + t.getProcessingTime() + ")";
            }
        }
        return s;
    }

    public String toString(int currentTime) {
        String s = "";
        s += "Time "+ currentTime + "\n";
        s += "Waiting clients: " + listToString(currentTime) + "\n";
        s += scheduler.toString();
        scheduler.forPeakHour(currentTime);
        s += "Peak hour: " + scheduler.getPeakHour() + ", average waiting time: " + avgWaitingTime + ", average service time: " +avgServiceTime+  "\n";
        return s;
    }

    public int getSimulationInterval() {
        return simulationInterval;
    }

    private void setAvgWaitingTime() {
        int sum = 0;
        for(Task t : generatedTasks) {
            sum += t.getWaitingTime();
        }
        avgWaitingTime = sum / nrClients;
    }

    public int currentTime; //declaration as instance var. so to end run() method from outside the class, modifying "currentTime"
    @Override
    public void run() {
        currentTime = 0;
        WriteFile file = new WriteFile();
        file.openFileW();
        while(currentTime < simulationInterval) {
            for(Task t : generatedTasks) {
                if(t.getArrivalTime() == currentTime) {
                    try {
                        scheduler.dispatchTask(t);
                    } catch (Exception e) {
                        System.out.println("Some exception was thrown");
                        return;
                    }
                }
            }
            String result = toString(currentTime);
            primaryController.addText(result);
            file.addRecord(result + "\n");
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread from SimulationManager was interrupted");
            }
            setAvgWaitingTime();
        }

        file.closeFileW();
        scheduler.endServers();
    }
}
