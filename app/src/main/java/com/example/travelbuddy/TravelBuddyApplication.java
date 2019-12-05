package com.example.travelbuddy;

import android.app.Application;

import com.example.travelbuddy.Objects.User;

public class TravelBuddyApplication extends Application {

    private User curUser;

    public User getCurUser() {
        return curUser;
    }

    public void setCurUser(User user) {
        this.curUser = user;
    }
}