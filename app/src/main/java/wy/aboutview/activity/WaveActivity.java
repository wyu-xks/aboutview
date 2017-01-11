package wy.aboutview.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.header.MaterialProgressDrawable;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import wy.aboutview.R;
import wy.aboutview.views.WaveView;

public class WaveActivity extends AppCompatActivity {

    WaveView waveView;//方形
    WaveView waveCircleView;//圆形
    private int progrees = 0;//进度
    private PtrClassicFrameLayout ptrView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progrees == 60) {
                return;
            }
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
        ptrView = (PtrClassicFrameLayout) findViewById(R.id.store_house_ptr_frame);
        mHandler.sendEmptyMessageDelayed(0, 10);

        StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, dip2px(this, 20), 0, dip2px(this, 20));
        header.setTextColor(0xFFFF0000);
        header.initWithString("refrech");

        MaterialHeader materialHeader = new MaterialHeader(this);
        materialHeader.setColorSchemeColors(new int[]{0xffff0000, 0xff00ff00});

//        ptrView.setDurationToCloseHeader(1500);
//        ptrView.setHeaderView(materialHeader);
//        ptrView.addPtrUIHandler(materialHeader);
        ptrView.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                /**判断是否可以下拉刷新。 UltraPTR 的 Content 可以包含任何内容，用户在这里判断决定是否可以下拉。
                 例如，如果 Content 是 TextView，则可以直接返回 true，表示可以下拉刷新。
                 如果 Content 是 ListView，当第一条在顶部时返回 true，表示可以下拉刷新。
                 如果 Content 是 ScrollView，当滑动到顶部时返回 true，表示可以刷新。
                 */
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrView.refreshComplete();
                    }
                }, 1500);
            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
