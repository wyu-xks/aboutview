package wy.aboutview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;

import wy.aboutview.bean.Histogram;

/**
 * Created by Xie on 2016/7/26.
 */
public class ChartView extends View {
    private static final String TAG = "ChartView";
    private final Paint wakePaint;
    private final Paint xulinePaint;
    private Context context;
    private Paint textPaint;
    private Paint linePaint;
    private Paint deepPaint;
    private Paint remPaint;
    private Paint lightPaint;
    private Paint middlePaint;
    private int width;
    private int height;

    private int endHeight;
    private int heightSum;
    private int line1Height;
    private int line2Height;
    private int line3Height;
    private int line4Height;
    private int widthSum;
    private int startX;
    private float timeStartX;
    private int endX;
    private LinearGradient linearGradient1;
    private LinearGradient linearGradient2;
    private LinearGradient linearGradient3;
    private LinearGradient linearGradient4;
    private LinearGradient linearGradient5;
    private int wakeHeight;
    private float oneTextWidth;
    private int singleHeight;
    private float singleWidth;
    private ArrayList<Histogram> dataList = new ArrayList<>();
    private int hours = 0;
    private int start = -1;
    private int end;
    private float drawStartX;
    private float drawEndX;
    private float twoTextWidth;
    private boolean isWakeShow = false;
    private float firstRate;
    private float singleRate;
    private String startTime;
    private String endTime;
    private int type;
    private float fourTextWidth;
    private int firstType;
    private int endType;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        lightPaint = new Paint();
        middlePaint = new Paint();
        deepPaint = new Paint();
        remPaint = new Paint();
        wakePaint = new Paint();
        linePaint = new Paint();
        xulinePaint = new Paint();
        textPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        linePaint.setColor(0XFF4F4B70);
        linePaint.setStrokeWidth(dipToPx(1));
        //画高度横线
        canvas.drawLine(0, line1Height - dipToPx(1), width, line1Height - dipToPx(1), linePaint);
        canvas.drawLine(0, line2Height - dipToPx(1), width, line2Height - dipToPx(1), linePaint);
        canvas.drawLine(0, line3Height - dipToPx(1), width, line3Height - dipToPx(1), linePaint);
        canvas.drawLine(0, line4Height + dipToPx(1), width, line4Height - dipToPx(1), linePaint);

        textPaint.setColor(0xfff7d175);
        textPaint.setStrokeWidth(0);
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        oneTextWidth = textPaint.measureText("2");   //测量字体宽度，
        twoTextWidth = textPaint.measureText("22");   //测量字体宽度，
        fourTextWidth = textPaint.measureText("11:40");   //测量字体宽度，

//        singleWidth = (int) ((width - (hours + 1) * oneTextWidth / 2) / hours + 1.0);
//        singleWidth = (widthSum - dipToPx(10)) * 1.0f / hours;

        singleWidth = widthSum * singleRate;
        timeStartX = startX + widthSum * firstRate;
        if (start != -1) {
            switch (type) {
                case 0:
                    int timeStart = 0;
                    for (int i = 0; i <= hours; i++) {
                        timeStart = start + i >= 24 ? start + i - 24 : start + i;
                        canvas.drawText(timeStart + "", timeStartX + i * singleWidth - (timeStart > 10 ? twoTextWidth / 2 : oneTextWidth / 2), endHeight + oneTextWidth + dipToPx(5), textPaint);
                    }
                    break;
                case 2:
                    canvas.drawText(startTime, startX, endHeight + oneTextWidth + dipToPx(5), textPaint);
                    canvas.drawText(endTime + "", endX - fourTextWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
                    break;
                case 3:
                    int midTime = start + 1 >= 24 ? start + 1 - 24 : start + 1;
                    int lastTime = start + 2 >= 24 ? start + 2 - 24 : start + 2;
                    if (firstType == 0) {
                        canvas.drawText(startTime, startX, endHeight + oneTextWidth + dipToPx(5), textPaint);
                    }
                    if (endType == 0) {
                        canvas.drawText(endTime, endX - fourTextWidth, endHeight + oneTextWidth + dipToPx(5), textPaint);
                    }
                    canvas.drawText(start + "", timeStartX - (midTime > 10 ? twoTextWidth / 2 : oneTextWidth / 2), endHeight + oneTextWidth + dipToPx(5), textPaint);
                    canvas.drawText(midTime + "", timeStartX + singleWidth - (midTime > 10 ? twoTextWidth / 2 : oneTextWidth / 2), endHeight + oneTextWidth + dipToPx(5), textPaint);
                    canvas.drawText(lastTime + "", timeStartX + 2 * singleWidth - (midTime > 10 ? twoTextWidth / 2 : oneTextWidth / 2), endHeight + oneTextWidth + dipToPx(5), textPaint);
                    break;
            }
        }


        xulinePaint.setColor(0xffffffff);
        xulinePaint.setStrokeWidth(dipToPx(1));
        xulinePaint.setStyle(Paint.Style.STROKE);
        xulinePaint.setAntiAlias(true);
        PathEffect effect = new DashPathEffect(new float[]{15, 15, 15, 15}, 0);
        Path path = new Path();


//        drawStartX = startX + dipToPx(5);
        drawStartX = startX;
        if (dataList != null && dataList.size() > 0) {
            for (Histogram histogram : dataList) {
                int key = histogram.type;
                float value = histogram.value;
//                float width = value * (widthSum - dipToPx(10));
                float width = value * (widthSum);
                drawHistogram(key, width, canvas);
            }
        }
    }

