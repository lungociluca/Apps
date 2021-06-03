package DataLayer;

import java.util.Formatter;
import java.util.List;

public class WriteReport {
    private Formatter toInsert;

    public boolean openFileW() {
        try {
            this.toInsert = new Formatter("report.txt");
            return true;
        } catch (Exception var2) {
            System.out.println("File not found");
            return false;
        }
    }

    public void addRecord( List<String> list) {
        String s = "";
        for(String string : list)
            s += string;
        this.toInsert.format("%s", s);
    }

    public void closeFileW() {
        this.toInsert.close();
    }
}
