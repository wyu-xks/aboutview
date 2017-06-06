package wy.aboutview.activity;

import android.graphics.Color;
import android.os.IBinder;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;

import wy.aboutview.R;
import wy.aboutview.image.ImgLoadUtil;


public class CoordinatorActivity extends AppCompatActivity {

    private NestedScrollView scrollView;
    private NavigationView nestedScrollView;
    private ImageView userIw;



    private IBinder.DeathRecipient mDeathRecipent = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.setColor(this, 0XFF636A96);
        StatusBarUtil.setColorNoTranslucent(this,getResources().getColor(R.color.colorAccent));
        setContentView(R.layout.activity_coordinator);
        scrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        nestedScrollView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = nestedScrollView.getHeaderView(0);
        userIw = (ImageView) headerView.findViewById(R.id.user_iv);
        ImgLoadUtil.displayCircle(userIw,R.drawable.ic_avatar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(scrollView);
                bottomSheetBehavior.setState(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED?BottomSheetBehavior.STATE_EXPANDED:BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

}
