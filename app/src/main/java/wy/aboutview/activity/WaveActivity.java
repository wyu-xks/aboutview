package wy.aboutview.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import wy.aboutview.R;
import wy.aboutview.views.WaveView;

public class WaveActivity extends AppCompatActivity {

    WaveView waveView;//方形
    WaveView waveCircleView;//圆形
    private int progrees = 0;//进度
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progrees == 61) return;
            Log.e("progress", progrees + "");
            waveView.setmProgress(progrees++);
            waveCircleView.setmProgress(progrees++);
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        waveView = (WaveView) findViewById(R.id.waveView);
        waveCircleView = (WaveView) findViewById(R.id.waveViewCircle);
        mHandler.sendEmptyMessageDelayed(0, 10);
    }
}
