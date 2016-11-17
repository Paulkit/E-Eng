package com.example.anthony.assignment.usefulClass;

/**
 * Created by paulck on 17/11/2016.
 */


import android.content.Context;
import android.content.SharedPreferences;


public class CustomPreferences {
    private Context mContext;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public CustomPreferences(Context context) {

        mContext = context;
        prefs = mContext.getSharedPreferences(mContext.getPackageName(), mContext.MODE_PRIVATE);
        editor = prefs.edit();

    }

    /** create preference and store preference also use this same method */
    public void storePreference(String tag, String val) {

        editor.putString(tag, val).apply();

    }

    public String getPrefrence(String tag) { // assign value to correspond position in tags array

        tag = prefs.getString(tag, "null");

        return tag;
    }

    public void clearPreference() { // All SharedPreferences will be erased
        editor.clear().apply();
    }

    public void removePreference(String tag) {
        editor.remove(tag).apply();

    }


}
