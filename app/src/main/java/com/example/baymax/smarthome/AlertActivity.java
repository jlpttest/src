package com.example.baymax.smarthome;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        Uri alarmRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final MediaPlayer alarmPlayer = new MediaPlayer();
        try {
            alarmPlayer.setDataSource(this, alarmRingtoneUri);
            alarmPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            alarmPlayer.setLooping(true);
            alarmPlayer.prepare();
            alarmPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.btnStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmPlayer.stop();
                alarmPlayer.release();
                finish();
            }
        });
    }
}
