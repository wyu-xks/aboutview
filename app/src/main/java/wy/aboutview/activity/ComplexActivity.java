package wy.aboutview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import wy.aboutview.Adapter.ComplexAdapter;
import wy.aboutview.R;

public class ComplexActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ComplexAdapter complexAdapter;
    private ArrayList<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex);

        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        complexAdapter = new ComplexAdapter(this,mDatas);
        mRecyclerView.setAdapter(complexAdapter);
        //SlideInOutLeftItemAnimator 、SlideInOutRightItemAnimator 、SlideInOutTopItemAnimator 、SlideInOutBottomItemAnimator 、ScaleInOutItemAnimator
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .paintProvider(complexAdapter)
                .marginProvider(complexAdapter)
                .visibilityProvider(complexAdapter)
                .build());
    }

    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 1; i < 25; i++) {
            mDatas.add("" + i);
        }
    }
}
