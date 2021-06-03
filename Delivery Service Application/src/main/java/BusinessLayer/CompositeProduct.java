package BusinessLayer;

import BusinessLayer.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CompositeProduct extends MenuItem{
    private final List<MenuItem> productsInMenu = new ArrayList<>();
    private int price;

    public CompositeProduct(float rating, int calories, int protein, int fat, int sodium, String name) {
        super(rating, calories, protein, fat, sodium, name);
        this.price = 0;
    }

    public int getPrice() {
        return this.price;
    }

    public void addItem(MenuItem item) {
        productsInMenu.add(item);
    }

    public String getItemsFromMenu() {
        String s = "";
        for(MenuItem item : productsInMenu)
            s += item.getName() + ", ";
        return s;
    }

    @Override
    public int computePrice() {
        float rating = 0;
        for(MenuItem item : productsInMenu) {
            price += item.getPrice();
            this.setCalories(this.getCalories() + item.getCalories());
            this.setFat(this.getFat() + item.getFat());
            this.setProtein(this.getProtein() + item.getProtein());
            this.setSodium(this.getSodium() + item.getSodium());
            rating += item.getPrice();
        }
        this.setRating(rating / productsInMenu.size());
        return 0;
    }
}
