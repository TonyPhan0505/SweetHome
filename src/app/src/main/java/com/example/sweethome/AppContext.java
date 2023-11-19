package com.example.sweethome;

import android.app.Application;

public class AppContext extends Application {
    private String username;

    @Override
    public void onCreate() {
        super.onCreate();
        username = "Boss553";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
