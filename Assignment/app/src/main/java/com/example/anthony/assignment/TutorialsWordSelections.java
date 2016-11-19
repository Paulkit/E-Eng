package com.example.anthony.assignment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class TutorialsWordSelections extends AppCompatActivity {
    String domain;
    ListView lv_TutorialsWords;
    ArrayList<WordData> words = new ArrayList<WordData>();
    String[] name = {""};
    String[] wordID ;
    ArrayList<String> lst = new ArrayList<String>(Arrays.asList(name));
    String url ;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials_word_selections);
        domain = getIntent().getStringExtra("domain");
        setTitle(domain);
        alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeNoActionBar));
        lv_TutorialsWords = (ListView) findViewById(R.id.lv_TutorialsWords);
        url = "https://od-api.oxforddictionaries.com:443/api/v1/wordlist/en/lexicalCategory=noun,adjective;domains=" + domain;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this , R.layout.list_item,lst);
        lv_TutorialsWords.setAdapter(adapter);
        new CallbackTask().execute(url);

    }

    private class CallbackTask extends AsyncTask<String, String, String> {

        ArrayAdapter<String> adapter;
        @Override
        protected String doInBackground(String... params) {
            //TODO: replace with your own app id and app key
            final String app_id = "11fc2c06";
            final String app_key = "4f473db2deb74248730c7c0d178ce451";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);

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
                wordID = new String[results.length()];
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonobject = results.getJSONObject(i);
                    words.add(new WordData(jsonobject.getString("id"), jsonobject.getString("word")));
                    publishProgress(jsonobject.getString("word"));
                    wordID[i]=jsonobject.getString("id");
                }


            } catch (final Exception e) {



                TutorialsWordSelections.this.runOnUiThread(new Runnable() {
                    public void run() {

                        DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        };

                        alertBuilderConfirm("Error occurred", "Message: " + e , "Confirm",Do);
                    }
                });

                return e.toString();
            }
                 return "Finish Async";
        }
        @Override
        protected void onPreExecute() {
           super.onPreExecute();
            adapter = (ArrayAdapter<String>) lv_TutorialsWords.getAdapter();
            adapter.clear();

        }

        protected void onProgressUpdate(String... progress) {
          super.onProgressUpdate();
            adapter.add(progress[0]);
            adapter.notifyDataSetChanged ();
            lv_TutorialsWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("Selected ID",wordID[position]);
                    Log.i("Selected Word",lst.get(position));
                     Intent i = new Intent(TutorialsWordSelections.this, TutorialsWord.class);
                      i.putExtra("name",lst.get(position));
                    i.putExtra("wordID",wordID[position]);
                    startActivity(i);

                }
            });
        }



        @Override
        protected void onPostExecute(String result) {
          super.onPostExecute(result);

            Log.i("post",result);
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

