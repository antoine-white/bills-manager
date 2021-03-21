package com.ablancomziar.billsmanager;

public class Address {

    private int num;
    private String city;
    private String street;
    private String state;

    public Address(int num, String city, String street, String state) {
        this.num = num;
        this.city = city;
        this.street = street;
        this.state = state;
    }

    public int getNum() {
        return num;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getStreet() {
        return street;
    }
}
