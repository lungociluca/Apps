package BusinessLogic;
import java.util.Formatter;

public class WriteFile {
    private Formatter toInsert;

    public boolean openFileW() {
        try {
            this.toInsert = new Formatter("text");
            return true;
        } catch (Exception e) {
            System.out.println("File not found");
            return false;
        }
    }

    public void closeFileW() {
        this.toInsert.close();
    }

    public void addRecord(String s) {
        this.toInsert.format("%s", s);
    }


}