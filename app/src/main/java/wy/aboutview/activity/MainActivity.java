package wy.aboutview.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import wy.aboutview.aidl.Book;
import wy.aboutview.aidl.IBookManger;
import wy.aboutview.R;
import wy.aboutview.aidl.IOnNewBookArrivedListener;
import wy.aboutview.okhttp.OkHttpHelper;
import wy.aboutview.service.BookMangerService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MESSAGE_ARRIVE = 1;
    private TextView pro;
    private TextView histogram;
    private TextView selection;
    private TextView edittext;
    private TextView recylerview;
    private TextView multipleview;
    private TextView toolbar;
    private TextView alarmview;
    private TextView waveview;
    private TextView supertextview;
    private TextView coordinatorLayout;
    private OkHttpHelper instance;
    private IBookManger iBookManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pro = (TextView) findViewById(R.id.circleprogress);
        histogram = (TextView) findViewById(R.id.histogram);
        selection = (TextView) findViewById(R.id.selection);
        edittext = (TextView) findViewById(R.id.edittext);
        recylerview = (TextView) findViewById(R.id.recylerview);
        multipleview = (TextView) findViewById(R.id.multipleview);
        toolbar = (TextView) findViewById(R.id.toolbar);
        alarmview = (TextView) findViewById(R.id.alarmview);
        waveview = (TextView) findViewById(R.id.waveview);
        supertextview = (TextView) findViewById(R.id.supertextview);
        coordinatorLayout = (TextView) findViewById(R.id.coordinatorLayout);

        pro.setOnClickListener(this);
        selection.setOnClickListener(this);
        histogram.setOnClickListener(this);
        edittext.setOnClickListener(this);
        recylerview.setOnClickListener(this);
        multipleview.setOnClickListener(this);
        toolbar.setOnClickListener(this);
        alarmview.setOnClickListener(this);
        waveview.setOnClickListener(this);
        supertextview.setOnClickListener(this);
        coordinatorLayout.setOnClickListener(this);

        Intent intent = new Intent(this, BookMangerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);


    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            iBookManger = IBookManger.Stub.asInterface(service);
            try {
                List<Book> bookList = iBookManger.getBookList();
                Log.e(TAG, "bookList : " + bookList.toString());
                bookList.add(new Book(2, "java"));
                Log.e(TAG, "bookList : " + bookList.toString());
                iBookManger.registerListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circleprogress:
                startActivity(new Intent(MainActivity.this, CircleActivity.class));
                break;
            case R.id.histogram:
                startActivity(new Intent(MainActivity.this, HistogramActivity.class));
                break;
            case R.id.selection:
                startActivity(new Intent(MainActivity.this, SelectActivity.class));
                break;
            case R.id.edittext:
                startActivity(new Intent(MainActivity.this, TextActivity.class));
                break;
            case R.id.recylerview:
                startActivity(new Intent(MainActivity.this, RecycleActivity.class));
                break;
            case R.id.multipleview:
                startActivity(new Intent(MainActivity.this, MultipleActivity.class));
                break;
            case R.id.toolbar:
                startActivity(new Intent(MainActivity.this, ToolbarActivity.class));
                break;
            case R.id.alarmview:
                startActivity(new Intent(MainActivity.this, AlarmClockActivity.class));
                break;
            case R.id.waveview:
                startActivity(new Intent(MainActivity.this, WaveActivity.class));
                break;
            case R.id.supertextview:
                startActivity(new Intent(MainActivity.this, SuperActivity.class));
                break;
            case R.id.coordinatorLayout:
                startActivity(new Intent(MainActivity.this, CoordinatorActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (null != iBookManger && iBookManger.asBinder().isBinderAlive()) {
            try {
                iBookManger.unregisterListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_ARRIVE:
                    Book book = (Book) msg.obj;
                    Log.e(TAG, "cliet receive : " + book.name);
                    break;
            }
        }
    };

    private IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            handler.obtainMessage(MESSAGE_ARRIVE, book).sendToTarget();
        }

    };

}
