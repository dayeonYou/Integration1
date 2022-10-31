package com.example.integration;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;


    public CustomAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);
        holder.tv_id.setText(arrayList.get(position).getId());
        holder.rightParcel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(),"YOUR PARCEL",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("확인 버튼을 누르면 받은 택배로 취급됩니다.");
                builder.setTitle("YOUR PARCEL");
                builder.setCancelable(false);
                builder.setNegativeButton("확인", (DialogInterface.OnClickListener) (dialog, which) -> {
                    remove(holder.getAdapterPosition());
                    dialog.dismiss();
                });
                builder.setPositiveButton("취소", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        holder.wrongParcel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(),"NOT YOUR PARCEL",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("\n자신의 것이 아닌 택배를 받은 경우\n      1. 받는 사람과 주소를 확인합니다.\n      2. 받는 사람의 전화번호로 연락해봅니다.\n      3. 택배기사에게 연락해봅니다.\n");
                builder.setTitle("NOT YOUR PARCEL");
                builder.setCancelable(false);
                builder.setNegativeButton("확인", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.setPositiveButton("취소", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        holder.itemView.setTag(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                remove(holder.getAdapterPosition());
                return true;
            }
        });
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
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        RadioButton rightParcel;
        RadioButton wrongParcel;
        ImageView iv_profile;
        TextView tv_id;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            this.rightParcel = (RadioButton) itemView.findViewById(R.id.rightParcel);
            this.wrongParcel = (RadioButton) itemView.findViewById(R.id.wrongParcel);
        }
    }
}