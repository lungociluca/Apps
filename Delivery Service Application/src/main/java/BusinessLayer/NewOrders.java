package BusinessLayer;

import DataLayer.SaveAndRecover;
import DataLayer.SaveNewOrders;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class NewOrders implements Observer {
    private List<Order> orders;
    private SaveNewOrders saveNewOrders;

    public NewOrders() {
        saveNewOrders = SaveAndRecover.recoverOrders();
        orders = saveNewOrders.getOrders();
    }

    public void saveOrdersList() {
        SaveAndRecover.save(null, null, saveNewOrders);
    }

    public String getOrdersString() {
        String s = "";
        for(Order order : orders)
            s += order.orderToString();
        return s;
    }

    public void remove(int id) {
        int index = 0;
        for(Order order : orders) {
            if(order.getOrderId() == id)
                break;
            index++;
        }
        orders.remove(index);
        saveNewOrders.setOrders(orders);
        SaveAndRecover.save(null, null, saveNewOrders);
    }

    @Override
    public void update(Observable o, Object arg) {
        Order order = (Order) arg;
        System.out.println("????  "+ order.orderToString());
        orders.add((Order) arg);
        for(Order or : orders)
            System.out.println("Order=> "+or.getClientId() + " " + or.getPrice());
        saveNewOrders.setOrders(orders);
        SaveAndRecover.save(null, null, saveNewOrders);
    }
}
