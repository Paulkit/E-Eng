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

    String domain = "";
    ArrayList<WordData> words = new ArrayList<WordData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);
        new CallbackTask().execute(wordlist());
        setTitle(domain);
    }

    private String wordlist() {
        final String language = "en";
        final String domains[] = {"Sport", "Art", "Literature", "Music", "Variety", "Architecture"};


        int min = 0;
        int max = domains.length;

        Random r = new Random();
        //domain = domains[r.nextInt(max - min + 1) + min];
        domain = domains[0];
        final String filters = "lexicalCategory=noun,adjective;domains=" + domain;
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
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                JSONObject json = new JSONObject(stringBuilder.toString());
                JSONObject metadata = json.getJSONObject("metadata");
                String total = metadata.getString("total");
                Log.i("Total of words", total);

                JSONArray results = json.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonobject = results.getJSONObject(i);
                    words.add(new WordData(jsonobject.getString("id"), jsonobject.getString("word")));
                }
                for (int index = 0; index < words.size(); index++) {
                    Log.i("Word[" + index + "]", words.get(index).getName());

                    url = new URL("https://od-api.oxforddictionaries.com:443/api/v1/entries/en/" + words.get(index).getId());
                    urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("app_id", app_id);
                    urlConnection.setRequestProperty("app_key", app_key);

                    // read the output from the server
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    stringBuilder = new StringBuilder();

                    line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    json = new JSONObject(stringBuilder.toString());
                    results = json.getJSONArray("results");

                    JSONArray lexicalEntriesArray = results.getJSONObject(0).getJSONArray("lexicalEntries");
                    for (int i = 0; i < lexicalEntriesArray.length(); i++) {
                        JSONArray entriesArray = lexicalEntriesArray.getJSONObject(i).getJSONArray("entries");
                        JSONArray sensesArray = entriesArray.getJSONObject(0).getJSONArray("senses");

                        for (int a = 0; a < sensesArray.length(); a++) {
                            if (sensesArray.getJSONObject(a).has("definitions")) {
                                words.get(index).getDefinitions().add(sensesArray.getJSONObject(a).getJSONArray("definitions").getString(0));
                            }else break;
                        }
                    }
                }
                return stringBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            for (int i = 0;i<words.size();i++){

                for (int a = 0;a<words.get(i).getDefinitions().size();a++)
                {
                    System.out.println(words.get(i).getName()+":  "+words.get(i).getDefinitions().get(a));
            }

            }
           // System.out.println(result);

        }
    }
}

