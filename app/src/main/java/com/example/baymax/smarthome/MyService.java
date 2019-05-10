package com.example.baymax.smarthome;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    DatabaseReference myData;
    private static final String CHANNEL_ID = "smart_home";
    private AlarmManager mAlarmManager;
    public class LocalBinder extends Binder {

        MyService getService() {
            return MyService.this;
        }
    }

    private final IBinder binder = new LocalBinder();

    private boolean isFirst = true;

    @Override
    public void onCreate() {
        super.onCreate();


        myData = FirebaseDatabase.getInstance().getReference("RFID_STATUS");
        myData.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data=dataSnapshot.getValue().toString();
                if (!isFirst && data != null && data.contains("sai the")) {
                    alert();
                }
                if (isFirst) isFirst = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        Query query4 = FirebaseDatabase.getInstance().getReference("RFID_STATUS").limitToLast(1);
//        query4.addListenerForSingleValueEvent(valueEventListener);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }
//    ValueEventListener valueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            dt.clear();
//            if (dataSnapshot.exists()) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String artist = snapshot.getValue(String.class);
//                    dt.add(artist);
//                }
//
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Home")
                .setContentText("Smart home")
                .setTicker("Home")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setChannelId(CHANNEL_ID)
                .build();

        createNotificationChannel();

        startForeground(10001, notification);

        return START_NOT_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Home";
            String description = "Smart home";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void alert() {
        Intent intent = new Intent(this, AlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, 100, pendingIntent);
    }

}
