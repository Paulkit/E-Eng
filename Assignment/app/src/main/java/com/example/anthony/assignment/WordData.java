package com.example.anthony.assignment;

import java.util.ArrayList;


public class WordData {

    String id;
    String name;
    ArrayList<String> definitions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WordData(String id, String name) {
        this.id = id;
        this.name = name;
        this.definitions = new ArrayList<String>();
    }
    public String getName() {
        return name;
    }

    public ArrayList<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(ArrayList<String> definitions) {
        this.definitions = definitions;
    }

    public void setName(String name) {
        this.name = name;
    }
}
