package DataModels;

public class Task {
    private int arrivalTime;
    private int processingTime;
    private int ID;
    private int waitingTime;

    public Task(int ID, int arrivalTime, int processingTime) {
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.ID = ID;
        waitingTime = 0;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getID() {
        return ID;
    }

    public void decrementServiceTime() {
        processingTime--;
    }

    public void incrementWaitingTime() {
        waitingTime++;
    }
}
