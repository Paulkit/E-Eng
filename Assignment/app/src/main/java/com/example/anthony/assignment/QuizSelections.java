package com.example.anthony.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class QuizSelections extends AppCompatActivity  /*implements View.OnClickListener,
        OnShowcaseEventListener */{
    private int counter = 0;
  //  private ShowcaseView showcaseView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selections);
        setTitle("Quiz Selection");

        /** Syllabary description  */
       /*   ViewTarget target = new ViewTarget(R.id.btn_quiz, this);
      showcaseView = new ShowcaseView.Builder(this)
                //   .withMaterialShowcase()
                .setTarget(target)
                .setContentTitle("Syllabary")
                .setContentText("A list of alphabet with pronunciation")
                .setStyle(R.style.CustomShowcaseTheme2)
                .singleShot(42)
                .setShowcaseEventListener(QuizSelections.this)
                .setOnClickListener(this)
                //.replaceEndButton(R.layout.view_custom_button)
                .build();
        showcaseView.setButtonText("Next");*/
    }
    public void showQuiz1(View view) {
        Intent i = new Intent(this, Quiz1.class);
        startActivity(i);

    }
    public void showQuiz2(View view) {
        Intent i = new Intent(this, Quiz2.class);
        startActivity(i);

    }
/*
    @Override
    public void onClick(View view) {
        switch (counter) {
            case 0:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.btn_quiz2)), true);
                showcaseView.setContentTitle("Tutorials");
                showcaseView.setContentText("Several tutorials about word definition");
                break;

            case 1:
                showcaseView.hide();
                //  setAlpha(1.0f, textView1, textView2, textView3);
                break;
        }
        counter++;


    }*/


}
