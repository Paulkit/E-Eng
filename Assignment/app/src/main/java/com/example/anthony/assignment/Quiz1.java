package com.example.anthony.assignment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class Quiz1 extends AppCompatActivity {

    ArrayList<Integer> list = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);
        new CallbackTask().execute(wordlist());

    }
    private String wordlist() {
        final String language = "en";
        final String filters = "lexicalCategory=noun,adjective;domains=sport";
        return "https://od-api.oxforddictionaries.com:443/api/v1/wordlist/" + language + "/" + filters;
    }
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "25e078c4";
            final String app_key = "99015df7c1045e3f566fd76780eb3cf9";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                JSONObject json = new JSONObject(stringBuilder.toString());
                JSONObject metadata  = json.getJSONObject("metadata");
                String total = metadata.getString("total");
                Log.i("Total of words :",total);

                JSONArray Words  = json.getJSONArray("results");
                for (int i = 0; i < Words.length(); i++) {
                    JSONObject jsonobject = Words.getJSONObject(i);
                    String word = jsonobject.getString("word");
                    Log.i("Word["+i+"]:",word);
                }

                return stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            System.out.println(result);

        }
    }
}

