package wy.aboutview.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import wy.aboutview.R;
import wy.aboutview.views.AlarmClockView;
import wy.aboutview.views.MultipleCircleView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        }
    }
}
