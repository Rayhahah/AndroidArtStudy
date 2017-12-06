package com.rayhahah.androidartstudy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rayhahah.androidartstudy.util.RLog;

public class MyBindService extends Service {

    private LocalBinder mLocalBinder = new LocalBinder();

    public MyBindService() {
    }

    /**
     * 绑定对象
     */
    public class LocalBinder extends Binder {
        public MyBindService getService() {
            return MyBindService.this;
        }
    }

    /**
     * 把Binder返回给客户端（也就是Activity中的ServiceConnection）
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    /**
     * 只会触发一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        RLog.e(this.getClass().getName() + "onCreate");
    }

    /**
     * 每一次StartService都会触发一次
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RLog.e(this.getClass().getName() + "onStartCommand");

        /**
         * return START_STICKY ： 当服务因为内存不足被杀死的情况下， 当内存充足了以后，会再次尝试启动这个Service
         */
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RLog.e(this.getClass().getName() + "onDestory");

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        RLog.e(this.getClass().getName() + "onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        RLog.e(this.getClass().getName() + "onTrimMemory");
    }

    /**
     * 接触绑定的时候触发
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        RLog.e(this.getClass().getName() + "onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * 重新绑定的时候触发
     *
     * @param intent
     */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        RLog.e(this.getClass().getName() + "onRebind");
    }

}
