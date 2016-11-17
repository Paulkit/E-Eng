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

public class TutorialsSelections extends AppCompatActivity {

    String [] list = {"Sport", "Art", "Literature", "Architecture"};
    ListView lv_Tutorials;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials_selections);
        setTitle("Tutorials Selection");
        lv_Tutorials = (ListView) findViewById(R.id.lv_Tutorials);
        ListAdapter adapter = new ArrayAdapter<>(this , R.layout.list_item,list);


        lv_Tutorials.setAdapter(adapter);
        lv_Tutorials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Selected",list[position]);
                Intent i = new Intent(TutorialsSelections.this, TutorialsWordSelections.class);
                i.putExtra("domain",list[position]);
                startActivity(i);
            }
        });
    }
}
