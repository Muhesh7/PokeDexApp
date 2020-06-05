package com.example.pokedex;

import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class pokemon {
    private int number;
       String name;
    private String url;
    public  pokemon(String name,String url,int number)
    {
        this.name=name;
        this.url=url;
        this.number=number;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getNumber() {
        String[] ImgUrl= url.split("/");

        return Integer.parseInt(ImgUrl[ImgUrl.length-1]);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
