package org.project.models;

import java.io.Serializable;

public class Address implements Serializable {

    private String qualificator;
    private String address;
    private String number;
    private String city;
    private String cap;
    private String province;

    public Address(String qualificator, String address, String number, String city, String cap, String province) {
        this.qualificator = qualificator;
        this.address = address;
        this.number = number;
        this.city = city;
        this.cap = cap;
        this.province = province;
    }

    public String getQualificator() {
        return qualificator;
    }

    public void setQualificator(String qualificator) {
        this.qualificator = qualificator;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "Address{" +
                "qualificator='" + qualificator + '\'' +
                ", address='" + address + '\'' +
                ", number='" + number + '\'' +
                ", city='" + city + '\'' +
                ", cap='" + cap + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
