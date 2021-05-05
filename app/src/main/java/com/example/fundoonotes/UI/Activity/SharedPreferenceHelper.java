package com.example.fundoonotes.UI.Activity;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private Context context;
    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String IS_LOGGED_IN = "loggedIn";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferenceHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setIsLoggedIn(boolean value) {
        editor  = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN,value);
        editor.apply();
    }

    public boolean getLoggedIN(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

}
