package com.rayhahah.androidartstudy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.rayhahah.androidartstudy.util.RLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerService extends Service {
    public static final int TCP_SERVER_PORT = 8999;
    private boolean mIsServiceDestory = false;

    public TCPServerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TcpServer()).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestory = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(TCP_SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            while (!mIsServiceDestory) {
                try {
                    final Socket client = serverSocket.accept();
                    RLog.e(client);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 监听到客户端连接就相应客户端
     *
     * @param client
     */
    private void responseClient(Socket client) throws IOException {
        //获取输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //获取输出流
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
        pw.println("欢迎来到聊天室");
        while (!mIsServiceDestory) {
            String strClient = br.readLine();
            RLog.e("client said=" + strClient);
            if (strClient == null) {
                //客户端断开连接
                break;
            }
            pw.println("server said= fuck!");
            pw.flush();
        }
        //断开连接
        pw.close();
        br.close();
        client.close();
    }
}
