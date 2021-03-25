package Classes;

import sample.Controller;

import java.io.File;
import java.io.FileWriter;

/** Stores a doubly linked list and performs operations on it */
public class _List {

    public class Node{ /** Node in a linked list representing an appointment */
        int begin, end; // integers storing the starting and ending time of an appointment as minutes
        Node next; // pointers to the appointment before and after it
        Node back;
        boolean parkingSpace;
        String email;

        public Node(int begin, int end, Node next, Node back, String email){
            this.email = email;
            this.begin = begin;
            this.end = end;
            this.next = next;
            this.back  =back;
            this.parkingSpace = false;
        }
    }

    private final Node head; // pointer to the head of the list
    private int size; // nr of nodes in the list
    private int nrOfIntervalsAvailable; // nr of intervals of time when an appointment can be made

    /** The first and last node in the list will contain the opening and closing time for the car wash
     *  If the car wash opens at 8:00 and closes at 14:00, these values will be converted into minutes and stored in the first and last node
     *  The constructor will also check if those times are valid and select a default value if not */
    public _List(int openHour, int closeHour){
        if(openHour < 0 || openHour > 23 || closeHour < 0 || closeHour > 23){
            System.out.println("Open and close time are not valid, default values will be elected: 9:00 - 16:00");
            openHour = 9;
            closeHour = 16;
        }
        head = new Node(0,toMinutes(openHour,0),null,null," ");
        Node temp = new Node(toMinutes(closeHour,0),0,null,head," ");
        head.next = temp;
        this.size = 0;
    }

    public int getSize(){
        return size;
    }

    /** Traverses the linked list and, taking into consideration the time to wash the car, calculates how many intervals of time for an appointment exist
     *  For n appointments there are n+1 intervals of time. The nr of intervals available will be calculated decrementing the total nr of intervals.
     *  Let's take an example. In the list we have an appointment finishing at 11:30 and the one after it starts at 11:32. If the time to wash the costumers car is grater than 2, we can't consider this interval available */
    public int getNrOfIntervalsAvailable(int timeToWash){
        nrOfIntervalsAvailable = size + 1;
        Node temp = head;

        while(temp.next != null){
            if(temp.end + timeToWash > temp.next.begin){
                nrOfIntervalsAvailable--;
            }
            temp = temp.next;
        }
        return nrOfIntervalsAvailable;
    }

    /** Since its easier to perform comparisons and additions on a time represented as minutes, we will convert time represented like hour:minute
     *  Example: 9:30 converts into 570 */
    static int toMinutes(int hours, int minutes){
        return (60*hours + minutes);
    }

    /** Does the opposite of the function above, converts time represented as minutes in time represented as hour:minutes, as string (for printing purpose) */
    public String printMinutesAsHours(int minutes){
        String toReturn;
        int hour = minutes / 60;
        int min = minutes % 60;
        if(hour == 0)
            toReturn = "00:";
        else{
            toReturn = Integer.toString(hour);
            toReturn = toReturn.concat(":");
        }

        if(min == 0)
            toReturn = toReturn.concat("00");
        else{
            toReturn = toReturn.concat(Integer.toString(min));
        }
        return  toReturn;
    }

    /** Insertion in a linked list, returns true if insertion could be done.
     *  To ave the changes made to the lists after the app is closed, commands will be stored as text in a file. So if an insertion is done we need to update the file
     *  The updateFile param is false when the appointment was inserted before and we need to reconstruct the list. The commands for adding an appointment
     *  was read from the file and we need to add that node in the list BUT WE DON'T have to update the file. We need to update the file after we added all appointments make by the
     *  previous users and the current users wishes to make one. We save this change in a text file so we can reconstruct the list when the next user wants to make an appointment*/
    public boolean insert(int startHour, int startMinute, int timeTowash, boolean needsParcking, String email, boolean updateFile){

        // see if time is valid
        if(startHour < 0 || startMinute < 0 || timeTowash < 0){
            return false;
        }

        int start = toMinutes(startHour,startMinute);
        int end = start + timeTowash;

        Node temp = head; // traverse the list with this variable

        if(start < head.end){ // if time of new appointment is earlier than the time the car wash opens => return false
            return false;
        }

        //this block will generate an error if the time inserted is too late according to schedule, if appointment time later than close time and temp is a null pointer
        try {
            while (start > temp.begin) {
                temp = temp.next;
            }
        } catch (Exception e) {
            return false;
        }

        if (end > temp.begin || temp.back.end  > start) // if the appointment to insert finishes after another one starts OR if the appointment to insert starts before another one has ended => return false
            return false;

        else{ // create the node to insert
            Node toInsert = new Node(start,end,temp,temp.back,email);


            // update the command file if needed
           if(updateFile){
               try{
                   File file = new File("inp");
                   FileWriter fr = new FileWriter(file, true);

                   String needParkingString = "false";
                   if(needsParcking)
                       needParkingString = "true";

                   fr.write("\nadd "+startHour+" "+startMinute+" "+timeTowash+" "+needParkingString+" "+email);
                   fr.close();
               }catch (Exception e){
                   System.out.println("file not found");
               }
           }

           // insert the node in the list and return true
            toInsert.parkingSpace = needsParcking;

            this.size++;
            temp.back = toInsert;
            toInsert.back.next = toInsert;
            return true;
        }
    }

    /** Return a string with all the appointments*/
    public String printList(int mode){
        Node temp = head.next;
        String toReturn=" ";

        while(temp.next != null){
            if(mode == 1)
                toReturn = toReturn.concat(printMinutesAsHours(temp.begin)+" - "+printMinutesAsHours(temp.end)/*+" "+temp.email*/+"     ");
            else
                toReturn = toReturn.concat(printMinutesAsHours(temp.begin)+" - "+printMinutesAsHours(temp.end)+" "+temp.email+"\n");
            temp = temp.next;
        }
        toReturn = toReturn.concat("\n");
        return toReturn;
    }

    /** In order to compute the time when a user can make an appointment, first we need to store all the intervals when the current appointments take place.
     *  This method returns all the intervals as a 2 dim array, the intervals available for an appointment will be computed in the Appointment class */
    public int[][] getAvailableHours(int timeToWash){
        getNrOfIntervalsAvailable(timeToWash);
        int[][] intervals = new int[2][nrOfIntervalsAvailable];
        Node temp = head;
        int index = 0;

        while(temp.next != null){
            if(temp.end + timeToWash <= temp.next.begin){
                intervals[0][index] = temp.end;
                intervals[1][index] = temp.next.begin;
                index++;
            }
            temp = temp.next;
        }

        return intervals;
    }

    /** Delete an appointment from the list, also update the file with commands made on the list if this is a new command
     *  If we just reconstructing the list when the app is opened => we dont need to update the command file, we already have that command there */
    public Node cancelAppointment(String email, boolean updateFile){
        Node temp = head;
        while(!temp.email.equals(email)){
            temp = temp.next;
            if(temp == null){
                System.out.println("Such appointment does not exist");
                return null;
            }
        }

        temp.next.back = temp.back;
        temp.back.next = temp.next;
        this.size--;

        System.out.println("Appointment cancelled successfully");
        if(updateFile){
            try{
                File file = new File("inp");
                FileWriter fr = new FileWriter(file, true);
                fr.write("\ndelete "+" "+email);
                fr.close();
            }catch (Exception e){
                System.out.println("file not found");
            }
        }
        return temp;
    }
}
