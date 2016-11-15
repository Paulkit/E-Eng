package com.example.anthony.assignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
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

public class Quiz1 extends AppCompatActivity {

    private final CharSequence DialogTitle = "Loading";
    private final CharSequence DialogMessage = "Wait to load data...";

    private ProgressDialog barProgressDialog;
    private int fileNumber = 0;
    private int diff = 1;
    Button button_Start;
    TextView tv_Score, tv_Life, tv_Definitions, tv_Input;
    String domain = "";
    ArrayList<WordData> words = new ArrayList<WordData>();
    final String domains[] = {"Sport", "Art", "Literature", "Music", "Variety", "Architecture"};
    String input = "";
    String answer ="";
    int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);
        button_Start = (Button) findViewById(R.id.button_Start);
        tv_Score = (TextView) findViewById(R.id.tv_Score);
        tv_Life = (TextView) findViewById(R.id.tv_Life);
        tv_Definitions = (TextView) findViewById(R.id.tv_Definitions);
        tv_Input = (TextView) findViewById(R.id.tv_Input);

        setTitle("Quiz 1");
        Random r = new Random();
        int min = 0;
        int max = domains.length;
        //domain = domains[r.nextInt(max - min + 1) + min];
        domain = domains[0];

        if (readDataFromFile(domain + ".xml") == false) {
            new CallbackTask().execute(wordlist());
        } else {
            domParseXML(getStringFromFile(domain + ".xml"));
            button_Start.setVisibility(View.VISIBLE);
            setTitle(domain);
        }

    }

    public void startQuiz(View view) {
         score = 0;
        int life = 3;
        int index = 0;
         genWordData(words);
        button_Start.setVisibility(View.INVISIBLE);
        tv_Score.setVisibility(View.VISIBLE);
        tv_Life.setVisibility(View.VISIBLE);
        tv_Definitions.setVisibility(View.VISIBLE);
        tv_Input.setVisibility(View.VISIBLE);

        tv_Life.setText("Life: " + life);
        tv_Score.setText("Score: " + score);
        tv_Input.setText(input);
        tv_Definitions.setMovementMethod(ScrollingMovementMethod.getInstance());

         answer = words.get(index).getName();
        char[] charArray = answer.toCharArray();
        shuffleArray(charArray);
        genDefinition(index);
        genButton(charArray);
    }

    private void genButton(char[] array) {
        final LinearLayout lm = (LinearLayout) findViewById(R.id.linearMain);
        final LinearLayout lm2 = (LinearLayout) findViewById(R.id.linearMain2);
        final LinearLayout lm3 = (LinearLayout) findViewById(R.id.linearMain3);
        final LinearLayout lm4 = (LinearLayout) findViewById(R.id.linearMain4);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < array.length; i++) {
            // Create LinearLayout
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            Log.i(" a", String.valueOf(array[i]));
            final Button btn = new Button(this);
            btn.setId(i);

            btn.setLayoutParams(params);
            btn.setText(String.valueOf(array[i]));
            btn.setTextSize(20);
            final int index = i;
            // Set click listener for button
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Log.i("TAG", "index :" + index);
                    input += btn.getText();
                    btn.setVisibility(View.INVISIBLE);
                    tv_Input.setText(input);
                }
            });

            ll.addView(btn);

            if (i < 3) {
                lm.addView(ll);
            } else if (i < 6) {
                lm2.addView(ll);
            } else if (i < 9) {
                lm3.addView(ll);
            } else if (i < 12) {
                lm4.addView(ll);
            }
        }
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

    public void check(View view) {
        if (input.equals(answer)){
            score++;
            tv_Score.setText("Score: " + score);
            input = "";
            tv_Input.setText(input);
        }
        else {
            input = "";
            tv_Input.setText(input);
            char[] temp = answer.toCharArray();
            for (int i = 0; i < temp.length; i++) {
                Button  btn = (Button) findViewById(i);
                btn.setVisibility(View.VISIBLE);
            }
        }
    }

    private void genWordData(ArrayList<WordData> gen) {

        Collections.shuffle(gen);
    }

    private void genDefinition(int index) {
        String Definitions = "Definitions:\n";
        for (int i = 0; i < words.get(index).getDefinitions().size(); i++) {
            Log.i("Definitions", words.get(index).getDefinitions().get(i).toString());
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void domParseXML(String XMLString) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = db.parse(new InputSource(new StringReader(XMLString)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean readDataFromFile(String filename) {
        try {
            StringBuilder sb = new StringBuilder();
            FileInputStream fin = openFileInput(filename);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            int progress = 0;
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

            } catch (Exception e) {
                e.printStackTrace();
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
                //write xml data into the FileOutputStream
                serializer.flush();
                //finally we close the file stream
                fout.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            barProgressDialog.dismiss();
            setTitle(domain);
            button_Start.setVisibility(View.VISIBLE);
            Log.i("0", words.get(0).getName());
            Log.i("0", words.get(0).getDefinitions().get(0).toString());
            Log.i("0", words.get(0).getDefinitions().get(1).toString());
        }
    }
}

