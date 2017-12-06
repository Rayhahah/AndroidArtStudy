package com.rayhahah.androidartstudy.module.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rayhahah.androidartstudy.R;

import java.lang.ref.WeakReference;

public class HandlerActivity extends AppCompatActivity {

    private TextView mTest;

    /**
     * 标准写法，弱引用
     */
    private static class MyHandler extends Handler {
        private WeakReference<HandlerActivity> activityWeakReference;

        public MyHandler(HandlerActivity activity) {
            activityWeakReference = new WeakReference<HandlerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerActivity activity = activityWeakReference.get();
            if (activity != null) {

            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        mTest = ((TextView) findViewById(R.id.tv_test));


        HandlerThread handlerThread = new HandlerThread("test");
        Handler threadHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        handlerThread.start();
        new Thread(new ThreadRunable()).start();
        //postInvalidate,内部也是使用handler来做消息转发到主线程再调用invalidate
//        mTest.postInvalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class ThreadRunable implements Runnable {

        @Override
        public void run() {
            Looper.prepare();
            Looper.loop();
        }
    }

    public void handlerPost(View view) {
    }

    public void handlerSendMessage(View view) {
    }
}
