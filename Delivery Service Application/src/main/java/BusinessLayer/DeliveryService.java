package BusinessLayer;

import DataLayer.ItemsAndOrders;
import DataLayer.SaveAndRecover;
import DataLayer.WriteFile;
import org.example.*;

import java.util.*;

public class DeliveryService extends Observable implements IDeliveryServiceProcessing {
    private List<MenuItem> menuItems;
    HashMap<Order, List<MenuItem>> map;
    private int size = 0;
    private NewOrders newOrders = new NewOrders();
    ItemsAndOrders itemsAndOrders;

    public DeliveryService() {
        itemsAndOrders = SaveAndRecover.recoverItemsAndOrders();
        menuItems = itemsAndOrders.getMenuItems();
        map = itemsAndOrders.getMap();
        size = itemsAndOrders.getSize();
        addAnObserver();
    }

    protected Boolean isWellFormed(){
        if(map == null || menuItems == null) {
            return false;
        }
        return true;
    }

    public DeliveryService(EmployeePageController employee) {
        this();
        List<Order> listOfOrders = new ArrayList<>();
        map.entrySet().forEach(entry -> {listOfOrders.add(entry.getKey());});
        employee.setOrders(listOfOrders);
    }

    public void saveDeliveryClass() {
        itemsAndOrders.setMenuItems(menuItems);
        itemsAndOrders.setMap(map);
        itemsAndOrders.setSize(size);
        SaveAndRecover.save(null, itemsAndOrders, null);
    }

    public void addNewProduct(BaseProduct baseProduct) {
        assert baseProduct != null;
        assert isWellFormed();
        int initSize = getListSize();
        menuItems.add(baseProduct);
        assert initSize + 1 == getListSize();
    }

    public void addNewCompositeProduct(CompositeProduct compositeProduct) {
        assert compositeProduct != null;
        assert isWellFormed();
        int initSize = getListSize();
        menuItems.add(compositeProduct);
        assert initSize + 1 == getListSize();
    }

    public MenuItem modifyProduct(String name) {
        return menuItems.stream().filter(item -> name.trim().contains(item.getName().trim())).findAny().orElse(null);
    }

    private static boolean meetsRequirements(MenuItem item, String name, float ratio, int calories, int protein, int fat, int sodium, int price) {
        if(!name.equals("\n")) {
            //if(!name.contains(item.getName()))
                //return false;
            if(!item.getName().contains(name))
                return false;
        }
        if(ratio != -1) {
            if(ratio <= item.getRating())
                return false;
        }
        if(calories != -1) {
            if (calories != item.getCalories())
                return false;
        }
        if(protein != -1) {
            if(protein != item.getProtein())
                return false;
        }
        if(fat != -1) {
            if(fat != item.getFat())
                return false;
        }
        if(sodium != -1){
            if(sodium != item.getSodium())
                return false;
        }
        if(price != -1) {
            if(price <= item.computePrice())
                return false;
        }
        return true;
    }

    public List<MenuItem> searchProduct(String name, float ratio, int calories, int protein, int fat, int sodium, int price) {
        List<MenuItem> toReturn = new ArrayList<>();
        menuItems.stream().forEach(menuItem -> {
            if(meetsRequirements(menuItem, name, ratio, calories, protein, fat, sodium, price))
                toReturn.add(menuItem);
            });
        return toReturn;
    }

    public void delete(String name) {
        int index = 0;
        for(MenuItem item : menuItems) {
            if(item.getName().equals(name))
                break;
            index++;
        }
        menuItems.remove(index);
    }

    public void addAnObserver() {
        addObserver(newOrders);
    }

    //public void printOrders() {
       // System.out.println(newOrders.getOrders());
    //}

    private int getListSize() {
        return menuItems.size();
    }

    private boolean containsOrder(Order order) {
        final int[] found = {0};
        map.entrySet().forEach(entry -> {
            if(entry.getKey().getClientId().equals(order.getClientId()))
                found[0]++;
        });
        if(found[0] == 0)
            return false;
        return true;
    }

    public void addOrder(String clientName, List<String> menuItemsInOrder) {
        assert menuItemsInOrder != null;
        assert isWellFormed();
        Order order = new Order(size++, clientName);
        List<MenuItem> listOfOrders = new ArrayList<>();
        try {
            for(String name : menuItemsInOrder) {
                MenuItem toAdd = searchProduct(name, -1, -1, -1, -1, -1 ,-1).get(0);
                listOfOrders.add(toAdd);
                order.increaseOrderPrice(toAdd.computePrice());
            }
            map.put(order, listOfOrders);

        }catch (Exception e){
            System.out.println("DeliveryService: Error while adding an order.");
        }
        WriteFile file = new WriteFile();
        file.openFileW();
        file.addRecord(order, listOfOrders);
        file.closeFileW();

        noticeEmployee(order);
    }

    private void noticeEmployee(Order order) {
        setChanged();
        notifyObservers(order);
    }

    public void printMap() {
        System.out.println("\nMap:\n");
        map.entrySet().forEach(entry -> {
            System.out.print(entry.getKey().getClientId() + " -> ");
            for(MenuItem item : entry.getValue()) {
                System.out.print(item.getName() +  ", ");
            }
            System.out.println();
        });
    }

    public void generateReport(int timeLowerLimit, int timeHigherLimit, int timesOrdered, int nrTimesOrdered, int orderValue, int specificDay, int specificMonth) {
        assert isWellFormed();
        assert (timeLowerLimit / 60) >= 0 && (timeHigherLimit / 60 < 24);
        assert (timesOrdered > 0 && nrTimesOrdered > 0 && orderValue > 0 && (specificDay / 31) <= 1 && (specificMonth / 12) <= 1);
        GenerateReport.generateReport(map, timeLowerLimit, timeHigherLimit, timesOrdered, nrTimesOrdered, orderValue,specificDay,specificMonth);
    }
}
