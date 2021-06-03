package org.example.model;

public class Product extends DbObject {
    private String name;
    private int price;
    private int stock;

    public Product(int ID, String name, int price, int stock) {
        super(ID);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product() {
    }

    public void decrementStock(int value) {
        stock -= value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String toString() {
        return name + " " + price + " " + stock;
    }
}
