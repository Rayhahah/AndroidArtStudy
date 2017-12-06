package com.rayhahah.androidartstudy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.rayhahah.androidartstudy.C;
import com.rayhahah.androidartstudy.util.RLog;

import java.lang.ref.WeakReference;

public class MessengerServerService extends Service {
    public MessengerServerService() {
    }

    private static class MyMessengerHandler extends Handler {
        private WeakReference<MessengerServerService> activityWeakReference;

        public MyMessengerHandler(MessengerServerService service) {
            activityWeakReference = new WeakReference<MessengerServerService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            MessengerServerService service = activityWeakReference.get();
            if (service != null) {

                switch (msg.what) {
                    case C.MESSENGER_MSG_FROM_CLIENT:
                        String strClient = msg.getData().getString("msg");
                        RLog.e("MessengerServerService receive str=" + strClient);

                        Messenger clientMessenger = msg.replyTo;
                        if (clientMessenger == null) {
                            return;
                        }
                        Message msgToClient = Message.obtain();
                        msgToClient.what = C.MESSENGER_MSG_FROM_SERVER;
                        Bundle b = new Bundle();
                        b.putString("msg", "Server Send To Client str=" + strClient);
                        msgToClient.setData(b);
                        try {
                            clientMessenger.send(msgToClient);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MyMessengerHandler(this));

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
