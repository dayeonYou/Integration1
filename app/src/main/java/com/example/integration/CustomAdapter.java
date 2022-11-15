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
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;
    private String branch;

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
            holder.rightParcel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(),"확인 해주셔서 감사합니다.",Toast.LENGTH_SHORT).show();
                }
            });
            holder.wrongParcel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(),"잘못 온 택배입니다.",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("\n자신의 것이 아닌 택배를 받은 경우\n      1. 받는 사람과 주소를 확인합니다.\n      2. 받는 사람의 전화번호로 연락해봅니다.\n      3. 택배기사에게 연락해봅니다.\n확인 버튼을 누르면 타인의 택배로 취급됩니다");
                    builder.setTitle("NOT YOUR PARCEL");
                    builder.setCancelable(false);
                    builder.setNegativeButton("확인", (DialogInterface.OnClickListener) (dialog, which) -> {
//                    String input = holder.tv_id.getText().toString(); //db 사용 안하고 저장하는 방법
//                    //인텐트 선언 및 정의
//                    Intent intent = new Intent(v.getContext(), Parcel_not.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    //입력한 input값을 intent로 전달한다.
//                    intent.putExtra("text", input);
                        //액티비티 이동
                        remove(holder.getAdapterPosition());
                        dialog.dismiss();
                        //context.startActivity(intent);
                        //v.getContext().startActivity(intent);

                        Parcel_not.writeNewUser(id,profileUrl);
                        MainActivity.deleteData(id);
                    });
                    builder.setPositiveButton("취소", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
        else if(branch == "end"){
            holder.rightParcel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(),"택배가 정상적으로 회수되었습니다.",Toast.LENGTH_SHORT).show();
                }
            });
            holder.wrongParcel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(),"택배 분실 발생!",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("\n 확인 버튼을 누르면 분실 택배로 취급됩니다. \n 서둘러 아래의 대처 절차를 따르십시오.\n");
                    builder.setTitle("Parcel missing!");
                    builder.setCancelable(false);
                    builder.setNegativeButton("확인", (DialogInterface.OnClickListener) (dialog, which) -> {
                        //remove(holder.getAdapterPosition());
                        dialog.dismiss();
//                        MainActivity.deleteData(id);
                    });
                    builder.setPositiveButton("취소", (DialogInterface.OnClickListener) (dialog, which) -> {
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
            //branch != "end"
            if(true) this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            if(branch == "home" || branch == "end"){
                this.rightParcel = (RadioButton) itemView.findViewById(R.id.rightParcel);
                this.wrongParcel = (RadioButton) itemView.findViewById(R.id.wrongParcel);
            }
        }
    }
}