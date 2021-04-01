package com.ablancomziar.billsmanager;

import android.content.Context;

import java.io.Serializable;

public class Address implements Serializable {

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

    // todo use string.xml
    public String getFormattedAddress(Context ctx) {
        return "Address{" +
                "num=" + num +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
