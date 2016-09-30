package wy.aboutview.activity;

import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wy.aboutview.R;
import wy.aboutview.views.RoundProgressBar;

public class CircleActivity extends FragmentActivity {

    private RoundProgressBar roundProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        roundProgressBar = (RoundProgressBar) findViewById(R.id.circleprogress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < 90; i++) {
                    SystemClock.sleep(100);
                    roundProgressBar.setProgress(i);
                }
            }
        }).start();
    }
}
