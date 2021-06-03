package DataLayer;

import BusinessLayer.BaseProduct;
import BusinessLayer.ImportInitialSet;
import BusinessLayer.MenuItem;
import BusinessLayer.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemsAndOrders implements Serializable {
    private List<MenuItem> menuItems = new ArrayList<>();
    HashMap<Order, List<MenuItem>> map = new HashMap<>();
    public boolean initialSetImported = false;
    private int size = 1;

    public void importInitialSet() {
        if(initialSetImported)
            return;
        initialSetImported = true;
        List<BaseProduct> list = ImportInitialSet.read();
        menuItems.addAll(list);
    }

    public List<MenuItem> getMenuItems() {
        if(!initialSetImported)
            importInitialSet();
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public HashMap<Order, List<MenuItem>> getMap() {
        return map;
    }

    public void setMap(HashMap<Order, List<MenuItem>> map) {
        this.map = map;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
