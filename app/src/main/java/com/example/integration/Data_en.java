package com.example.integration;

public class Data_en {
    private String text;
    //alt+insert
    public Data_en(String text){
        this.text = text;

    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }
}