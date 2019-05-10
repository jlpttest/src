package com.example.baymax.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startIntent = new Intent(context, AlertActivity.class);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(startIntent);
    }
}
