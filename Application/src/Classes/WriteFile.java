package Classes;

import java.util.Formatter;

public class WriteFile {
    private Formatter toInsert;

    public boolean openFileW(){
        try{
            toInsert = new Formatter("Users.txt");
        }
        catch (Exception e){
            System.out.println("File not found");
            return false;
        }
        return true;
    }

    public void addRecord(String email, String password){
        toInsert.format("%s%s",email,password);
        System.out.println("adding "+email+" "+password);
    }

    public void closeFileW(){
        toInsert.close();
    }
}
