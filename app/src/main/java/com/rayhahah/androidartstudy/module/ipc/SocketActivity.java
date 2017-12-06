package com.rayhahah.androidartstudy.module.ipc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.service.TCPServerService;
import com.rayhahah.androidartstudy.util.RLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class SocketActivity extends AppCompatActivity {

    private Socket mTCPSocket;
    private Socket mClientSocket;
    private PrintWriter mTCPPw;
    private static final int MSG_CONNECT_TCP = 10;
    private static final int MSG_FROM_SERVER = 11;
    private boolean mIsTCPClientFinish = false;

    private MyTCPHandler mTCPHandler = new MyTCPHandler(this);

    private static class MyTCPHandler extends Handler {
        private WeakReference<SocketActivity> activityWeakReference;

        public MyTCPHandler(SocketActivity activity) {
            activityWeakReference = new WeakReference<SocketActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SocketActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_CONNECT_TCP:
                        RLog.e("Connect to TCP server success!");
                        break;
                    case MSG_FROM_SERVER:
                        String msgFromServer = (String) msg.obj;
                        RLog.e("server said=" + msgFromServer);
                        break;
                    default:
                        break;
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        startService(new Intent(this, TCPServerService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mIsTCPClientFinish = true;
    }

    public void clickToTCP(View view) {
        mIsTCPClientFinish = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mTCPSocket = new Socket("127.0.0.1", TCPServerService.TCP_SERVER_PORT);
                    mClientSocket = mTCPSocket;
                    mTCPPw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mTCPSocket.getOutputStream())));
                    mTCPHandler.sendEmptyMessage(MSG_CONNECT_TCP);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    BufferedReader mTCPBr = new BufferedReader(new InputStreamReader(mTCPSocket.getInputStream()));
                    while (!mIsTCPClientFinish) {
                        String strServer = mTCPBr.readLine();
                        if (strServer != null) {
                            Message msg = mTCPHandler.obtainMessage();
                            msg.what = MSG_FROM_SERVER;
                            msg.obj = strServer;
                            mTCPHandler.sendMessage(msg);
                        }
                    }
                    mTCPPw.close();
                    mTCPBr.close();
                    mTCPSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void clickToTellTCP(View view) {
        mTCPPw.println("I am Client!!");
        mTCPPw.flush();
    }

    public void clickToReleaseTCP(View view) {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mIsTCPClientFinish = true;
    }
}
