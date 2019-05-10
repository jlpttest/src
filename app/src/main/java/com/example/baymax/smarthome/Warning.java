package com.example.baymax.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
public class Warning extends AppCompatActivity {
    private boolean binded = false;
    private MyService myService;
    ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            myService = binder.getService();
            binded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binded = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!isMyServiceRunning(MyService.class)) {
            startService();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (binded) {
            this.unbindService(mConnection);
            binded = false;
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void startService() {
        Intent intent = new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
}
