package com.example.anthony.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class TutorialsWord extends AppCompatActivity {
    String wordName,wordID;
    ArrayList<String> definitions = new ArrayList<>();
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials_word);
        wordName = getIntent().getStringExtra("name");
        wordID = getIntent().getStringExtra("wordID");
        setTitle(wordName);
        textView= (TextView) findViewById(R.id.textView);

    }
}
