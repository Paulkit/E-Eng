package com.example.anthony.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showSyllabary(View view){
        Intent i = new Intent(this, syllabary.class);
        startActivity(i);
    }

    public void showQuiz(View view){

    }

    public void showHighScore(View view){

    }

    public void quit(View view) {
        System.exit(0);
    }
}
