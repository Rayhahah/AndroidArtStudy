package com.rayhahah.androidartstudy.module.remoteview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;

import com.rayhahah.androidartstudy.R;

public class RemoteViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_view);
    }

    /**
     * 自定义通知栏
     *
     * @param view
     */
    public void customNotification(View view) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remote_view);
        remoteViews.setTextViewText(R.id.tv_msg, "你好");
        remoteViews.setImageViewResource(R.id.iv_icon, R.drawable.cover);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, RemoteViewActivity.class), PendingIntent.FLAG_ONE_SHOT);
        remoteViews.setOnClickPendingIntent(R.id.ll_remote_notification, pendingIntent);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("title")
                .setContentText("Content Text")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContent(remoteViews)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    /**
     * 自定义桌面小组件
     *
     * @param view
     */
    public void appWidget(View view) {

    }
}
