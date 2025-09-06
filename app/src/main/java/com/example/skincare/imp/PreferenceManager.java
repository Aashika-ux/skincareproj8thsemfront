package com.example.skincare.imp;

import android.content.Context;
import android.content.SharedPreferences;
public class PreferenceManager {

    private static final String PREF_NAME = "user_pref";
    private static final String KEY_TOKEN = "token";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Save user info
    public void saveUser(String username, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("email", email);
        editor.apply();
    }

    // Getters
    public String getUsername() {
        return sharedPreferences.getString("username", "");
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    public PreferenceManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveToken(String token){
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken(){
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void clearToken(){
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
    // Add this method
    public void clearData() {
        editor.clear();
        editor.apply();
    }
    public void clearUser() {
        editor.remove("username");
        editor.remove("email");
        editor.apply();
    }
}
