package com.example.anthony.assignment;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Quiz1Result extends AppCompatActivity {
    TextView tv_Score;
    ArrayList<String> wrong = new ArrayList<String>();
    ArrayList<String> output = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1_result);
        setTitle("Result");
        int score = getIntent().getIntExtra("Score", 0);
        wrong = getIntent().getStringArrayListExtra("wrong");
        if (readDataFromFile("records.xml") == true) {
            domParseXML(getStringFromFile("records.xml"));
        }
        for (int i = 0; i < wrong.size(); i++) {

            Log.i("Wrong", wrong.get(i).toString());
            output.add(i, wrong.get(i).toString());
        }
        tv_Score = (TextView) findViewById(R.id.tv_Score);
        tv_Score.setText("Score: " + String.valueOf(score));
        writeToXML(output);
        domParseXML(getStringFromFile("records.xml"));
        for (int i = 0; i < output.size(); i++) {

            Log.i("output", output.get(i).toString());

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
        NodeList recordsNodeList = root.getElementsByTagName("word");
        String ans =recordsNodeList.item(0).getFirstChild().getNodeValue();

        for (int i = 0; i < recordsNodeList.getLength(); i++)
        {
            String word =recordsNodeList.item(i).getFirstChild().getNodeValue();
            output.add(i,word);
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
    private void writeToXML(ArrayList<String> data) {
        XmlSerializer serializer = Xml.newSerializer();

        try {
            FileOutputStream fout = openFileOutput("records.xml", Context.MODE_PRIVATE);
            serializer.setOutput(fout, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "records");
            for (int i = 0; i < data.size(); i++) {
                serializer.startTag(null, "word");
                Log.i("write", data.get(i).toString());
                serializer.text(data.get(i).toString());
                serializer.endTag(null, "word");
            }
            serializer.endTag(null, "records");
            serializer.endDocument();
            serializer.flush();

            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
