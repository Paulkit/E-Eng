package com.example.anthony.assignment.usefulClass;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by paulck on 18/11/2016.
 */

public class App extends Application {
    private GoogleApiHelper googleApiHelper;

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        googleApiHelper = new GoogleApiHelper(getApplicationContext());
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }
    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }


}