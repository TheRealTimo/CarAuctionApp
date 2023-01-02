package com.example.carauctionapp.classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.carauctionapp.utilities.Constants;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SESSION_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String userApiKey) {
        editor.putString(Constants.USER_API_KEY, userApiKey).commit();
        editor.putBoolean(Constants.USER_IS_LOGGED_IN_KEY, true).commit();
    }

    public String getCurrentUserApiKey() {
        return sharedPreferences.getString(Constants.USER_API_KEY, "");
    }

    public boolean getIsUserLoggedIn() {
        return sharedPreferences.getBoolean(Constants.USER_IS_LOGGED_IN_KEY, false);
    }
}
