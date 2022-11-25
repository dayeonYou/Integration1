package com.example.integration;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import java.lang.*;
import java.util.Objects;
import static com.example.integration.App.CHANNEL_1_ID;
import static com.example.integration.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    static FirebaseDatabase database;
    static DatabaseReference databaseReference;
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    private NotificationHelper mNotificationHelper;
    private NotificationManagerCompat notificationManager;
    SharedPreferences sp;
    SharedPreferences sp_w;
    int size_array = 0;
    int flag_receive = 0;
    int flag_not = 0;
    static int saveInt;
    String info_name;
    String info_ad;
    String id, profile;
    private ArrayList<String> listN;
    private ArrayList<String> listE;
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

        mNotificationHelper = new NotificationHelper(this);

        notificationManager = NotificationManagerCompat.from(this);

        sp_w = getSharedPreferences("sp_wInt", MODE_PRIVATE);
        saveInt = sp_w.getInt("sp_wInt", 0);
        editText_name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do stuff
                info_name = editText_name.getText().toString();
                writeUserName(info_name);
            }
        });
        editText_ad.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do stuff
                info_ad = editText_ad.getText().toString();
                writeUserAd(info_ad);
            }
        });


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Info/name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                info_name = dataSnapshot.getValue().toString();
                editText_name.setText(info_name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });
        databaseReference = database.getReference("Info/ad");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                info_ad = dataSnapshot.getValue().toString();
                editText_ad.setText(info_ad);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });
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
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sp = getSharedPreferences("size_arraylist", MODE_PRIVATE);
                size_array = sp.getInt("size_arraylist", 0);
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    assert user != null;
                    id = user.getId();
                    profile = user.getProfile();
                    if(Objects.equals(id, "exist")){
                        break;
                    }
                    arrayList.add(user);// 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    if((info_name!=null) && (info_ad!=null)){
                        boolean Con_name = id.contains(info_name);
                        boolean Con_ad = id.contains(info_ad);
                        boolean Con = Con_ad & Con_name;
                        if(!Con){
                            //타인의 택배 --> 홈화면 띄우기 --> 푸시알림 --> 확인버튼 --> 타인의 택배함으로
                            writeNewUser(user.getId(),user.getProfile(),2);
                            deleteData(user.getId());
                            //scheduleNotification(getNotification(3));
                            sendOnChannel2_1("타인의 택배 도착!","타인의 택배로 추정됩니다.",3);
                            arrayList.remove(arrayList.size()-1);
                            flag_not = 1;
                        }
                    }
                    if(user.getReceive() != null) { //택배 회수됨
                        writeNewUser(user.getId(),user.getProfile(),1);
                        deleteData(user.getId());
                        //push 알림
                        flag_receive = 1;
                        arrayList.remove(arrayList.size()-1);
                    }
                }
                save(saveInt,2);
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

                if((size_array < arrayList.size())){ //택배 추가됨, 맞는 택배
                    sendOnChannel2_1("택배 도착!","택배가 도착했습니다.",1);
                    // scheduleNotification(getNotification(1));
                }
                else if((size_array > arrayList.size()) && (flag_receive == 1)){
                    //scheduleNotification(getNotification(2));
                    sendOnChannel2_1("택배 회수!","택배가 회수되었습니다.",2);
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
    public void writeNewUser(String tv_id, String iv_profile, int num) {

        if(num == 1){
            String index = "eUser_0" + saveInt;
            database.getReference("E").child(index).child("id").setValue(tv_id);
            database.getReference("E").child(index).child("profile").setValue(iv_profile);

        }
        else{
            String index = "nUser_0" + saveInt;
            database.getReference("N").child(index).child("id").setValue(tv_id);
            database.getReference("N").child(index).child("profile").setValue(iv_profile);
        }
        saveInt++;
    }
    public void writeUserName(String name) {
        database.getReference("Info").child("name").setValue(name);
    }
    public void writeUserAd(String address) {
        database.getReference("Info").child("ad").setValue(address);
    }

    public void sendOnChannel1(String title, String message) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(title, message);
        mNotificationHelper.getManager().notify(1,nb.build());
    }

    public void sendOnChannel2(String title, String message) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel2Notification(title, message);
        mNotificationHelper.getManager().notify(2,nb.build());
    }

    public void sendOnChannel3(String title, String message) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel3Notification(title, message);
        mNotificationHelper.getManager().notify(3,nb.build());
    }
    public void sendOnChannel2_1(String passTitle, String passMessage, int num) {
        String title = passTitle;
        String message = passMessage;
        int requestID = (int) System.currentTimeMillis();
        int icon;
        Intent activityIntent;
        switch(num){
            case 2:
                activityIntent = new Intent(this, Parcel_e.class);
                //icon = R.drawable.ic_two;
                break;
            case 3:
                activityIntent = new Intent(this, Parcel_not.class);
                //icon = R.drawable.ic_three;
                break;
            default:
                activityIntent = new Intent(this, MainActivity.class);
                //icon = R.drawable.ic_one;
                break;
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this,requestID,activityIntent,PendingIntent.FLAG_MUTABLE);
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage",message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,requestID,broadcastIntent,PendingIntent.FLAG_MUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .addAction(R.mipmap.ic_launcher,"확인",actionIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(1, notification);
    }
}