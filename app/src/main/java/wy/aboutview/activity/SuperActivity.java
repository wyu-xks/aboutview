package wy.aboutview.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.allen.library.SuperTextView;

import wy.aboutview.R;

public class SuperActivity extends AppCompatActivity {

    private SuperTextView super_tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super);
        super_tv1 = (SuperTextView) findViewById(R.id.super_tv);
        super_tv1.setLeftBottomString("支付宝")
                .setLeftIcon(getResources().getDrawable(R.mipmap.clock_icon))
                .setLeftBottomString2("支付说明>>")
                .setLeftTopString("方式选择")
                .setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
            @Override
            public void onLeftBottomClick() {
                super.onLeftBottomClick();
                Log.d("SuperActivity", "onLeftBottomClick");
            }

            @Override
            public void onLeftTopClick() {
                super.onLeftTopClick();
                Log.d("SuperActivity", "onLeftTopClick");
            }

            @Override
            public void onLeftBottomClick2() {
                super.onLeftBottomClick2();
                Log.d("SuperActivity", "onLeftBottomClick2");
            }

            @Override
            public void onSuperTextViewClick() {
                super.onSuperTextViewClick();
                Log.d("SuperActivity", "onSuperTextViewClick");
            }
        });
    }
}
