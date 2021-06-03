package DataLayer;

import java.io.*;

public class SaveAndRecover {
    public static void save(LogIn logIn, ItemsAndOrders itemsAndOrders, SaveNewOrders saveNewOrders) {
        if(logIn != null) {
            try {
                FileOutputStream file = new FileOutputStream("user.txt");

                ObjectOutputStream out = new ObjectOutputStream(file);

                out.writeObject(logIn);
                out.close();
                file.close();
            } catch (Exception e) {
                System.out.println("Can't save users names and passwords");
            }
        }

        if(itemsAndOrders != null) {
            try {
                FileOutputStream file = new FileOutputStream("delivery.txt");
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(itemsAndOrders);
                out.close();
                file.close();
            } catch (Exception e) {
                System.out.println("Can't save delivery service");
            }
        }


        if(saveNewOrders != null) {
            try {
                System.out.println("saving....");
                FileOutputStream file = new FileOutputStream("orders.txt");

                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(saveNewOrders);
                out.close();
                file.close();
            } catch (Exception e) {
                System.out.println("Can't save orders");
            }
        }
    }

    public static LogIn recoverUsers() {
        LogIn logIn = new LogIn();
        try {
            FileInputStream file = new FileInputStream("user.txt");
            ObjectInputStream in = new ObjectInputStream(file);
            logIn = (LogIn)in.readObject();
            in.close();
            file.close();
        }
        catch (Exception e) {
            System.out.println("Can't recover users");
        }
        return logIn;
    }

    public static ItemsAndOrders recoverItemsAndOrders() {
        ItemsAndOrders itemsAndOrders = new ItemsAndOrders();
        try {
            FileInputStream file = new FileInputStream("delivery.txt");
            ObjectInputStream in = new ObjectInputStream(file);
            itemsAndOrders = (ItemsAndOrders) in.readObject();
            in.close();
            file.close();
        }
        catch (Exception e) {
            System.out.println("Can't recover delivery service");
        }
        return itemsAndOrders;
    }

    public static SaveNewOrders recoverOrders() {
        SaveNewOrders saveNewOrders = new SaveNewOrders();
        try {
            FileInputStream file = new FileInputStream("orders.txt");
            ObjectInputStream in = new ObjectInputStream(file);
            saveNewOrders = (SaveNewOrders) in.readObject();
            in.close();
            file.close();
        }
        catch (Exception e) {
            System.out.println("Can't recover orders");
        }
        return saveNewOrders;
    }
}
