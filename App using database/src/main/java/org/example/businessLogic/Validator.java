package org.example.businessLogic;

import org.example.model.Client;
import org.example.model.Order;
import org.example.model.Product;

public class Validator {

    /**
     * checks if one of the characters written by the user isn't a letter or space
     * @param s string entered in the GUI
     * @return true if the string doesn't respect the format, false otherwise
     */
    private static boolean checkForBadCharacters(String s) {
        s = s.toLowerCase();
        char[] characterString = s.toCharArray();
        for(char c : characterString)
            if((c < 97 || c > 122) && c != 32)
                return true;

        return false;
    }

    /**
     * checks the lengths of the objects fields
     * @param product the object to add as record in the data base
     * @return the result of the checking in order to display on the GUI
     */
    public static String check(Product product) {
        if(checkForBadCharacters(product.getName()))
            return "Some characters used for names are not allowed";
        if(product.getName().length() > 45)
            return "Product name is too long";
        if(product.getStock() < 0 || product.getPrice() < 0) {
            return "Negative numbers are not allowed";
        }

        return "Ok";
    }

    public static String check(Client client) {
        if(checkForBadCharacters(client.getName()) || checkForBadCharacters(client.getAddress().split(" ")[0]))
            return "Some characters used for names are not allowed";
        if(client.getPhone().length() > 10 || client.getAddress().length() > 45 || client.getName().length() > 45)
            return "Maximum number of characters for fields is 45, for phone 10";
        char[] charArray = client.getPhone().toCharArray();
        for(char c : charArray)
            if(c < '0' || c > '9')
                return "Only numbers allowed for phone number";
        return "Ok";
    }

    public static String check(Order order) {
        if(order.getClient_ID() < 0 || order.getNr_products() < 0 || order.getProduct_ID() < 0)
            return "No negative numbers allowed";

        return "Ok";
    }
}
