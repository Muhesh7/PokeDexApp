package com.example.pokedex;

import java.util.ArrayList;

public class mainTypeResults {
    private ArrayList<mainType> pokemon;

    public ArrayList<mainType> getResults(){
        return  pokemon;
    }

    public void setResults(ArrayList<mainType> results) {
        this.pokemon = results;
    }
}
