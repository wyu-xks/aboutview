package wy.aboutview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import wy.aboutview.R;
import wy.aboutview.bean.Pickers;
import wy.aboutview.dialog.ActionSheetDialog;
import wy.aboutview.views.PickerScrollView;

public class SelectActivity extends AppCompatActivity {

    private static final String TAG = SelectActivity.class.getSimpleName();
    private ActionSheetDialog dialog;
    private ActionSheetDialog actionSheetDialog;
    private List<Pickers> ageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Button selection = (Button) findViewById(R.id.selection);
        initData();
        selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new ActionSheetDialog(SelectActivity.this);
                    actionSheetDialog = dialog.builder();
                }
                actionSheetDialog.show();
                actionSheetDialog.setDataList(ageList);
                actionSheetDialog.setSelectIndex(actionSheetDialog.getIndex());
                actionSheetDialog.setListen(pickerListener);
            }
        });
    }

    private void initData() {
        ageList.add(new Pickers("1", getString(R.string.age_less_15)));
        ageList.add(new Pickers("2", getString(R.string.age_less_20)));
        ageList.add(new Pickers("3", getString(R.string.age_less_25)));
        ageList.add(new Pickers("4", getString(R.string.age_less_30)));
        ageList.add(new Pickers("5", getString(R.string.age_less_35)));
        ageList.add(new Pickers("6", getString(R.string.age_less_40)));
        ageList.add(new Pickers("7", getString(R.string.age_less_50)));
        ageList.add(new Pickers("8", getString(R.string.age_less_60)));
        ageList.add(new Pickers("9", getString(R.string.age_more_60)));

    }

    // 滚动选择器选中事件
    PickerScrollView.onSelectListener pickerListener = new PickerScrollView.onSelectListener() {

        @Override
        public void onSelect(Pickers pickers) {
            Log.e(TAG, "选择：" + pickers.getShowId() + "--内容："
                    + pickers.getShowConetnt());
        }
    };
}
