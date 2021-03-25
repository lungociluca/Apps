package Classes;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/** Uses the "users" text file as a data base with the emails and passwords, performs login in, add user operations. */
public class SignIn {
    private Scanner file;
    private String email;
    private String password;

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    /** If the email and password respect the requirements mentioned on the screen, if the email is was not
     * previously used for another appointment, the data will be added to the "Users" text file*/
    public String addUser(){
        openFile();
        boolean ok = readFile("Search email");
        closeFile();

        if(ok)
            return "Email already used";

        System.out.println(email+" does not exist");

        String valid = checkIfValidEmailAndPassword();

        if(valid.equals("Valid")){
            try{
                File file = new File("Users.txt");
                FileWriter fr = new FileWriter(file, true);
                fr.write("\n"+email+" "+password);
                fr.close();
            }catch (Exception e){
                System.out.println("file not found");
            }
        }
        return valid;
    }

    /** Checks if a user with this email already exists*/
    public boolean exists(){
        openFile();
        boolean ok = readFile("exists");
        if(ok)
            System.out.println("user found");
        else
            System.out.println("user NOT found");
        closeFile();
        return ok;
    }

    private String checkIfValidEmailAndPassword(){
        if(password.length() >= 25 || email.length() >= 25 )
            return "Length of email and password allowed is maximum 25";

        if(email.contains(" ") || password.contains(" "))
            return "Email or password should not contain spaces";
        if(email.contains("@gmail.") || email.contains("@yahoo.") || email.contains("@hotmail."))
            return "Valid";
        else
            return "Only emails of type @gmail, @yahoo, @hotmail are valid";
    }

    private void openFile(){
        try{
            file = new Scanner(new File("C:\\Users\\Luca\\IdeaProjects\\App\\Users.txt"));
        }
        catch(Exception e){
            System.out.println("File does not exist");
        }
    }

    /** Can check if an email already exists (if command == "Search email) or checks if a the email and the password are correct */
    private boolean readFile(String command){
        while(file.hasNext()){
            String email = file.next();
            String password = file.next();
            //System.out.println("---------- "+email+" "+password);

            if(this.email.equals(email)){
                if(command.equals("Search email"))
                    return true;
                if(this.password.equals(password))
                    return true;
            }
        }
        return false;
    }

    private void closeFile(){
        file.close();
    }
}