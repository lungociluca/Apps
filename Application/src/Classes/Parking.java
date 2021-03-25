package Classes;

/** Manages parking spots */
public class Parking {
    private final int totalSpots;
    private int freeSpots;

    public Parking(int parkingSpots){
        this.totalSpots = this.freeSpots = parkingSpots;
    }

    public int getFreeSpots(){
        return freeSpots;
    }

    public boolean addCar(){
        System.out.print("ADD to parking ");
        if(freeSpots > 0){
            freeSpots--;
            System.out.println(freeSpots);
            return true;
        }
        return false;
    }

    public boolean removeCar(){
        if(freeSpots < totalSpots){
            freeSpots++;
            return true;
        }
        return false;
    }
}
