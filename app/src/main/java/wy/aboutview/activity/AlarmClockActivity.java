package wy.aboutview.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import wy.aboutview.R;
import wy.aboutview.bean.AlarmBean;
import wy.aboutview.views.AlarmClockView;

public class AlarmClockActivity extends FragmentActivity {

    private static final String TAG = AlarmClockActivity.class.getSimpleName();
    private String[] split;
    private String hour_time;
    private String min_time;
    private TextView targe_hour_time;
    private TextView targe_min_time;
    private TextView wakeup_time;
    private TextView mornming;
    private String timeText;
    private AlarmClockView alarmClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        initView();
    }

    private void initView() {
        String wakeUpTime = "08:06";
        String targerTime = "8:6";
        float sleepTime = -90;
        targe_hour_time = (TextView)findViewById(R.id.targe_hour_time);
        targe_min_time = (TextView)findViewById(R.id.targe_min_time);
        wakeup_time = (TextView)findViewById(R.id.wakeup_time);
        mornming = (TextView)findViewById(R.id.mornming);
        alarmClockView = (AlarmClockView)findViewById(R.id.alarm_clock);
        split = targerTime.split(":");
        hour_time = Integer.valueOf(split[0]) + "";
        min_time = Integer.valueOf(split[1]) + "";
        targe_hour_time.setText(hour_time);
        targe_min_time.setText(min_time);
        String hour = wakeUpTime.substring(0, 2);
        String min = wakeUpTime.substring(3);
        String wakeUp = getWakeUp(hour, min);
        wakeup_time.setText(wakeUp == null ? wakeUpTime : wakeUp);
        timeText = Integer.valueOf(hour) > 12 ? "下午" : "上午";
        mornming.setText(timeText);
        float progress = ((Integer.valueOf(hour) * 3600 + Integer.valueOf(min) * 60) * 1.0f / (12 * 3600) * 100);
        alarmClockView.setProgress(sleepTime, progress, true);

    }

    private String getWakeUp(String hour, String min) {
        if (Integer.valueOf(hour) > 12) {
            String hourStr = (Integer.valueOf(hour) - 12) + "";
            hourStr = hourStr.length() > 2 ? hourStr : "0" + hourStr;
            String wakeUp = hourStr + ":" + min;
            return wakeUp;
        } else {
            return null;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AlarmBean alarmBean) {
        String hour = alarmBean.wakeUpTime.substring(0, 2);
        String min = alarmBean.wakeUpTime.substring(3);
        String wakeUp = getWakeUp(hour, min);
        wakeup_time.setText(wakeUp == null ? alarmBean.wakeUpTime : wakeUp);
        timeText = Integer.valueOf(hour) > 12 ? "下午" : "上午";
        mornming.setText(timeText);
        split = alarmBean.targeTime.split(":");
        hour_time = Integer.valueOf(split[0]) + "";
        min_time = Integer.valueOf(split[1]) + "";
        targe_hour_time.setText(hour_time);
        targe_min_time.setText(min_time);
        String sleepTime = getTimeStr(alarmBean.sleepTime);
        Log.e(TAG, "sleepTime :" + sleepTime);
    };


    private String getTimeStr(float time) {
        if (time < 0) {
            time = 12 * 60 * 60 + time;
            int intTime = (int) time;
            int hourCount = intTime / 3600;
            int min = intTime % 3600;
            int minCount = min / 60;
            String sleepTime = (hourCount + 12) + ":" + (minCount >= 10 ? "" + minCount : "0" + minCount) + ":00";
            return sleepTime;
        } else {
            int hourCount = (int) (time / 3600);
            int min = (int) time % 3600;
            int minCount = min / 60;
            String sleepTime = (hourCount >= 10 ? hourCount : "0" + hourCount) + ":" + (minCount >= 10 ? "" + minCount : "0" + minCount) + ":00";
            return sleepTime;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
