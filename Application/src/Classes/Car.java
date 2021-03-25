package Classes;

public class Car extends Appointments{
    private int timeToWashSmall = 10;
    private int timeToWashMedium = 12;
    private int timeToWashBig = 14;
    private int timeToWashInteriorSmall = 5;
    private int timeToWashInteriorMedium = 8;
    private int timeToWashInteriorBig = 15;
    private int timeToWash;

    public int getTimeToWashSmall() {
        return timeToWashSmall;
    }

    public int getTimeToWashMedium() {
        return timeToWashMedium;
    }

    public int getTimeToWashBig() {
        return timeToWashBig;
    }

    public int getTimeToWashInteriorSmall() {
        return timeToWashInteriorSmall;
    }

    public int getTimeToWashInteriorMedium() {
        return timeToWashInteriorMedium;
    }

    public int getTimeToWashInteriorBig() {
        return timeToWashInteriorBig;
    }

    public void setTimeToWashSmall(int timeToWashSmall) {
        this.timeToWashSmall = timeToWashSmall;
    }

    public void setTimeToWashMedium(int timeToWashMedium) {
        this.timeToWashMedium = timeToWashMedium;
    }

    public void setTimeToWashBig(int timeToWashBig) {
        this.timeToWashBig = timeToWashBig;
    }

    public void setTimeToWashInteriorSmall(int timeToWashInteriorSmall) {
        this.timeToWashInteriorSmall = timeToWashInteriorSmall;
    }

    public void setTimeToWashInteriorMedium(int timeToWashInteriorMedium) {
        this.timeToWashInteriorMedium = timeToWashInteriorMedium;
    }

    public void setTimeToWashInteriorBig(int timeToWashInteriorBig) {
        this.timeToWashInteriorBig = timeToWashInteriorBig;
    }

    public Car(int nrOfStations, int nrOfParkingSpots, int openHour, int closeHour) {
        super(nrOfStations, nrOfParkingSpots, openHour, closeHour);
    }

}
