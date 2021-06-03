package DataLayer;

import BusinessLayer.Order;

import java.io.Serializable;
import java.util.List;

public class SaveNewOrders implements Serializable {
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        for(Order o : orders)
            System.out.println("\n-> " + o.getClientId() + " " + o.getPrice());
    }
}
