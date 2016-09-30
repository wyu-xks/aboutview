package wy.aboutview.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import wy.aboutview.R;
import wy.aboutview.bean.Histogram;
import wy.aboutview.views.ChartView;

public class HistogramActivity extends FragmentActivity {

    private ArrayList<Histogram> histogramArrayList = new ArrayList<>();
    private ChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        chartView = (ChartView) findViewById(R.id.chartview);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 18; i++) {
            int type = i % 6;
            if(type == 0){
                type = 4;
            }
            histogramArrayList.add(new Histogram(type, 1.0f / 18));
        }

        chartView.setDataList(histogramArrayList);
    }
}
