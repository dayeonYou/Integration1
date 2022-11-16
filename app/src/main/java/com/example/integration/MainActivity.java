package com.example.integration;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import java.lang.*;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    static FirebaseDatabase database;
    static DatabaseReference databaseReference;
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";
    SharedPreferences sp;
    SharedPreferences sp_w;
    int size_array = 0;
    int flag_receive = 0;
    static int saveInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ONMAJU");
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        Button btnE = (Button) findViewById(R.id.parcelEBtn);
        Button btnN = (Button) findViewById(R.id.parcelNBtn);

        EditText editText_name= (EditText) findViewById(R.id.ed_name);
        EditText editText_ad = (EditText) findViewById(R.id.ed_ad);

        editText_name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do stuff
                String info_name = editText_name.getText().toString();
                String info_ad = editText_ad.getText().toString();
                writeUserInfo(info_name, info_ad);
            }
        });
        editText_ad.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do stuff
                String info_name = editText_name.getText().toString();
                String info_ad = editText_ad.getText().toString();
                writeUserInfo(info_name, info_ad);
            }
        });
        sp_w = getSharedPreferences("sp_wInt", MODE_PRIVATE);
        saveInt = sp_w.getInt("sp_wInt", 0);
        
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnE.setEnabled(true);
            }
        }, 1000);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnN.setEnabled(true);
            }
        }, 1000);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Btn1.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Btn2.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Btn3.class);
                startActivity(intent);

            }
        });

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Parcel_e.class);
                startActivity(intent);

            }
        });

        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Parcel_not.class);
                startActivity(intent);

            }
        });

        if(false == isConnected()) {
            Toast.makeText(this,"not connected",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (getNetworkType()){
            case ConnectivityManager.TYPE_WIFI:
                Toast.makeText(this,"connected to WI-FI",Toast.LENGTH_SHORT).show();
                HttpMgrTread httpThread = new HttpMgrTread();
                httpThread.start();
                break;
            case ConnectivityManager.TYPE_MOBILE:
                Toast.makeText(this,"connected to MOBILE",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);

        //받은 택배 있음
        rv.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerView); // 아디 연결

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("User"); // DB 테이블 연결
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sp = getSharedPreferences("size_arraylist", MODE_PRIVATE);
                size_array = sp.getInt("size_arraylist", 0);
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    assert user != null;
                    String id = user.getId();
                    String profile = user.getProfile();
                    if(Objects.equals(id, "exist")){
                        break;
                    }
                    arrayList.add(user);// 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    if(user.getReceive() != null) { //택배 회수됨
                        writeNewUser(id,profile);
                        save(saveInt,2);
                        deleteData(user.getId());
                        //push 알림
                        flag_receive = 1;
                    }
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                if(size_array < arrayList.size()){ //택배 추가됨
                    scheduleNotification(getNotification(1));
                }
                else if((size_array > arrayList.size()) && (flag_receive == 1)){
                    scheduleNotification(getNotification(2));
                    flag_receive = 0;
                }
                save(arrayList.size(),1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new CustomAdapter(arrayList, this,"home");
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        if(false){ //받은 택배 없음
            rv.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scheduleNotification(Notification notification){
        int requestID = (int) System.currentTimeMillis();

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID,1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION,notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,requestID,notificationIntent,PendingIntent.FLAG_MUTABLE);

        long futureMillis = SystemClock.elapsedRealtime();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,futureMillis,pendingIntent);

    }

    private Notification getNotification(int num){
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,default_notification_id);;
        switch (num){
            case 1:
                builder.setContentText("새로운 택배가 도착했습니다!");
                break;
            case 2:
                builder.setContentText("택배가 회수되었습니다!");
                break;
            default:
                builder.setContentText("default");
                break;
        }
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("택배 정보 갱신!");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        return builder.build();
    }

    @Override
    public void onBackPressed() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);
        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
        });
        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetWork != null) && (activeNetWork.isConnectedOrConnecting());
        return isConnected;
    }
    private int getNetworkType(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.getType();
    }
    static void deleteData(String tv_id) {

        Query query = databaseReference.orderByChild("id").equalTo(tv_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for(DataSnapshot dataSnapshot1 : datasnapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void save(int s, int num){
        sp = getSharedPreferences("size_arraylist",MODE_PRIVATE);
        sp_w = getSharedPreferences("sp_wInt",MODE_PRIVATE);
        SharedPreferences.Editor editor;
        if(num == 1){
            editor = sp.edit();
            editor.clear();
            editor.putInt("size_arraylist",s);
        }
        else{
            editor = sp_w.edit();
            editor.clear();
            editor.putInt("sp_wInt",s);
        }
        editor.commit();

    }
    public void writeNewUser(String tv_id, String iv_profile) {
        String index = "eUser_0" + saveInt;
        database.getReference("E").child(index).child("id").setValue(tv_id);
        database.getReference("E").child(index).child("profile").setValue(iv_profile);
        saveInt++;
    }
    public void writeUserInfo(String name, String address) {
        database = FirebaseDatabase.getInstance();
        database.getReference("Info").child("name").setValue(name);
        database.getReference("Info").child("ad").setValue(address);
    }
}