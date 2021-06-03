package org.example.businessLogic;

import java.util.Formatter;

public class WriteFile {
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

    public void addRecord(String clientName,String address, String productName, int unitsBought, int totalPrice) {
        this.toInsert.format("Client: %s\nAddress: %s\nProduct ordered: %s x %d units\nTotal price: %d", clientName, address, productName,  unitsBought, totalPrice);
    }

    public void closeFileW() {
        this.toInsert.close();
    }
}
