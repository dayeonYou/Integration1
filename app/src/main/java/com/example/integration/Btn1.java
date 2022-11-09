package com.example.integration;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Btn1 extends AppCompatActivity {
    String videoUrl = "abababa";
    static FirebaseDatabase database;
    static DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.btn1);
        setTitle("CAMERA");
        MediaController mediaController = new MediaController(this);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Video/url");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videoUrl = dataSnapshot.getValue().toString();
                VideoView videoView = findViewById(R.id.videoView);

                Uri uri = Uri.parse(videoUrl);

                videoView.setVideoURI(uri);

                mediaController.setAnchorView(videoView);

                mediaController.setMediaPlayer(videoView);

                videoView.setMediaController(mediaController);

                videoView.start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });
    }
}
