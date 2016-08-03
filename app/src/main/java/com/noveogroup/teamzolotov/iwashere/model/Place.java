package com.noveogroup.teamzolotov.iwashere.model;

/**
 * Created by dserov on 03/08/16.
 */
public class Place {

    private Address address;

    public static class Address {
        private String state;
    }

    public String getState() {
        return address.state;
    }
}
