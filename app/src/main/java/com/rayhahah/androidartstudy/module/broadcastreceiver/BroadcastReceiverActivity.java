package com.rayhahah.androidartstudy.module.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.receiver.MyDynamicReceiver;
import com.rayhahah.androidartstudy.util.RLog;

public class BroadcastReceiverActivity extends AppCompatActivity {

    private MyDynamicReceiver mDynamicReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;
    private IntentFilter mLocalFilter;
    private MyLocalReceiver mLocalReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_receiver);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    public void sendBroadcastHello(View view) {
        Intent hello = new Intent("hello");
        sendBroadcast(hello);
    }

    public void sendOrderBroadcastHello(View view) {
        sendOrderedBroadcast(new Intent("helloOrder"), "");
    }

    public void registerReceiver(View view) {
        mDynamicReceiver = new MyDynamicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("hello");
        registerReceiver(mDynamicReceiver, filter);
    }

    public void unregisterReceiver(View view) {
        if (mDynamicReceiver != null) {
            unregisterReceiver(mDynamicReceiver);
        }
    }


    public void unregisterLocalReceiver(View view) {
        if (mLocalReceiver != null) {
            mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
        }
    }

    public void registerLocalReceiver(View view) {
        mLocalFilter = new IntentFilter("local");
        mLocalReceiver = new MyLocalReceiver();
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, mLocalFilter);
    }

    public void sendLocalBroadcast(View view) {
        mLocalBroadcastManager.sendBroadcast(new Intent("local"));
    }

    class MyLocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            RLog.e("MyLocalReceiver receive action=" + intent.getAction());
        }
    }

}
