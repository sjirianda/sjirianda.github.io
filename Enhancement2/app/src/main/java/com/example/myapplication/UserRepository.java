package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class UserRepository {
    private final SharedPreferences prefs;

    public UserRepository(Context context) {
        prefs = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
    }

    public String getUsername() {
        return prefs.getString("username", null);
    }

    public String getPassword() {
        return prefs.getString("password", null);
    }

    public void saveUser(String username, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}