    private void drawHistogram(int key, float width, final Canvas canvas) {
        switch (key) {
            case 5:
                drawEndX = drawStartX + width;
                linearGradient5 = new LinearGradient(drawStartX, wakeHeight, drawEndX, endHeight, 0xffe20118, 0xFFEF8DAC, Shader.TileMode.MIRROR);
                wakePaint.setShader(linearGradient5);
                wakePaint.setStrokeWidth(1.0f);
                canvas.drawRect(drawStartX, wakeHeight, drawEndX, endHeight, wakePaint);// 长方形
                drawStartX = drawEndX;
                break;
            case 4:
                drawEndX = drawStartX + width;
                linearGradient1 = new LinearGradient(drawStartX, line1Height, drawEndX, endHeight, 0xffff7e0c, 0xFF9FDE88, Shader.TileMode.MIRROR);
                remPaint.setShader(linearGradient1);
                canvas.drawRect(drawStartX, line1Height, drawEndX, endHeight, remPaint);// 长方形
                drawStartX = drawEndX;
                break;
            case 3:
            case 2:
                drawEndX = drawStartX + width;
                linearGradient2 = new LinearGradient(drawStartX, line2Height, drawEndX, endHeight, 0Xff37dc00, 0XFFCAC6E6, Shader.TileMode.MIRROR);
                lightPaint.setShader(linearGradient2);
                canvas.drawRect(drawStartX, line2Height, drawEndX, endHeight, lightPaint);// 长方形
                drawStartX = drawEndX;

                break;
            case 1:
                drawEndX = drawStartX + width;
                linearGradient3 = new LinearGradient(drawStartX, line3Height, drawEndX, endHeight, 0xff00b9df, 0xff00fad0, Shader.TileMode.MIRROR);
                deepPaint.setShader(linearGradient3);
                canvas.drawRect(drawStartX, line3Height, drawEndX, endHeight, deepPaint);// 长方形
                drawStartX = drawEndX;
                break;
            case 0:
                drawEndX = drawStartX + width;
                drawStartX = drawEndX;
                break;
        }

    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            //MeasureSpec.EXACTLY表示该view设置的确切的数值
            width = widthSize;
            height = heightSize;
        }

//        Log.e(TAG, "width:" + width + " heigth:" + height);
        endHeight = height / 2 - dipToPx(10); //结束柱状图的Y坐标
        heightSum = endHeight - dipToPx(60);
        widthSum = width - dipToPx(20);
        startX = dipToPx(10);
        endX = width - dipToPx(10);
        singleHeight = heightSum / 4;
        wakeHeight = dipToPx(60);
        line1Height = dipToPx(60) + singleHeight;
        line2Height = dipToPx(60) + singleHeight * 2;
        line3Height = dipToPx(60) + singleHeight * 3;
        line4Height = endHeight;
        setMeasuredDimension(width, height);
    }

    //设置坐标的开始点与结束点
    public void setSleepTime(int start, int end, float firstRate, float singleRate, String startTime, String endTime, int type, int firstType, int endType) {
        this.start = start;
        this.end = end;
        this.firstRate = firstRate;
        this.singleRate = singleRate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.firstType = firstType;
        this.endType = endType;
        hours = end < start ? end + 24 - start : end - start;
        if (hours != 0) {
            singleWidth = widthSum * singleRate;
            timeStartX = startX + dipToPx(5) + widthSum * firstRate;
        } else {
            singleWidth = widthSum * 1.0f;
        }
        postInvalidate();
    }

    public void setDataList(ArrayList<Histogram> dataList) {
        this.dataList = dataList;
//        singleWidth = widthSum / hours;
        postInvalidate();
    }

}
