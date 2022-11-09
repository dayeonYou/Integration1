package com.example.integration;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Handle_missing_parcel extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.handle_missing_parcel);
        setTitle("분실 택배 대처법!");
    }
}
