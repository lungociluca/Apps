package Classes;

import java.io.File;
import java.util.Scanner;


/** This class updates a text file. "inp" text file saves all the appointments as commands like add and delete
 *  When the application is opened, all the commands from the file are executed */
public class ReadFile {
    private Scanner file;

    public void openFile(){
        try{
            file = new Scanner(new File("inp"));
        }
        catch(Exception e){
            System.out.println("File does not exist, inp.text");
        }
    }

    public void readFile(Car appointments){
        while(file.hasNext()){
            String command = file.next();

            if(command.equals("/")){
                command = file.next();
                while(!command.equals("/")){
                    command = file.next();
                }
                command = file.next();
            }

            if(command.equals("add")){
                int startHour = file.nextInt();
                int startMinute = file.nextInt();
                int timeToWash = file.nextInt();
                String p = file.next();
                boolean needsParking = false;
                if(p.equals("true"))
                    needsParking = true;
                String email = file.next();
                appointments.addAppointment(startHour,startMinute,timeToWash,needsParking,email,false);

            }
            else if(command.equals("delete")){
                String email = file.next();
                appointments.deleteAppointment(email,false);
            }
            else if(command.equals("print")){
                int station = file.nextInt();
                //appointments.printAppointmentsList(station);
            }
            else if(command.equals("?")){
                int timeToWash = file.nextInt();
                appointments.showAvaillableHours(timeToWash);
            }
            else if(command.equals("parking")){
                System.out.println(appointments.parking.getFreeSpots()+" parking spots free");
            }
            else
                System.out.println("invalid command "+command);
        }
    }

    public void closeFile(){
        file.close();
    }

}

