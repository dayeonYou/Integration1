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
import java.util.Arrays;

public class Parcel_N extends AppCompatActivity {
    private ArrayList<Data_en> arrayList;
    private Adapter_en mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> contentTextArray = new ArrayList<>();
    private String string2 = null;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.parcel_not);
        setTitle("잘못 온 택배");
        Intent intent = getIntent();
        contentTextArray.add(intent.getStringExtra("text"));
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String save = sp.getString("save", "");
        if(save != null){
            String arr[] = save.split("\n");
            ArrayList<String> list = new ArrayList(Arrays.asList(arr));
            for(int i=0;i<list.size();i++){
                if(list.get(i) != null) contentTextArray.add(list.get(i));
            }
        }
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
        int i=0;
        for(i=0;i<contentTextArray.size();i++){
            if(i==0){
                string2 = contentTextArray.get(0);
            }
            if(string2 != null && i!=0){
                string2 = string2 + "\n" + contentTextArray.get(i);
            }
        }
        save(string2);
    }

    public void save(String s){
        sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("save",s);
        editor.commit();
    }
}