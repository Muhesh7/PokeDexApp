package com.example.pokedex;

import android.net.Uri;

import java.util.ArrayList;

public class Model {
    private String height;
    private String weight;
    private ArrayList<typo> types;

    public Model(String height, String weight, ArrayList<typo> types) {
        this.height = height;
        this.weight = weight;
        this.types = types;
    }

    public ArrayList<typo> getTypes() {
        return types;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }
}
