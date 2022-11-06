package com.example.integration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Parcel_N extends AppCompatActivity {
    private ArrayList<Data_en> arrayList;
    private Adapter_en mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> contentTextArray = new ArrayList<>();
    private String string2 = "aaa";
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.parcel_not);
        setTitle("잘못 온 택배");
        int i = 0, count = 0;
        Intent intent = getIntent();
        contentTextArray.add(intent.getStringExtra("text"));
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String save = sp.getString("save", "");
        contentTextArray.add(save);

        if (contentTextArray.get(0) != null) {
            recyclerView = (RecyclerView) findViewById(R.id.rvN);
            recyclerView.setVisibility(View.VISIBLE);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);

            arrayList = new ArrayList<>();

            mainAdapter = new Adapter_en(arrayList);
            recyclerView.setAdapter(mainAdapter);
            for (int k = 0; k < contentTextArray.size(); k++) {
                Data_en data_en = new Data_en(contentTextArray.get(k));
                arrayList.add(data_en);
                mainAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        save(string2);
    }

    public void save(String s){
        sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("save",s);
        editor.commit();
    }
}