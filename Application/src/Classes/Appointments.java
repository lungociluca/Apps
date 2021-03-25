package Classes;

/** This class manages the lists with appointments for each car wash station */
public class Appointments {

    private int nrOfStations;
    private _List[] stations; //array of pointers to the head of each list
    Parking parking;

    public Appointments(int nrOfStations, int nrOfParkingSpots, int openHour, int closeHour){
        this.parking = new Parking(nrOfParkingSpots);
        this.nrOfStations = nrOfStations;
        stations = new _List[nrOfStations];
        for(int i = 0; i < nrOfStations; i++){
            stations[i] = new _List(openHour,closeHour);
        }
    }

    public int getNrOfFreeParkingSpots(){
        return parking.getFreeSpots();
    }

    /** Traverse each of the lists and see if we can insert the new appointment in one of them, return true if insertion was done.
     *  If the user required a parking spot also check if there are free parking spots */
    public boolean addAppointment(int startHour, int startMinute, int timeTowash, boolean needsParcking, String email, boolean updateFile){
        for(int i = 0; i < nrOfStations; i++){
            if(stations[i].insert(startHour,startMinute,timeTowash,needsParcking,email,updateFile)){
                //System.out.println("Appointment made");
                if(needsParcking){
                    boolean ok = parking.addCar();
                    return ok;
                }
                return true;
            }
        }
        return false;
    }

    /** Traverse the lists in oreder to find and delete a certain appointment, also free up a parking space if the appointment required one */
    public boolean deleteAppointment(String email, boolean updateFile){
        for (int i = 0; i < nrOfStations; i++) {
            _List.Node toDelete = stations[i].cancelAppointment(email,updateFile);
            if(toDelete != null)
            {
                if(toDelete.parkingSpace)
                    parking.removeCar();
                return true;
            }

        }
        return false;
    }

    public String printAppointmentsList(int station, int mode){
        if(station >= nrOfStations)
            return("Invalid station number");
        else
            return(stations[station].printList(mode));
    }

    /** When we compute the intervals of time a user can make an appointment at, the strategy that I thought of requires the start and end time of each
     * exiting appointment to be sorted in ascending order */
    private void sortIntervals(int[][] intervals, int size){
        boolean sorted = false;
        while(!sorted){
            sorted = true;
            for (int i = 0; i < size-1; i++) {
                if(intervals[0][i] > intervals[0][i+1]){
                    sorted = false;

                    int aux = intervals[0][i];
                    intervals[0][i] = intervals[0][i+1];
                    intervals[0][i+1] = aux;

                    aux = intervals[1][i];
                    intervals[1][i] = intervals[1][i+1];
                    intervals[1][i+1] = aux;
                }
            }
        }
    }

    /** Read the text above showAvaillableHours() first !
     *  Finds the ending time for a free interval, the latest one possible. Merges intervals on the logic described below and returns the end time of a merged interval. */
    private int findMax(int[][] intervals, int index, int max, int size){
        int newMax = 0;
        int end = index;
        while(intervals[0][end] <= max){
            end++;
            if(end == size){
                break;
            }
        }

        for(int i = index; i < end; i++)
            if(intervals[1][i] > newMax)
                newMax = intervals[1][i];

        return newMax;
    }

    /** Here the intervals of time a costumer can make an appointment is computed.
     * First we get all the intervals available from all of the stations.
     * Now we need to merge those intervals, we can t show the user small intervals taken from each station because that is what we have right now.
     * So put all the intervals from all of the stations in a 2 dim. array => intervals array where intervals[0][...] and intervals[1][...] represent the start and end time of an interval available
     * The intervals are merged based on this logic: if we are free to make an appointment between 10:00 and 10:20 and there is another interval that starts between 10:00 and 10:20 and finishes at time x => than we are free from 10:00 to x too.
     * Example: you can make an appointment between 10:00 and 10:20. You can make an appointment between 10:15 and 10:35. This means you can make an appointment any time from 10:00 and 10:35.
     * On this logic all the intervals are merged and shown to the user, so he/she can see when they can make an appointment
     * PSEUDO-COD :
     *
     * for each station
     *      size += sizeOfStation
     *
     * for each station
     *      for each interval
     *          intervals[index] = interval[j] of station i
     *
     *  sort intervals
     *
     *  index = 0
     *  while ( index < size )
     *      start = intervals[0][index]
     *      end = intervals[1][index]
     *      while( we can extend the interval )
     *          if( an interval starts between start and end )
     *              end = end of that interval
     *
     *      while( intervals[0][index] < end )
     *          intervals++
     *  */
    public String showAvaillableHours(int timeToWash){
        //get size of the intervals from every list
        int size = 0;
        for (int i = 0; i < nrOfStations; i++) {
            size += stations[i].getNrOfIntervalsAvailable(timeToWash);
        }

        if(size == 0){
            return "The car wash is full today";
        }

        int[][] intervals = new int[2][size];
        int index = 0;

        //put all the intervals in one 2 dim. array
        for (int i = 0; i < nrOfStations; i++) {
            int[][] thisStationIntervals = stations[i].getAvailableHours(timeToWash);

            for (int j = 0; j < stations[i].getNrOfIntervalsAvailable(timeToWash); j++) {
                intervals[0][index] = thisStationIntervals[0][j];
                intervals[1][index] = thisStationIntervals[1][j];
                index++;
            }
        }

        sortIntervals(intervals,size);

        index = 0;
        int max;
        String rez = "You can make an appointment between ";

        //merge the intervals
        while(index < size){
            String s = stations[0].printMinutesAsHours(intervals[0][index]);
            rez = rez.concat(s);
            rez = rez.concat("->");
            max = intervals[1][index];

            while (max < findMax(intervals,index,max,size)){
                max = findMax(intervals,index,max,size);
            }

            s = stations[0].printMinutesAsHours(max - timeToWash);
            rez = rez.concat(s);
            rez = rez.concat("   ");

            while(intervals[0][index] < max){
                index++;
                if(index == size)
                    break;
            }
        }
        return rez;
    }
}
