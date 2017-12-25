package com.rayhahah.androidartstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.module.broadcastreceiver.BroadcastReceiverActivity;
import com.rayhahah.androidartstudy.module.contentprovider.ProviderActivity;
import com.rayhahah.androidartstudy.module.framework.FrameWorkActivity;
import com.rayhahah.androidartstudy.module.ipc.IPCActivity;
import com.rayhahah.androidartstudy.module.service.ServiceActivity;
import com.rayhahah.androidartstudy.module.textview.TextViewActivity;
import com.rayhahah.androidartstudy.module.view.ViewActivity;
import com.rayhahah.androidartstudy.module.webview.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 跳转到View的学习
     *
     * @param view
     */
    public void clickToView(View view) {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    public void clickToServie(View view) {
        startActivity(new Intent(this, ServiceActivity.class));
    }

    public void clickToWebview(View view) {
        startActivity(new Intent(this, WebViewActivity.class));

    }

    public void clickToBroadcastReceiver(View view) {
        startActivity(new Intent(this, BroadcastReceiverActivity.class));
    }

    public void clickToIPC(View view) {
        startActivity(new Intent(this, IPCActivity.class));
    }

    public void clickToContentProvider(View view) {
        startActivity(new Intent(this, ProviderActivity.class));
    }

    public void clickToTextView(View view) {
        startActivity(new Intent(this, TextViewActivity.class));
    }

    public void clickToFrameWork(View view) {
        startActivity(new Intent(this, FrameWorkActivity.class));
    }
}
