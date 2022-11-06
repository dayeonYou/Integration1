package com.example.integration;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_en extends RecyclerView.Adapter<Adapter_en.CustomViewHolder> {
    private ArrayList<Data_en> arrayList;
    public Adapter_en(ArrayList<Data_en> arrayList){
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public Adapter_en.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_en,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter_en.CustomViewHolder holder, int position){
        holder.text.setText(arrayList.get(position).getText());
        holder.itemView.setTag(position);
    }
    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount(){
        return (null!=arrayList ? arrayList.size() : 0);
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView text;

        public CustomViewHolder(@NonNull View itemView){
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}