package com.example.anthony.assignment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anthony.assignment.usefulClass.App;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class Quiz3 extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private final int SPEECH_RECOGNITION_CODE = 1;
    private MediaPlayer mediaPlayer;
    private TextView txtOutput,textView;
    private ImageButton btnMicrophone;  private Handler mHandler = new Handler();
    Button button_Start;
    TextView tv_Score, tv_Life,tv_Question, tv_instruction3_1, tv_instruction3_2, tv_instruction3_3, tv_instruction3_4, tv_instruction3_5;
    int score, life;
    String answer = "";
    ArrayList<String> words = new ArrayList<String>();
    ArrayList<String> wrong = new ArrayList<String>();
    private GoogleApiClient mGoogleApiClient;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);
        setTitle("Speaking Quiz");
        tv_Score = (TextView) findViewById(R.id.tv_Score);
        tv_Life = (TextView) findViewById(R.id.tv_Life);
        txtOutput = (TextView) findViewById(R.id.txt_output);
        btnMicrophone = (ImageButton) findViewById(R.id.btn_mic);
        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }

        });
        button_Start = (Button) findViewById(R.id.button_Start);
        tv_Question= (TextView) findViewById(R.id.tv_Question);
        textView= (TextView) findViewById(R.id.textView);

        tv_instruction3_1 = (TextView) findViewById(R.id.tv_instruction3_1);
        tv_instruction3_2 = (TextView) findViewById(R.id.tv_instruction3_2);
        tv_instruction3_3 = (TextView) findViewById(R.id.tv_instruction3_3);
        tv_instruction3_4 = (TextView) findViewById(R.id.tv_instruction3_4);
        tv_instruction3_5 = (TextView) findViewById(R.id.tv_instruction3_5);

        //Check if connected
        if(App.getGoogleApiHelper().isConnected())
        {
            //Get google api client
            connected = true;
            mGoogleApiClient = App.getGoogleApiHelper().getGoogleApiClient();
        }


    }
    public void startQuiz(View view) {
        score = 0;
        life = 3;
        button_Start.setVisibility(View.INVISIBLE);
        tv_Score.setVisibility(View.VISIBLE);
        tv_Life.setVisibility(View.VISIBLE);
        btnMicrophone.setVisibility(View.VISIBLE);
        txtOutput.setVisibility(View.VISIBLE);
        tv_Question.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        words = getStringFromFile("dictionary.txt");
        tv_Life.setText("Life: " + life);
        tv_Score.setText("Score: " + score);

        tv_instruction3_1.setVisibility(View.INVISIBLE);
        tv_instruction3_2.setVisibility(View.INVISIBLE);
        tv_instruction3_3.setVisibility(View.INVISIBLE);
        tv_instruction3_4.setVisibility(View.INVISIBLE);
        tv_instruction3_5.setVisibility(View.INVISIBLE);

        Log.i("Total of words", String.valueOf(words.size()));
        GenNewAnswer();
    }
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void check() {

        if (txtOutput.getText().toString().toLowerCase().equals(answer)) {
            txtOutput.setText("");
            score++;
            if(connected) {

                Games.Leaderboards.submitScore(mGoogleApiClient,getString( R.string.LEADER_BOARD), score);
                if (score == 5) {
                    Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_reach_10_marks_in_speaking_quiz));

                } else if (score == 50) {
                    Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_reach_50_marks_in_speaking_quiz));

                } else if (score == 100) {
                    Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_reach_100_marks_in_speaking_quiz));

                }
            }
            tv_Score.setText("Score: " + score);
            GenNewAnswer();
        } else {
            txtOutput.setText("");
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
    public void GenNewAnswer() {
        Random r = new Random();
        int index = r.nextInt(words.size() - 1);
        answer = words.get(index).toString();
        Log.i("answer", answer);
        new CallbackTask().execute("https://ssl.gstatic.com/dictionary/static/sounds/de/0/" + answer + ".mp3");

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            if (result.equals("0")) GenNewAnswer(); else
                tv_Question.setText(answer);
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    txtOutput.setText(text);
                    try {
                        playAudio("https://ssl.gstatic.com/dictionary/static/sounds/de/0/" + answer + ".mp3");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            check();
                        }
                    }, 3000);

                }
                break;
            }
        }
    }
}
