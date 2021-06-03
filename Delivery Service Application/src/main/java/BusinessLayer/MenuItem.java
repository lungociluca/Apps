package BusinessLayer;

import java.io.Serializable;

public abstract class MenuItem implements Serializable {
    private int calories, protein, fat, sodium, price;
    private float rating;
    private String name;

    public MenuItem(float rating, int calories, int protein, int fat, int sodium, String name) {
        this.rating = rating;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.sodium = sodium;
        this.price = 0;
        this.name = name;
    }

    public abstract int computePrice();
    public abstract String getItemsFromMenu();

    public String toString() {
        if(this.getClass().getSimpleName().equals("CompositeProduct")) {
            String s = name + " (" + getItemsFromMenu() + ") " + "\nRating: " + rating + "\tCalories: " + calories + "\tProtein: " + protein+ "\tFat: " + fat + "\tSodium: " + sodium + "\tPrice: " +price + "\n";
            s = s.replaceAll(", \\)", ")");
            return s;
        }
        return name + "\nRating: " + rating + "\tCalories: " + calories + "\tProtein: " + protein+ "\tFat: " + fat + "\tSodium: " + sodium + "\tPrice: " +price + "\n";
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
