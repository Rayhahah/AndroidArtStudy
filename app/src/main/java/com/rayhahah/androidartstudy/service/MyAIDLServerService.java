package com.rayhahah.androidartstudy.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.rayhahah.androidartstudy.aidl.Book;
import com.rayhahah.androidartstudy.aidl.IBookManager;
import com.rayhahah.androidartstudy.aidl.INewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MyAIDLServerService extends Service {

    public MyAIDLServerService() {
    }


    private CopyOnWriteArrayList<Book> mBooksList = new CopyOnWriteArrayList<>();

    //    这个是无法实现解除注册的，因为listner在进程通信中找不到了，需要使用RemoteCallbackList
//    private CopyOnWriteArrayList<INewBookArrivedListener> mBookArrivedListeners = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<INewBookArrivedListener> mBookArrivedListeners = new RemoteCallbackList<>();

    /**
     * 线程安全
     */
    private AtomicBoolean mIsServiceDestory = new AtomicBoolean(false);
    private AtomicInteger mBookCount = new AtomicInteger(5);


    private IBookManagerService mBinder = new IBookManagerService();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestory.set(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Book b1 = new Book();
        b1.bookName = "book1";
        b1.bookPrice = 10;
        mBooksList.add(b1);
        Book b2 = new Book();
        b2.bookName = "book2";
        b2.bookPrice = 20;
        mBooksList.add(b2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mIsServiceDestory.get()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Book e = new Book();
                    e.bookName = "book" + mBookCount.get();
                    e.bookPrice = mBookCount.get() * 10;
                    mBooksList.add(e);
                    mBookCount.set(mBookCount.get() + 1);

                    /**
                     * 一个beginBroadcast要对应一个finishBroadcast，否则会报错
                     */
                    int N = mBookArrivedListeners.beginBroadcast();
                    for (int i = 0; i < N; i++) {
                        INewBookArrivedListener iNewBookArrivedListener = mBookArrivedListeners.getBroadcastItem(i);
                        try {
                            if (iNewBookArrivedListener != null) {
                                iNewBookArrivedListener.onNewBookArrived(e);
                            }
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                    /**
                     * 必须要使用finishBroadcast来结束遍历
                     */
                    mBookArrivedListeners.finishBroadcast();
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        /**
         * 权限验证
         */
        int check = checkCallingOrSelfPermission("com.rayhahah.androidartstudy.permission.ACCESS_BOOK_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mBinder;
    }

    public class IBookManagerService extends IBookManager.Stub {

        @Override
        public String getMessage() throws RemoteException {
            return "hello";
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooksList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooksList.add(book);
        }

        @Override
        public void registerListener(INewBookArrivedListener listener) throws RemoteException {
            mBookArrivedListeners.register(listener);
        }

        @Override
        public void unregisterListener(INewBookArrivedListener listener) throws RemoteException {
            mBookArrivedListeners.unregister(listener);

        }
    }
}
