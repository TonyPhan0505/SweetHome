package com.example.sweethome;

import android.app.Application;

public class AppContext extends Application {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
