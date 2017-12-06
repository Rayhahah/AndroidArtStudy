package com.rayhahah.androidartstudy.module.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.service.MyBindService;
import com.rayhahah.androidartstudy.service.StudyService;
import com.rayhahah.androidartstudy.util.RLog;

public class ServiceActivity extends AppCompatActivity {

    private ServiceConnection mConn;
    private Intent mStartService;
    private Intent mBindService;
    private MyBindService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public void startService(View view) {
        //启动服务
        mStartService = new Intent(this, StudyService.class);
        startService(mStartService);
    }

    /**
     * 停止服务
     */
    public void stopService(View view) {
        if (mStartService != null) {
            stopService(mStartService);
        }
    }


    public void bindService(View view) {
        mConn = new ServiceConnection() {
            /**
             * 与服务端交互的接口方法，在绑定Service成功的时候回调
             * 这个方法会获取绑定Service.onBind() 返回的IBinder对象，通过这个对象，实现Activity和Service的交互
             * @param name
             * @param service
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                RLog.e("onServiceConnected,ComponentName=" + name);
                MyBindService.LocalBinder binder = (MyBindService.LocalBinder) service;
                mService = binder.getService();
                RLog.e(mService.getClass().getName());
            }

            /**
             * 取消绑定的时候调用，但是正常情况下不会调用
             * 它的调用时机是当Service被意外销毁的时候 ： 例如当内存不足的时候Service被干掉了
             * @param name
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                RLog.e("onServiceDisconnected,ComponentName=" + name);
                mService = null;

            }

            @Override
            public void onBindingDied(ComponentName name) {
                RLog.e("onBindingDied,ComponentName=" + name);
            }
        };
        mBindService = new Intent(this, MyBindService.class);
        bindService(mBindService, mConn, Service.BIND_AUTO_CREATE);
    }

    public void unbindService(View view) {
        if (mConn != null) {
            mService = null;
            unbindService(mConn);
        }
    }
}
