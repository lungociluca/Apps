package org.example.model;

public class Client extends DbObject {
    private String name;
    private String address;
    private String phone;

    public Client(int ID, String name, String address, String phone) {
        super(ID);
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Client() {

    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString() {
        return name+" "+address+" "+phone+"\n";
    }
}
