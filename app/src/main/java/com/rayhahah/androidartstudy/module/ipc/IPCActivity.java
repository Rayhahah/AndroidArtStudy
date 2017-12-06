package com.rayhahah.androidartstudy.module.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.C;
import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.aidl.Book;
import com.rayhahah.androidartstudy.aidl.IBookManager;
import com.rayhahah.androidartstudy.aidl.IComputeCenter;
import com.rayhahah.androidartstudy.aidl.INewBookArrivedListener;
import com.rayhahah.androidartstudy.aidl.ISecurityCenter;
import com.rayhahah.androidartstudy.service.MessengerServerService;
import com.rayhahah.androidartstudy.service.MyAIDLServerService;
import com.rayhahah.androidartstudy.util.BinderPool;
import com.rayhahah.androidartstudy.util.RLog;

import java.lang.ref.WeakReference;
import java.util.List;

public class IPCActivity extends AppCompatActivity {

    private Intent mMessengerService;
    private ServiceConnection mMessengerConn;
    private Messenger mMessenger;
    private Intent mAIDLService;
    private ServiceConnection mAIDLConn;
    private IBookManager mIBookManager;
    private BinderPool mBinderPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBinderPool = BinderPool.getInstance(IPCActivity.this);
            }
        }).start();
    }

    public void clickToSocket(View view) {
        startActivity(new Intent(this, SocketActivity.class));
    }

    private static class MyMessengerHandler extends Handler {
        private WeakReference<IPCActivity> activityWeakReference;

        public MyMessengerHandler(IPCActivity activity) {
            activityWeakReference = new WeakReference<IPCActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            IPCActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    //接受到不同进程服务端的消息
                    case C.MESSENGER_MSG_FROM_SERVER:
                        RLog.e("Client Receive Msg=" + msg.getData().getString("msg"));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private MyMessengerHandler mMessengerHandler = new MyMessengerHandler(this);

    private Messenger mGetReplyMessenger = new Messenger(mMessengerHandler);

    public void clickToSendToMessenger(View view) {
        if (mMessenger != null) {
            Message msg = Message.obtain();
            msg.what = C.MESSENGER_MSG_FROM_CLIENT;
            Bundle bundle = new Bundle();
            bundle.putString("msg", "Send Client Message");
            msg.setData(bundle);
            //传递给服务端用于回应消息的Messenger
            msg.replyTo = mGetReplyMessenger;
            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickToUnBindMessenger(View view) {
        if (mMessengerConn != null) {
            unbindService(mMessengerConn);
            mMessengerConn = null;
        }
    }

    public void clickToBindMessenger(View view) {
        mMessengerService = new Intent(this, MessengerServerService.class);
        mMessengerConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMessenger = new Messenger(service);
                Message msg = Message.obtain();
                msg.what = C.MESSENGER_MSG_FROM_CLIENT;
                Bundle bundle = new Bundle();
                bundle.putString("msg", "Client Connect Success!!!");
                msg.setData(bundle);
                //传递给服务端用于回应消息的Messenger
                msg.replyTo = mGetReplyMessenger;
                try {
                    mMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mMessenger = null;
                mMessengerConn = null;
            }
        };
        bindService(mMessengerService, mMessengerConn, Context.BIND_AUTO_CREATE);
    }


    private static class MyAIDLHandler extends Handler {
        private WeakReference<IPCActivity> activityWeakReference;

        public MyAIDLHandler(IPCActivity activity) {
            activityWeakReference = new WeakReference<IPCActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            IPCActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case C.MSG_RECEIVED_NEW_BOOK:
                        Book book = (Book) msg.getData().getParcelable("book");
                        RLog.e(book.toString());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * ------------------------------------------------分割线-------------------------------------------------------------------------------
     */

    private MyAIDLHandler mAIDLHandler = new MyAIDLHandler(this);

    public void clickToUnBindAIDL(View view) {
        if (mAIDLConn != null) {
            try {
                mIBookManager.unregisterListener(mArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(mAIDLConn);
            mAIDLConn = null;
            mIBookManager = null;
        }
    }

    private INewBookArrivedListener mArrivedListener = new INewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Message msg = Message.obtain();
            msg.what = C.MSG_RECEIVED_NEW_BOOK;
            Bundle data = new Bundle();
            data.putParcelable("book", book);
            msg.setData(data);
            mAIDLHandler.sendMessage(msg);
        }
    };

    public void clickToBindAIDL(View view) {
        Looper.loop();
        mAIDLHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        mAIDLService = new Intent(this, MyAIDLServerService.class);
        mAIDLConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (service == null) {
                    RLog.e("null");
                }

                mIBookManager = IBookManager.Stub.asInterface(service);
                try {
                    mIBookManager.registerListener(mArrivedListener);
                    List<Book> bookList = mIBookManager.getBookList();
                    RLog.e(bookList);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                RLog.e("disconnect aidl service");
            }
        };
        bindService(mAIDLService, mAIDLConn, Context.BIND_AUTO_CREATE);
    }

    public void clickToGetAIDL(View view) {
        if (mIBookManager != null) {
            try {
                RLog.e(mIBookManager.getBookList());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickToAddToAIDL(View view) {
        if (mIBookManager != null) {
            Book book = new Book();
            book.bookName = "book3";
            book.bookPrice = 30;
            try {
                mIBookManager.addBook(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------------------------------------
     */


    public void clickToBinderPoolSecurity(View view) {
        IBinder securityBinder = mBinderPool.queryBinder(BinderPool.BINDER_SECURITY);
        ISecurityCenter securityCenter = ISecurityCenter.Stub.asInterface(securityBinder);
        try {
            RLog.e(securityCenter.decrypt("password"));
            RLog.e(securityCenter.encrypt("content"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void clickToBinderPoolCompute(View view) {
        IBinder computeBinder = mBinderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        IComputeCenter computeCenter = IComputeCenter.Stub.asInterface(computeBinder);
        try {
            RLog.e(computeCenter.add(10, 20));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
