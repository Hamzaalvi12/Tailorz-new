package com.example.tailorz.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    //This class is the setter and getter for user preferences
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    final static String SUBS_DATA = "userData";

    public Prefs(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SUBS_DATA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserName(String value) {
        editor.putString("userName", value);
        editor.apply();
    }
    public void setUserEmail(String value) {
        editor.putString("email", value);
        editor.apply();
    }
    public void setUserCategory(String value) {
        editor.putString("category", value);
        editor.apply();
    }
    public void setUserAddress(String value) {
        editor.putString("address", value);
        editor.apply();
    }
    public void setUserTelephone(String value) {
        editor.putString("telephone", value);
        editor.apply();
    }
    public void setUserWhatsapp(String value) {
        editor.putString("whatsapp", value);
        editor.apply();
    }
    public void setUserProfileImage(String value) {
        editor.putString("profile_url", value);
        editor.apply();
    }

    public void setTailorCategory(String value) {
        editor.putString("tailor_category", value);
        editor.apply();
    }

    public void setUserGender(String value) {
        editor.putString("user_gender", value);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", null);
    }
    public String getUserEmail() {
        return sharedPreferences.getString("email", null);
    }
    public String getUserCategory() {
        return sharedPreferences.getString("category", null);
    }
    public String getUserAddress() {
        return sharedPreferences.getString("address", null);
    }
    public String getUserTelephone() {
        return sharedPreferences.getString("telephone", null);
    }
    public String getUserWhatsapp() {
        return sharedPreferences.getString("whatsapp", null);
    }
    public String getUserProfileImage() {
        return sharedPreferences.getString("profile_url", null);
    }

    public String getTailorCategory() { return sharedPreferences.getString("tailor_category", null); }
    public String getUserGender() { return sharedPreferences.getString("user_gender", null); }
}
