package org.example.model;

public class Order extends DbObject {
    private int client_ID;
    private int product_ID;
    private int nr_products;

    public Order(int ID, int client_ID, int product_ID, int nr_products) {
        super(ID);
        this.client_ID = client_ID;
        this.product_ID = product_ID;
        this.nr_products = nr_products;
    }

    public Order() {

    }

    public int getClient_ID() {
        return client_ID;
    }

    public void setClient_ID(int client_ID) {
        this.client_ID = client_ID;
    }

    public int getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(int product_ID) {
        this.product_ID = product_ID;
    }

    public int getNr_products() {
        return nr_products;
    }

    public void setNr_products(int nr_products) {
        this.nr_products = nr_products;
    }

    public String toString() {
        return getID() + " " + client_ID + " " + product_ID + " " + nr_products;
    }
}
