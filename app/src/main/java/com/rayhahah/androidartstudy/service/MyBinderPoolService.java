package com.rayhahah.androidartstudy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.rayhahah.androidartstudy.util.BinderPool;

/**
 * 线程池机制的Service
 */
public class MyBinderPoolService extends Service {

    public MyBinderPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }

    private BinderPool.BinderPoolImpl mBinderPool = new BinderPool.BinderPoolImpl();
}
