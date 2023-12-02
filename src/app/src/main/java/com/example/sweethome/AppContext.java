package com.example.sweethome;
/**
 * The AppContext class manages global variables that can be accessed in any activity of the app.
 *
 * This class currently only manages the global variable "username", which is the username of the
 * currently logged in user.
 *
 * Dec 1, 2023
 *
 */

import android.app.Application;

public class AppContext extends Application {
    public String username;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
