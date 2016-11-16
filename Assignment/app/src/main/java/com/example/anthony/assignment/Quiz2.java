package com.example.anthony.assignment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class Quiz2 extends AppCompatActivity {
    private String path;
    private MediaPlayer mediaPlayer;
    Button button_Start, button_Submit;
    TextView tv_Score, tv_Life, textView2;
    EditText et_Input;
    int score, life;
    String answer = "";
    ArrayList<String> words = new ArrayList<String>();
    Button button_Play;
    ArrayList<String> wrong = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);
        setTitle("Listening Quiz");
        tv_Score = (TextView) findViewById(R.id.tv_Score);
        tv_Life = (TextView) findViewById(R.id.tv_Life);
        et_Input = (EditText) findViewById(R.id.et_Input);
        button_Play = (Button) findViewById(R.id.button_Play);
        button_Submit = (Button) findViewById(R.id.button_Submit);
        textView2 = (TextView) findViewById(R.id.textView2);
        button_Start = (Button) findViewById(R.id.button_Start);


    }

    public void startQuiz(View view) {
        score = 0;
        life = 3;
        button_Start.setVisibility(View.INVISIBLE);
        tv_Score.setVisibility(View.VISIBLE);
        tv_Life.setVisibility(View.VISIBLE);
        et_Input.setVisibility(View.VISIBLE);
        button_Submit.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.INVISIBLE);
        button_Play.setVisibility(View.VISIBLE);
        words = getStringFromFile("dictionary.txt");
        tv_Life.setText("Life: " + life);
        tv_Score.setText("Score: " + score);
        Log.i("Total of words", String.valueOf(words.size()));
        GenNewAnswer();
    }

    public void check(View view) {

        if (et_Input.getText().toString().toLowerCase().equals(answer)) {
            et_Input.setText("");
            score++;
            tv_Score.setText("Score: " + score);
            GenNewAnswer();
        } else {
            et_Input.setText("");
            life--;
            wrong.add(answer);
            tv_Life.setText("Life: " + life);
            GenNewAnswer();
            if (life == 0) {
                Intent i = new Intent(this, Quiz1Result.class);
                i.putExtra("Score", score);
                i.putExtra("wrong", wrong);
                this.finish();
                startActivity(i);
            }

        }


    }

    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                Log.i("statusCode",String.valueOf(statusCode));
                if (statusCode == 200) return "1";
                else return "0";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("0")) GenNewAnswer();
        }
    }

    public void GenNewAnswer() {
        Random r = new Random();
        int index = r.nextInt(words.size() - 1);
        answer = words.get(index).toString();
        Log.i("answer", answer);
        new CallbackTask().execute("https://ssl.gstatic.com/dictionary/static/sounds/de/0/" + answer + ".mp3");
        button_Play.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                    playAudio("https://ssl.gstatic.com/dictionary/static/sounds/de/0/" + answer + ".mp3");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList<String> getStringFromFile(String filename) {
        ArrayList<String> temp = new ArrayList<String>();
        String result = null;
        try {

            InputStream iStream = getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
            String line;
            while ((line = reader.readLine()) != null) {

                temp.add(line);
            }
            iStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }


    private void playAudio(String url) throws Exception {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
