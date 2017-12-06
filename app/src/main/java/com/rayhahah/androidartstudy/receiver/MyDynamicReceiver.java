package com.rayhahah.androidartstudy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rayhahah.androidartstudy.util.RLog;

public class MyDynamicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        RLog.e("MyDynamicReceiver receive action="+action);
    }


}
