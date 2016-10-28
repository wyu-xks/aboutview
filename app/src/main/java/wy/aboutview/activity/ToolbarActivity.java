package wy.aboutview.activity;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import wy.aboutview.R;

public class ToolbarActivity extends AppCompatActivity {

    private TextView text;
    private LinearLayout left_menu;
    private int width;
    private int height;
    private boolean isLeftMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        left_menu = (LinearLayout) findViewById(R.id.left_menu);
        text = (TextView) findViewById(R.id.text);
        toolbar.inflateMenu(R.menu.toolbar_menu);

        toolbar.setNavigationIcon(R.mipmap.data);
//        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle(R.string.home_page);//设置主标题
//        toolbar.setSubtitle("Subtitle");//设置子标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search) {
                    Toast.makeText(ToolbarActivity.this, R.string.menu_search, Toast.LENGTH_SHORT).show();
                    text.setText(getString(R.string.menu_search));
                } else if (menuItemId == R.id.action_notification) {
                    Toast.makeText(ToolbarActivity.this, R.string.menu_notifications, Toast.LENGTH_SHORT).show();
                    text.setText(getString(R.string.menu_notifications));
                } else if (menuItemId == R.id.action_settings) {
                    Toast.makeText(ToolbarActivity.this, R.string.menu_settings, Toast.LENGTH_SHORT).show();
                    text.setText(getString(R.string.menu_settings));
                } else if (menuItemId == R.id.action_about) {
                    Toast.makeText(ToolbarActivity.this, R.string.menu_about_us, Toast.LENGTH_SHORT).show();
                    text.setText(getString(R.string.menu_about_us));
                }
                return true;
            }
        });
        left_menu.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                width = left_menu.getWidth();
                height = left_menu.getHeight();
                left_menu.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ToolbarActivity.this, "点击导航栏图标", Toast.LENGTH_SHORT).show();
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isLeftMenu) {
                            for (int wid = width; wid >= 0; wid -= 5) {
                                SystemClock.sleep(2);
                                params.leftMargin = -1 * wid;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        left_menu.setLayoutParams(params);
                                    }
                                });
                            }
                            isLeftMenu = true;
                        } else {
                            for (int wid = 0; wid <= width; wid += 5) {
                                SystemClock.sleep(2);
                                params.leftMargin = -1 * wid;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        left_menu.setLayoutParams(params);
                                    }
                                });
                            }
                            isLeftMenu = false;
                        }
                    }
                }).start();
            }
        });
    }
}
