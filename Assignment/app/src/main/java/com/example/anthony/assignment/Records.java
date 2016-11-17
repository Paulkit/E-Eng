package com.example.anthony.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Records extends AppCompatActivity {
    ArrayList<String> output = new ArrayList<String>();
    String [] list;
    ListView lv_records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        setTitle("Records");
        if (readDataFromFile("records.xml") == true) {
            domParseXML(getStringFromFile("records.xml"));
            list = new String[output.size()];
            for (int i =0;i<output.size();i++){

                list[i]=output.get(i).toString();

            }
            lv_records = (ListView) findViewById(R.id.lv_records);
            ListAdapter adapter = new ArrayAdapter<>(this , R.layout.list_item,list);


            lv_records.setAdapter(adapter);
            lv_records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Log.i("Selected",list[position]);
                    Intent i = new Intent(Records.this, RecordSearch.class);
                    i.putExtra("word",list[position]);
                    startActivity(i);
                }
            });
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
