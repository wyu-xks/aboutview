package wy.aboutview.activity;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import wy.aboutview.R;
import wy.aboutview.views.MultipleCircleView;

public class MultipleActivity extends AppCompatActivity {

    private MultipleCircleView multipleCircleView;
    int[] data = new int[4];
    int noise = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple);
        multipleCircleView = (MultipleCircleView) findViewById(R.id.multipleview);
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    data[0] = (int) (Math.random() * 100);
                    data[1] = (int) (Math.random() * 100);
                    data[2] = (int) (Math.random() * 100);
                    data[3] = (int) (Math.random() * 100);
                    noise = (int) (Math.random() * 100) / 50;
                    SystemClock.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            multipleCircleView.setData(data, noise);
                        }
                    });
                }

            }
        }).start();
    }


}
