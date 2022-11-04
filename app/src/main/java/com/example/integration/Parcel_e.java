package com.example.integration;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Parcel_e extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.parcel_end);
        setTitle("회수된 택배");
    }
}