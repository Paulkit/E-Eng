package com.example.anthony.assignment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Quiz1 extends AppCompatActivity   implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private final CharSequence DialogTitle = "Loading";
    private final CharSequence DialogMessage = "Wait to load data...";
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog barProgressDialog;
    private int fileNumber = 0;
    private int diff = 1;
    Button button_Start,button_Submit;
    TextView tv_Score, tv_Life, tv_Definitions, tv_Hints,textView2;
    EditText et_Input;
    String domain = "";
    ArrayList<WordData> words = new ArrayList<WordData>();
    ArrayList<String> wrong = new ArrayList<String>();
    final String domains[] = {"Sport", "Art", "Literature", "Architecture"};
    String answer ="";
    int score,life,index;

    private AlertDialog.Builder alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);
        button_Start = (Button) findViewById(R.id.button_Start);
        tv_Score = (TextView) findViewById(R.id.tv_Score);
        tv_Life = (TextView) findViewById(R.id.tv_Life);
        tv_Definitions = (TextView) findViewById(R.id.tv_Definitions);
        tv_Hints = (TextView) findViewById(R.id.tv_Hints);
        et_Input = (EditText) findViewById(R.id.et_Input);
        button_Submit = (Button) findViewById(R.id.button_Submit);
        textView2 = (TextView) findViewById(R.id.textView2);

        alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeNoActionBar));
        setTitle("Word Quiz");
        Random r = new Random();
        int min = 0;
        int max = domains.length-1;
        domain = domains[r.nextInt(max - min + 1) + min];


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();

        if (readDataFromFile(domain + ".xml") == false) {
            new CallbackTask().execute(wordlist());
        } else {
            domParseXML(getStringFromFile(domain + ".xml"));
            button_Start.setVisibility(View.VISIBLE);
            setTitle("Word Quiz - "+domain);
        }

    }

    public void startQuiz(View view) {
        score = 0;
        life = 3;
        index = 0;
        genWordData(words);
        button_Start.setVisibility(View.INVISIBLE);
        tv_Score.setVisibility(View.VISIBLE);
        tv_Life.setVisibility(View.VISIBLE);
        tv_Definitions.setVisibility(View.VISIBLE);
        tv_Hints.setVisibility(View.VISIBLE);
        et_Input.setVisibility(View.VISIBLE);
        button_Submit.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.INVISIBLE);

        tv_Life.setText("Life: " + life);
        tv_Score.setText("Score: " + score);
        tv_Definitions.setMovementMethod(ScrollingMovementMethod.getInstance());

        answer = words.get(index).getName();
        Log.i("ANSWER",answer);
        String temp = answer;

        for (int i =0;i<3;i++) {
            Random r = new Random();
            char c = (char)(r.nextInt(26) + 'a');
            temp += c;
        }

        char[] charArray = temp.toCharArray();
        shuffleArray(charArray);
        genDefinition(index);
        genHints(charArray);
    }


    private void shuffleArray(char[] array) {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }
    private void genHints(char[] array) {

        String hint = "";
        for (int i = 0; i < array.length; i++) {
            boolean isRepeated = false;
            for (int j =array.length - 1; j > i; j--) {
                if (array[i]==(array[j])) {
                    isRepeated = true;
                    break;
                }
            }
            if (!isRepeated) {

                hint += array[i];
            }
        }

        tv_Hints.setText(hint);
    }
    public void check(View view) {
        Log.i ("index",String.valueOf(index));
        Log.i ("leh",String.valueOf(words.size()));
        if (index<words.size()){
            if (et_Input.getText().toString().toLowerCase().equals(answer)){
                et_Input.setText("");
                score++;

                if(score==5){
                    Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_Reach_10_word));

                }else if(score==50){
                    Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_Reach_50_word));

                }else if(score==100){
                    Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_Reach_100_word));

                }

                tv_Score.setText("Score: " + score);
                index++;
                answer = words.get(index).getName();
                Log.i("ANSWER",answer);
                String temp = answer;

                for (int i =0;i<3;i++) {
                    Random r = new Random();
                    char c = (char)(r.nextInt(26) + 'a');
                    temp += c;
                }
                char[] charArray = temp.toCharArray();
                shuffleArray(charArray);
                genDefinition(index);
                genHints(charArray);
            }
            else {
                et_Input.setText("");
                life--;
                wrong.add(answer);
                tv_Life.setText("Life: " + life);
                index++;
                answer = words.get(index).getName();
                Log.i("ANSWER",answer);
                String temp = answer;

                for (int i =0;i<3;i++) {
                    Random r = new Random();
                    char c = (char)(r.nextInt(26) + 'a');
                    temp += c;
                }
                char[] charArray = temp.toCharArray();
                shuffleArray(charArray);
                genDefinition(index);
                genHints(charArray);
                if (life==0){
                    Intent i = new Intent(this, Quiz1Result.class);
                    i.putExtra("Score", score);
                    i.putExtra("wrong", wrong);
                    this.finish();
                    startActivity(i);
                }
            }

        }else {

            Intent i = new Intent(this, Quiz1Result.class);
            i.putExtra("Score", score);
            i.putExtra("wrong", wrong);
            this.finish();
            startActivity(i);
        }

    }

    private void genWordData(ArrayList<WordData> gen) {

        Collections.shuffle(gen);
    }

    private void genDefinition(int index) {
        String Definitions = "Definitions:\n";
        for (int i = 0; i < words.get(index).getDefinitions().size(); i++) {
            boolean isRepeated = false;
            for (int j = words.get(index).getDefinitions().size() - 1; j > i; j--) {
                if (words.get(index).getDefinitions().get(i).toString().equals(words.get(index).getDefinitions().get(j).toString())) {
                    isRepeated = true;
                    break;
                }
            }
            if (!isRepeated) {
                Definitions += "- " + words.get(index).getDefinitions().get(i).toString() + "\n\n";
            }
        }
        tv_Definitions.setText(Definitions);
    }

    private String wordlist() {
        final String language = "en";
        final String filters = "lexicalCategory=noun,adjective;domains=" + domain;
        return "https://od-api.oxforddictionaries.com:443/api/v1/wordlist/" + language + "/" + filters;
    }

    private void writeDataToFile(String filename, String data) {
        try {
            FileOutputStream fout = openFileOutput(filename, Context.MODE_PRIVATE);
            fout.write(data.getBytes());
            fout.close();
        } catch (FileNotFoundException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

          //  alertBuilderConfirm("Error occurred", "FileNotFoundException: " + e , "Confirm",Do);
        } catch (IOException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

            alertBuilderConfirm("Error occurred", "IOException: " + e , "Confirm",null);
        }
    }

    private void domParseXML(String XMLString) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

            alertBuilderConfirm("Error occurred", "ParserConfigurationException: " + e , "Confirm",Do);
        }

        Document doc = null;
        try {
            doc = db.parse(new InputSource(new StringReader(XMLString)));
        } catch (SAXException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

            alertBuilderConfirm("Error occurred", "SAXException: " + e , "Confirm",Do);
        } catch (IOException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

            alertBuilderConfirm("Error occurred", "IOException: " + e , "Confirm",Do);
        }
        Element root = doc.getDocumentElement();
        NodeList wordsNodeList = root.getElementsByTagName("word");
        for (int i = 0; i < wordsNodeList.getLength(); i++) {
            Element word = (Element) wordsNodeList.item(i);
            String name = word.getAttribute("name");
            String id = word.getAttribute("id");
            int numberOfDefinitions = Integer.parseInt(word.getAttribute("numberOfDefinitions"));
            words.add(new WordData(id, name));

            for (int a = 0; a < numberOfDefinitions; a++) {
                words.get(i).getDefinitions().add(word.getElementsByTagName("definition" + a).item(0).getFirstChild().getNodeValue());

            }
        }
    }

    private String getStringFromFile(String filename) {
        String result = null;
        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fin = openFileInput(filename);
            byte[] data = new byte[fin.available()];
            while (fin.read(data) != -1) {
                sb.append(new String(data));
            }
            fin.close();
            result = sb.toString();
        } catch (FileNotFoundException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

        //    alertBuilderConfirm("Error occurred", "FileNotFoundException: " + e , "Confirm",Do);
        } catch (IOException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

            alertBuilderConfirm("Error occurred", "IOException: " + e , "Confirm",null);
        }
        return result;
    }

    private boolean readDataFromFile(String filename) {
        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fin = openFileInput(filename);

        } catch (FileNotFoundException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

           // alertBuilderConfirm("Error occurred", "FileNotFoundException: " + e , "Confirm",null);
            return false;
        } catch (IOException e) {
            DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            };

            alertBuilderConfirm("Error occurred", "IOException: " + e , "Confirm",null);
            return false;
        }
        return true;
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
            int progress = 0;
            //TODO: replace with your own app id and app key
            final String app_id = "6d1da903";
            final String app_key = "610188df55f40e7dc8135da75a622b58";
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

                barProgressDialog.setMax(results.length());
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
                            } else break;
                        }
                    }
                    publishProgress(Integer.valueOf(progress));
                    progress++;

                }
                return stringBuilder.toString();

            } catch (final Exception e) {

                Quiz1.this.runOnUiThread(new Runnable() {
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
        }

        @Override
        protected void onPreExecute() {
            barProgressDialog = new ProgressDialog(Quiz1.this);

            barProgressDialog.setTitle(DialogTitle);
            barProgressDialog.setMessage(DialogMessage);
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(0);
            barProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            barProgressDialog.incrementProgressBy(diff);
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            XmlSerializer serializer = Xml.newSerializer();

            try {
                FileOutputStream fout = openFileOutput(domain + ".xml", Context.MODE_PRIVATE);
                serializer.setOutput(fout, "UTF-8");
                serializer.startDocument(null, Boolean.valueOf(true));
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startTag(null, "words");
                for (int i = 0; i < words.size(); i++) {
                    serializer.startTag(null, "word");
                    serializer.attribute(null, "name", words.get(i).getName());
                    serializer.attribute(null, "id", words.get(i).getId());
                    serializer.attribute(null, "numberOfDefinitions", Integer.toString(words.get(i).getDefinitions().size()));

                    for (int a = 0; a < words.get(i).getDefinitions().size(); a++) {

                        serializer.startTag(null, "definition" + a);
                        serializer.text(words.get(i).getDefinitions().get(a));
                        serializer.endTag(null, "definition" + a);

                    }
                    serializer.endTag(null, "word");
                }
                serializer.endTag(null, "words");
                serializer.endDocument();

                serializer.flush();

                fout.close();
            } catch (FileNotFoundException e) {

           //     alertBuilderConfirm("Error occurred", "FileNotFoundException: " + e , "Confirm",null);
            } catch (IOException e) {
                DialogInterface.OnClickListener Do = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                };

                alertBuilderConfirm("Error occurred", "IOException: " + e , "Confirm",null);
            }

            barProgressDialog.dismiss();
            setTitle("Word Quiz - "+domain);
            button_Start.setVisibility(View.VISIBLE);

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

