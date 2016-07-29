package com.noveogroup.teamzolotov.iwashere.model;

public class Profile {
    private final String email;
    private final String username;

    public Profile(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
