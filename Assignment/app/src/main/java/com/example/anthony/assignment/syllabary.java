package com.example.anthony.assignment;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class syllabary extends AppCompatActivity {

    //https://ssl.gstatic.com/dictionary/static/sounds/de/0/[word].mp3
    private String path;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabary);
        try {
            playAudio("https://ssl.gstatic.com/dictionary/static/sounds/de/0/a.mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAudio(String url) throws Exception{
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }




}
