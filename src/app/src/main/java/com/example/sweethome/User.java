package com.example.sweethome;

public class User {
    private String username;
    private String email;

    /* no-arg constructor for this class */
    public User(){
        //no argument constructor is necessary for using toObject in firestore
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}
