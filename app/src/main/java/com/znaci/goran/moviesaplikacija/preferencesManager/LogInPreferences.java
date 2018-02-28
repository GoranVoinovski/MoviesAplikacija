package com.znaci.goran.moviesaplikacija.preferencesManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.znaci.goran.moviesaplikacija.models.RatedList;

/**
 * Created by Goran on 2/12/2018.
 */

public class LogInPreferences {

    private static SharedPreferences getPreferences(Context c){
        return c.getApplicationContext().getSharedPreferences("UserID", Activity.MODE_PRIVATE);
    }

    public static void  setGuestUserID (String Email , Context c){

        getPreferences(c).edit().putString("GuestUserID",Email).apply();
    }

    public static String  getGuestUserID (Context c){

        return getPreferences(c).getString("GuestUserID","");
    }

    public static void removeGuestUserID(Context c) {
        getPreferences(c).edit().remove("GuestUserID").apply();
    }

    public static void  setUserID (String Email , Context c){

        getPreferences(c).edit().putString("UserLogin",Email).apply();
    }

    public static String  getUserID (Context c){

        return getPreferences(c).getString("UserLogin","");
    }

    public static void removeUserID(Context c) {
        getPreferences(c).edit().remove("UserLogin").apply();
    }

    public static void  setSessionID (String Email , Context c){

        getPreferences(c).edit().putString("SessionID",Email).apply();
    }

    public static String  getSessionID (Context c){

        return getPreferences(c).getString("SessionID","");
    }

    public static void removeSessionID(Context c) {
        getPreferences(c).edit().remove("SessionID").apply();
    }

    public static void  setAccountID (int Email , Context c){

        getPreferences(c).edit().putInt("AccountID",Email).apply();
    }

    public static int  getAccountID (Context c){

        return getPreferences(c).getInt("AccountID",0);
    }

    public static void removeAccountID(Context c) {
        getPreferences(c).edit().remove("AccountID").apply();
    }

    public static void addRated (RatedList rated, Context c){

        Gson gson = new Gson();
        String mapString = gson.toJson(rated);
        getPreferences(c).edit().putString("Rated", mapString).apply();


    }
    public static RatedList getRated (Context c){

        return new Gson().fromJson(getPreferences(c).getString("Rated", ""), RatedList.class);
    }

    public static void removeRated(Context c){

        getPreferences(c).edit().remove("Rated").apply();

    }

    public static void addRatedShow (RatedList rated, Context c){

        Gson gson = new Gson();
        String mapString = gson.toJson(rated);
        getPreferences(c).edit().putString("RatedShow", mapString).apply();


    }
    public static RatedList getRatedShow (Context c){

        return new Gson().fromJson(getPreferences(c).getString("RatedShow", ""), RatedList.class);
    }

    public static void removeRatedShow(Context c){

        getPreferences(c).edit().remove("RatedShow").apply();

    }
}
