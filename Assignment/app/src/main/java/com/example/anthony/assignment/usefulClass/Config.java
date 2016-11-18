package com.example.anthony.assignment.usefulClass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by paulck on 17/11/2016.
 */

public class Config {
    public static final int EXIT_SHORT_DELAY = 2000; // 2 seconds
    public static final int LOADING_SHORT_DELAY = 2000; // 2 seconds


    public static void stateControls(boolean able, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(able);
            if (child instanceof ViewGroup) {
                stateControls(able, (ViewGroup) child);
            }
        }
    }


}
