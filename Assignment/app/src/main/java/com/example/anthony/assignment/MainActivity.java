package com.example.anthony.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    //https://ssl.gstatic.com/dictionary/static/sounds/de/0/[word].mp3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showSyllabary(View view){

    }

    public void showQuiz(View view){

    }

    public void showHighScore(View view){

    }

    public void quit(View view) {
        System.exit(0);
    }
}
