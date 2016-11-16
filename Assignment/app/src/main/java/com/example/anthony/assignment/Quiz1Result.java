package com.example.anthony.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class Quiz1Result extends AppCompatActivity {
    TextView tv_Score;
    ArrayList<String> wrong = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1_result);
        setTitle("Result");
        int score = getIntent().getIntExtra("Score",0);
         wrong = getIntent().getStringArrayListExtra("wrong");

        for (int i =0;i<wrong.size();i++){

            Log.i("Wrong",wrong.get(i).toString());

        }
       tv_Score = (TextView) findViewById(R.id.tv_Score);
       tv_Score.setText("Score: "+String.valueOf(score));
    }
}
