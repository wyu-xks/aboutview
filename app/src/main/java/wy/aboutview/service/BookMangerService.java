package wy.aboutview.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import wy.aboutview.aidl.Book;
import wy.aboutview.aidl.IBookManger;
import wy.aboutview.aidl.IOnNewBookArrivedListener;

public class BookMangerService extends Service {

    public BookMangerService() {
    }

    private CopyOnWriteArrayList<Book> mBookLists = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerLists = new RemoteCallbackList<>();

    private AtomicBoolean mIsServiceDeath = new AtomicBoolean(false);

    private Binder mBinder = new IBookManger.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookLists;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            if (!mBookLists.contains(book)) {
                mBookLists.add(book);
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerLists.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerLists.unregister(listener);
            //RemoteCallbackList 的 beginBroadcast() 与 finishBroadcast() 需配套使用
            Log.e("BMS","mListenerLists size : " + mListenerLists.beginBroadcast());
            mListenerLists.finishBroadcast();

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookLists.add(new Book(1, "android"));
        mBookLists.add(new Book(2, "ios"));
        new Thread(new ServiceRunnable()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDeath.set(true);
        super.onDestroy();
    }

    private class ServiceRunnable implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDeath.get()) {
                try {
                    Thread.sleep(5000);
                    int size = mBookLists.size() + 1;
                    Book book = new Book(size, "new book # " + size);
                    mBookLists.add(book);
                    notifiClient(book);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void notifiClient(Book book) {
        //RemoteCallbackList 的 beginBroadcast() 与 finishBroadcast() 需配套使用
        int n = mListenerLists.beginBroadcast();
        for (int i = 0; i < n; i++) {
            IOnNewBookArrivedListener listener = mListenerLists.getBroadcastItem(i);
            try {
                if (listener != null) {
                    listener.onNewBookArrived(book);
                    Log.e("BookMangerService", "noticfi client : " + book.name);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mListenerLists.finishBroadcast();
    }
}
