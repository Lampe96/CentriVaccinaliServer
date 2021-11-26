package org.project.models;

import java.io.Serializable;

public class Hub implements Serializable {

    private String nameHub;
    private String password;
    private Address address;
    private String type;
    private int image;

    public Hub() {

    }

    public Hub(String nameHub, String password, Address address, String type, int image) {
        this.nameHub = nameHub;
        this.password = password;
        this.address = address;
        this.type = type;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNameHub() {
        return nameHub;
    }

    public void setNameHub(String nameHub) {
        this.nameHub = nameHub;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Hub{" +
                "nameHub='" + nameHub + '\'' +
                ", password='" + password + '\'' +
                ", address=" + address +
                ", type='" + type + '\'' +
                ", image=" + image +
                '}';
    }
}
