package wy.aboutview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import wy.aboutview.R;


/**
 * 创建者     xks
 * 创建时间   2016/7/20 14:29
 * 描述	      ${带颜色渐变的进度条，线程安全的View，可直接在线程中更新进度 }
 */
public class RoundProgressBar extends View {


    private final Paint textPaint;
    private final Paint jianPaint;
    private Context context;
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 圆环进度渐变开始的颜色
     */
    private int startColor;

    /**
     * 圆环进度渐变结束的颜色
     */
    private int endColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress = 0;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    int[] colors = new int[2];

    public RoundProgressBar(Context context) {
        this(context, null);
    }


    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        textPaint = new Paint();
        paint = new Paint();
        jianPaint = new Paint();

        this.context = context;

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.WHITE);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.YELLOW);
        startColor = mTypedArray.getColor(R.styleable.RoundProgressBar_startColor, Color.YELLOW);
        endColor = mTypedArray.getColor(R.styleable.RoundProgressBar_endColor, Color.BLUE);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.parseColor("#F7D175"));
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 64);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 8);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        mTypedArray.recycle();

        colors[0] = startColor;
        colors[1] = endColor;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */

        int centre = getWidth() / 2; //获取圆心的x坐标
        int height = getHeight() / 2; //获取圆心的y坐标
        int radius = (int) (centre - roundWidth / 2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, height, radius, paint); //画出圆环

        Log.e("log", centre + "");

        /**
         * 画进度百分比
         */
        textPaint.setStrokeWidth(0);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        int percent = (int) (((float) progress / (float) max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = textPaint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        if (textIsDisplayable && percent != 0 && style == STROKE) {
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize / 2, textPaint); //画出进度百分比
        }


        /**
         * 画圆弧 ，画圆环的进度
         */


        jianPaint.setStyle(Paint.Style.STROKE); //设置空心
        jianPaint.setStrokeCap(Paint.Cap.ROUND);
        jianPaint.setAntiAlias(true);  //消除锯齿
        //设置进度是实心还是空心
        jianPaint.setStrokeWidth(roundWidth); //设置圆环的宽度
        RectF oval = new RectF(centre - radius, height - radius, centre
                + radius, height + radius);  //用于定义的圆弧的形状和大小的界限
        float[] fl = new float[]{};
        Shader mShader = new SweepGradient(centre, height, colors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(90 * progress * 1.0f / max, centre, height);

        mShader.setLocalMatrix(matrix);


        jianPaint.setShader(mShader);


        switch (style) {
            case STROKE: {
                jianPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 90, 360 * progress * 1.0f / max, false, jianPaint);  //根据进度画圆弧
                break;
            }
            case FILL: {
                jianPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 90, 360 * progress / max, true, jianPaint);  //根据进度画圆弧
                break;
            }
        }
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    public synchronized int getMax() {
        return max;
    }


    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }


    public void setColors(int[] colors) {
        this.colors = colors;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }


    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }


    private Handler handler = new Handler();

    public int getCricleColor() {
        return roundColor;
    }


    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }


    public int getCricleProgressColor() {
        return roundProgressColor;
    }


    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }


    public int getTextColor() {
        return textColor;
    }


    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }


    public float getTextSize() {
        return textSize;
    }


    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }


    public float getRoundWidth() {
        return roundWidth;
    }


    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
