package com.example.anthony.assignment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.anthony.assignment.usefulClass.Config;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,
        OnShowcaseEventListener, AdapterView.OnItemClickListener {
    private GoogleApiClient mGoogleApiClient;
    private long mLastClickTime = 0;
    private com.wang.avi.AVLoadingIndicatorView prograss_indecator;
    private static int REQUEST_LEADERBOARD = 100; //  indeed an arbitrary int that you define by yourself.

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;

    private ShowcaseView showcaseView;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Learning English");
        prograss_indecator = (com.wang.avi.AVLoadingIndicatorView) findViewById(R.id.avi);
        // Create the Google Api Client with access to the Play Games services

        /*   mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();
        */

        /** Syllabary description  */
        ViewTarget target = new ViewTarget(R.id.btn_syllabary, this);
        showcaseView = new ShowcaseView.Builder(this)
                //   .withMaterialShowcase()
                .setTarget(target)
                .setContentTitle("Title for btn_syllabary")  /** Syllabary Title */
                .setContentText("Content for btn_syllabary") /** Syllabary Title */
                .setStyle(R.style.CustomShowcaseTheme2)
                .singleShot(42)
                .setShowcaseEventListener(MainActivity.this)
                .setOnClickListener(this)
                //.replaceEndButton(R.layout.view_custom_button)
                .build();
        showcaseView.setButtonText("Next");
/*
        Target viewTarget = new ViewTarget(R.id.button, this);
        new ShowcaseView.Builder(this)
                .setTarget(viewTarget)
                .setContentTitle(R.string.title_single_shot)
                .setContentText(R.string.R_string_desc_single_shot)
                .singleShot(42)
                .build();*/
        //   sv.setButtonPosition(lps);


    }


    @Override
    protected void onStart() {
        super.onStart();
        // mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mGoogleApiClient.disconnect();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("paul", "onResume");
        //   GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
    }

    public void showSyllabary(View view) {
        Intent i = new Intent(this, syllabary.class);
        startActivity(i);
    }

    public void showQuiz(View view) {
        Intent i = new Intent(this, QuizSelections.class);
        startActivity(i);
    }

    public void showHighScore(View view) {
        //   mSignInClicked = true;
        //   mGoogleApiClient.connect();
    }

    public void showRecords(View view) {
        Intent i = new Intent(this, Records.class);
        startActivity(i);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

// if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow

        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, String.valueOf(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
                // prograss_indecator
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        getString(R.string.LEADER_BOARD)), REQUEST_LEADERBOARD);
            }
        }

        // Put code here to display the sign-in button
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }
    }

    // Call when the sign-in button is clicked
    private void signInClicked() {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }


    // go back  key pressed
    @Override
    public void onBackPressed() {


        Toast toast = Toast.makeText(this, "Pressed one more time to exit", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        if (mLastClickTime == 0) {
            mLastClickTime = SystemClock.elapsedRealtime();
            toast.show();
            return;
        }

        if (SystemClock.elapsedRealtime() - mLastClickTime < Config.EXIT_SHORT_DELAY) {
            super.onBackPressed();
        } else {

            mLastClickTime = SystemClock.elapsedRealtime();
            toast.show();
        }


    }

    private void setAlpha(float alpha, View... views) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            for (View view : views) {
                view.setAlpha(alpha);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (counter) {
            case 0:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.btn_quiz)), true);
                showcaseView.setContentTitle("Title for btn_quiz");  /** Syllabary Title */
                showcaseView.setContentText("Content for btn_quiz");  /** Syllabary Title */
                break;

            case 1:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.btn_highscore)), true);
                showcaseView.setContentTitle("Title for btn_highscore");  /** Syllabary Title */
                showcaseView.setContentText("Content for btn_highscore");  /** Syllabary Title */
                break;

            case 2:
                //  showcaseView.setTarget(Target.NONE);
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.btn_record)), true);
                showcaseView.setContentTitle("Title for btn_record");  /** Syllabary Title */
                showcaseView.setContentText("Content for btn_record");  /** Syllabary Title */
                showcaseView.setButtonText("Finish");
                //  showcaseView.setButtonText(getString(R.string.close));
                //    setAlpha(0.4f, textView1, textView2, textView3);
                break;

            case 3:
                showcaseView.hide();
                //  setAlpha(1.0f, textView1, textView2, textView3);
                break;
        }
        counter++;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

    }
}
