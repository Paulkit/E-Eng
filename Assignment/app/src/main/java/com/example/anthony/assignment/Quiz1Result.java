package com.example.anthony.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Quiz1Result extends AppCompatActivity {
    TextView tv_Score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1_result);
        setTitle("Result");
        int score = getIntent().getIntExtra("Score",0);

       tv_Score = (TextView) findViewById(R.id.tv_Score);
       tv_Score.setText("Score: "+String.valueOf(score));
    }
}
