package org.project.models;

import java.io.Serializable;

public class Hub implements Serializable {

    private String nameHub;
    private String password;
    private String address;
    private String type;

    public Hub(String nameHub, String password, String address, String type) {
        this.nameHub = nameHub;
        this.password = password;
        this.address = address;
        this.type = type;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
