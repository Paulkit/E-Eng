package com.example.anthony.assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class TutorialsWordSelections extends AppCompatActivity {
    String domain;
    ListView lv_TutorialsWords;
    ArrayList<WordData> words = new ArrayList<WordData>();
    String[] name;
    String[] wordID;
    String url ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials_word_selections);
        domain = getIntent().getStringExtra("domain");
        setTitle(domain);
        lv_TutorialsWords = (ListView) findViewById(R.id.lv_TutorialsWords);
         url = "https://od-api.oxforddictionaries.com:443/api/v1/wordlist/en/lexicalCategory=noun,adjective;domains=" + domain;

    }

}

