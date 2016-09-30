package wy.aboutview.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import java.util.List;

import wy.aboutview.R;
import wy.aboutview.bean.Pickers;
import wy.aboutview.views.PickerScrollView;


public class ActionSheetDialog {
    private int type;
    private Context context;
    private Dialog dialog;
    private PickerScrollView sLayout_content;
    private Display display;

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.picker_dialog, null);

        view.setMinimumWidth(display.getWidth());
        sLayout_content = (PickerScrollView) view.findViewById(R.id.picker_view);
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int)(((Activity)context).getWindowManager().getDefaultDisplay().getWidth() * 0.97);
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public void setDataList(List<Pickers> dataList){
        sLayout_content.setData(dataList);
    }
    public void setSelectIndex(int index){
        sLayout_content.setSelected(index);
    }

    public void setListen(PickerScrollView.onSelectListener pickerListener){
        sLayout_content.setOnSelectListener(pickerListener);
    }

    public int getIndex(){
        return sLayout_content.getSelected();
    }
}
