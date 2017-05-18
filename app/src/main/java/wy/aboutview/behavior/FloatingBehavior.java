package wy.aboutview.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author : xks
 * Date : 2017/5/11
 * Des : @Des
 */

public class FloatingBehavior extends FloatingActionButton.Behavior {

    public FloatingBehavior(Context context, AttributeSet attrs) {

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency) || dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        if (dependency instanceof NestedScrollView) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fab_BM = lp.bottomMargin;
            int distance = fab.getHeight() + fab_BM ;
            fab.setY(dependency.getY() - distance);
        }
        return super.onDependentViewChanged(parent, fab, dependency);
    }
}
