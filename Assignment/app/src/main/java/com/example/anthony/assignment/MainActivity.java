package com.example.anthony.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private com.wang.avi.AVLoadingIndicatorView prograss_indecator;
private static int REQUEST_LEADERBOARD = 100; //  indeed an arbitrary int that you define by yourself.

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;

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

    }


    @Override
    protected void onStart() {
        super.onStart();
     //   mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    //    mGoogleApiClient.disconnect();
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

    public void quit(View view) {
        System.exit(0);
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
                       getString( R.string.LEADER_BOARD)), REQUEST_LEADERBOARD);
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

}
