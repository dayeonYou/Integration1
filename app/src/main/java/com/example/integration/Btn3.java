package com.example.integration;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Btn3 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.btn3);
        setTitle("Setting");
        Button explain = (Button) findViewById(R.id.explain);
        explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.integration.Explain.class);
                startActivity(intent);
            }
        });
        AudioManager audioManager;
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        Button set_btn2 = (Button) findViewById(R.id.set_btn2);
        set_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //토스트알람
                Toast.makeText(getApplicationContext(), "set alarm sound mode", Toast.LENGTH_SHORT).show();
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                    // 진동 모드일 경우
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                    // 무음 모드일 경우
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                //소리 알람
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),notification);
                ringtone.play();
            }
        });
        
        Button set_btn3 = (Button) findViewById(R.id.set_btn3);
        set_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "set alarm vibration mode", Toast.LENGTH_SHORT).show();
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(1000);
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    // 벨소리 모드일 경우
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
                else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                    // 무음 모드일 경우
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
            }
        });
        final Switch set_btn4 = (Switch) findViewById(R.id.switchView);
        set_btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(set_btn4.isChecked() == true)  Toast.makeText(getApplicationContext(), "set toast message on", Toast.LENGTH_SHORT).show();
                else  Toast.makeText(getApplicationContext(), "set toast message off", Toast.LENGTH_SHORT).show();
            }
        });
        TextView textView = findViewById(R.id.text);
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                    // 진동 모드일 경우
                    Toast.makeText(getApplicationContext(), "set alarm sound mode", Toast.LENGTH_SHORT).show();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                    // 무음 모드일 경우
                    Toast.makeText(getApplicationContext(), "set alarm sound mode", Toast.LENGTH_SHORT).show();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                setVolumeControlStream(seekBar.getProgress());
                textView.setText("VOLUME Setting : "+seekBar.getProgress());
                setVolumeControlStream(AudioManager.STREAM_ALARM);
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),notification);
                ringtone.play();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
}