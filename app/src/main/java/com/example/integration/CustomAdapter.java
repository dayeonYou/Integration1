package com.example.integration;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;
    private String branch;
    FirebaseDatabase database = FirebaseDatabase.getInstance();;
    SharedPreferences sp;
    static int saveInt;
    Context mContext;

    public CustomAdapter(ArrayList<User> arrayList, Context context,String branch) {
        this.arrayList = arrayList;
        this.context = context;
        this.branch = branch;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(branch == "home"){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }
        else if(branch == "not"){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_en, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_e, parent, false);
        }
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        //branch!="end"
        if(true){
            Glide.with(holder.itemView)
                    .load(arrayList.get(position).getProfile())
                    .into(holder.iv_profile);
        }
        holder.tv_id.setText(arrayList.get(position).getId());
        String id = arrayList.get(position).getId();
        String profileUrl = arrayList.get(position).getProfile();

        if(branch == "home"){
//            holder.rightParcel.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    Toast.makeText(v.getContext(),"?????? ???????????? ???????????????.",Toast.LENGTH_SHORT).show();
//                }
//            });
//            holder.wrongParcel.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    Toast.makeText(v.getContext(),"?????? ??? ???????????????.",Toast.LENGTH_SHORT).show();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                    builder.setMessage("\n????????? ?????? ?????? ????????? ?????? ??????\n      1. ?????? ????????? ????????? ???????????????.\n      2. ?????? ????????? ??????????????? ??????????????????.\n      3. ?????????????????? ??????????????????.\n?????? ????????? ????????? ????????? ????????? ???????????????");
//                    builder.setTitle("NOT YOUR PARCEL");
//                    builder.setCancelable(false);
//                    builder.setNegativeButton("??????", (DialogInterface.OnClickListener) (dialog, which) -> {
////                    String input = holder.tv_id.getText().toString(); //db ?????? ????????? ???????????? ??????
////                    //????????? ?????? ??? ??????
////                    Intent intent = new Intent(v.getContext(), Parcel_not.class);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    //????????? input?????? intent??? ????????????.
////                    intent.putExtra("text", input);
//                        //???????????? ??????
//                        remove(holder.getAdapterPosition());
//                        dialog.dismiss();
//                        //context.startActivity(intent);
//                        //v.getContext().startActivity(intent);
//                        // Parcel_not.writeNewUser(id,profileUrl);
//                        mContext = context;
//                        sp = mContext.getSharedPreferences("sp_N", MODE_PRIVATE);
//                        saveInt = sp.getInt("save_N", 0);
//                        writeNewUser(id,profileUrl);
//                        saveInt++;
//                        save(saveInt);
//                        MainActivity.deleteData(id);
//                    });
//                    builder.setPositiveButton("??????", (DialogInterface.OnClickListener) (dialog, which) -> {
//                        dialog.cancel();
//                    });
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                }
//            });
        }
        else if(branch == "end"){
            holder.rightParcel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(),"????????? ??????????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                }
            });
            holder.wrongParcel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(),"?????? ?????? ??????!",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("\n ?????? ????????? ????????? ?????? ????????? ???????????????. \n ????????? ????????? ?????? ????????? ???????????????.\n");
                    builder.setTitle("Parcel missing!");
                    builder.setCancelable(false);
                    builder.setNegativeButton("??????", (DialogInterface.OnClickListener) (dialog, which) -> {
                        //remove(holder.getAdapterPosition());
                        dialog.dismiss();
//                        MainActivity.deleteData(id);
                    });
                    builder.setPositiveButton("??????", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                remove(holder.getAdapterPosition());
                if(branch == "home") MainActivity.deleteData(id);
                else if(branch == "not"){
                    Parcel_not.deleteDataN(id);
                }
                else if(branch == "end"){
                    Parcel_e.deleteDataE(id);
                }
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
        // ?????? ?????????
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        RadioButton rightParcel;
        RadioButton wrongParcel;
        ImageView iv_profile;
        TextView tv_id;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            //branch != "end"
            if(true) this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_id = itemView.findViewById(R.id.tv_id);
//            if(branch == "home" || branch == "end")
            if(branch == "end"){
                this.rightParcel = (RadioButton) itemView.findViewById(R.id.rightParcel);
                this.wrongParcel = (RadioButton) itemView.findViewById(R.id.wrongParcel);
            }
        }
    }
    public void writeNewUser(String tv_id, String iv_profile) {
        String index = "nUser_0" + saveInt;
        database.getReference("N").child(index).child("id").setValue(tv_id);
        database.getReference("N").child(index).child("profile").setValue(iv_profile);
    }
    public void save(int s){
        this.mContext = context;
        sp = mContext.getSharedPreferences("sp_N",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putInt("save_N",s);
        editor.commit();
    }
}