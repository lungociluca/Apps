package org.example.model;

public class DbObject {
    private int ID;

    public DbObject(int ID) {
        this.ID = ID;
    }

    public DbObject() {

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
