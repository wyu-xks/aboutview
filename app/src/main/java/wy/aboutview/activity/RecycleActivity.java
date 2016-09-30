package wy.aboutview.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import wy.aboutview.Adapter.ComplexAdapter;
import wy.aboutview.R;

public class RecycleActivity extends AppCompatActivity {

    private ArrayList<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("aboutview");
    }

    public void linear(View viw){
        Intent intent = new Intent(this,ListActivity.class);
        startActivity(intent);
    }

    public void grid(View v){
        Intent intent = new Intent(this,GridActivity.class);
        startActivity(intent);
    }

    public void adapterList(View v){
        Intent intent = new Intent(this, AnimationListActivity.class);
        startActivity(intent);
    }

    public void adapterGrid(View v){
        Intent intent = new Intent(this, AnimationGridActivity.class);
        startActivity(intent);
    }


    public void complexDiver(View v){
        Intent intent = new Intent(this, ComplexActivity.class);
        startActivity(intent);
    }

    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 1; i < 25; i++) {
            mDatas.add("" + i);
        }
    }
}
