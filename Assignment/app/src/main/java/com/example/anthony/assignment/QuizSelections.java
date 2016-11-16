package com.example.anthony.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class QuizSelections extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selections);
        setTitle("Quiz Selection");
    }
    public void showQuiz1(View view) {
        Intent i = new Intent(this, Quiz1.class);
        startActivity(i);

    }
    public void showQuiz2(View view) {
        Intent i = new Intent(this, Quiz2.class);
        startActivity(i);

    }
}
