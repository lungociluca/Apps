package BusinessLayer;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private int orderId;
    private String clientId;
    private Time orderDate;
    private int price;

    public class Time implements Serializable {
        public int hour;
        public int minute;
        public int day;
        public int month;
        public int year;

        public Time(int hour, int minute, int day, int month, int year) {
            this.hour = hour;
            this.minute = minute;
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public String toString() {return hour+":"+minute;}
    }

    @Override
    public int hashCode() {
        return this.orderId;
    }

    @Override
    public boolean equals(Object obj) {
        Order order = (Order) obj;
        if(order.orderId == this.orderId)
            return true;
        else
            return false;
    }

    public Order(int orderId, String clientId) {
        this.orderId = orderId;
        this.clientId = clientId;
        Date date = new Date();
        this.orderDate = new Time(date.getHours(), date.getMinutes(), date.getDate(), date.getMonth() + 1, date.getYear() + 1900);
        this.price = 0;
    }

    public void increaseOrderPrice(int value) {
        price += value;
    }

    public int getPrice() {
        return price;
    }

    public int getOrderId(){return orderId;}

    public Time getOrderDate() {
        return orderDate;
    }

    public String getClientId() {
        return clientId;
    }

    public String orderToString() {
        return "Order with id = " + orderId + " was performed at " + orderDate.toString() + ". Client name: " + clientId + ". Price: "+price+".\n";
    }
}
