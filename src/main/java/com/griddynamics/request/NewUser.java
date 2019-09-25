package com.griddynamics.request;

public class NewUser {
    public String username;
    public String email;
    public String password;

    public NewUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
