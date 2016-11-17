package com.example.anthony.assignment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

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
        textView = (TextView) findViewById(R.id.textView);
        new CallbackTask().execute(dictionaryEntries());
    }
    private String dictionaryEntries() {
        final String language = "en";
        final String word_id = wordName.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + wordID;
    }


    //in android calling network requests on the main thread forbidden by default
    //create class to do async job
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "6d1da903";
            final String app_key = "610188df55f40e7dc8135da75a622b58";
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
                JSONArray results = json.getJSONArray("results");

                JSONArray lexicalEntriesArray = results.getJSONObject(0).getJSONArray("lexicalEntries");
                for (int i = 0; i < lexicalEntriesArray.length(); i++) {
                    JSONArray entriesArray = lexicalEntriesArray.getJSONObject(i).getJSONArray("entries");
                    JSONArray sensesArray = entriesArray.getJSONObject(0).getJSONArray("senses");

                    for (int a = 0; a < sensesArray.length(); a++) {
                        if (sensesArray.getJSONObject(a).has("definitions")) {
                            definitions.add(sensesArray.getJSONObject(a).getJSONArray("definitions").getString(0));
                        } else break;
                    }
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
            String out = "";
            for (int i =0;i<definitions.size();i++){
                out+= i+1+". "+definitions.get(i).toString()+"\n\n";
            }

            textView.setText(out);
        }

    }
}

