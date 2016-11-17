package com.example.anthony.assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.anthony.assignment.usefulClass.Config;

/**
 * Created by paulck on 17/11/2016.
 */

public class SplashActivity extends Activity {


    //private com.wang.avi.AVLoadingIndicatorView prograss_indecator;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


      //  prograss_indecator = (com.wang.avi.AVLoadingIndicatorView) findViewById(R.id.avi);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        }, Config.LOADING_SHORT_DELAY);




    }

    @Override
    public void onBackPressed() {


    }

}
