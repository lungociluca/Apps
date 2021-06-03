package DataLayer;

import BusinessLayer.MenuItem;
import BusinessLayer.Order;

import java.io.Serializable;
import java.util.Formatter;
import java.util.List;

public class WriteFile implements Serializable {
    private Formatter toInsert;

    public boolean openFileW() {
        try {
            this.toInsert = new Formatter("bill");
            return true;
        } catch (Exception var2) {
            System.out.println("File not found");
            return false;
        }
    }

    public void addRecord(Order order, List<MenuItem> list) {
        String s = "Items ordered from the menu:\n\n";
        for (MenuItem item : list) {
            s += item.toString() + "\n";
        }
        s += "Order was performed at " +order.getOrderDate().toString() + " by " + order.getClientId() +".\nTOTAL PRICE " +order.getPrice();
        this.toInsert.format("%s", s);
    }

    public void closeFileW() {
        this.toInsert.close();
    }
}
