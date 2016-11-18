package com.example.anthony.assignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anthony.assignment.usefulClass.Config;

public class syllabary extends AppCompatActivity {

    //https://ssl.gstatic.com/dictionary/static/sounds/de/0/[word].mp3
    private String path;
    private MediaPlayer mediaPlayer;
    private com.wang.avi.AVLoadingIndicatorView prograss_indecator;
    private RelativeLayout loading_bg;
    private RelativeLayout activity_bg;
    private AlertDialog.Builder alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabary);
        setTitle("Syllabary");
        alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeNoActionBar));
        loading_bg = (RelativeLayout) findViewById(R.id.grey_background);
        prograss_indecator = (com.wang.avi.AVLoadingIndicatorView) findViewById(R.id.avi);
        activity_bg = (RelativeLayout) findViewById(R.id.grey_background);
    }

    public void showUI() {
        prograss_indecator.setVisibility(View.GONE);
        loading_bg.setVisibility(View.GONE);
        Config.stateControls(true, activity_bg);
    }

    public void lockUI() {
        prograss_indecator.setVisibility(View.VISIBLE);
        loading_bg.setVisibility(View.VISIBLE);
        Config.stateControls(false, activity_bg);

    }

    public void playSyllabary(View view) {
        /** lock ui & show loading bar*/
        lockUI();
        try {
            playAudio("https://ssl.gstatic.com/dictionary/static/sounds/de/0/" + ((TextView) view).getText().toString() + ".mp3");
        } catch (Exception e) {
            e.printStackTrace();

            alertBuilderConfirm("Error occurred", "Message: " + e, "Confirm",null);
            /** realse ui*/
            showUI();
        }
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
        /** realse ui*/
        showUI();
    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            }
            catch (Exception e) {
                e.printStackTrace();
                DialogInterface.OnClickListener yesListen = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                };
                alertBuilderConfirm("Error occurred", "Message: " + e, "Confirm",yesListen);
                /** realse ui*/
                showUI();
            }
        }
    }


    public void alertBuilderConfirm(String title, String msg, String yes, DialogInterface.OnClickListener yesListen) {
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.setPositiveButton(yes, yesListen);
        alert.show();
    }




}
