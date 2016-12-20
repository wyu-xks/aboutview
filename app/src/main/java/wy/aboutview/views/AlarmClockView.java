package wy.aboutview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import wy.aboutview.R;
import wy.aboutview.bean.AlarmBean;


/**
 * 创建者     xks
 * 创建时间   2016/7/20 14:29
 * 描述	      ${自定义闹钟控件，可直接在线程中更新进度 }
 */
public class AlarmClockView extends View {


    private static final String TAG = AlarmClockView.class.getSimpleName();
    private static final int HOUR = 1;
    private static final int MIN = 2;
    private static final int END_ICON = 3;
    private static final int START_ICON = 4;
    private final Paint textPaint;
    private final Paint jianPaint;
    private final float hourRadius;
    private final float minRadius;
    private final Bitmap sleep_bitmap;
    private final Bitmap wake_bitmap;
    private final int sleep_bitmap_weidth;
    private final int sleep_bitmap_height;
    private final int wake_bitmap_weidth;
    private final int wake_bitmap_height;
    private final float icon_radius;

    private Paint paint;
    private int roundColor;//圆环底色
    private int roundProgressColor;
    private int startColor;
    private int endColor;
    private int textColor; //中间字符串的颜色
    private float textSize;//中间字符串的字
    private float roundWidth; // 圆环的宽度
    private int max;

    private float progress = 0;
    private boolean textIsDisplayable;
    private int style;//进度的风格，实心或者空心

    public static final int STROKE = 0;
    public static final int FILL = 1;

    int[] colors = new int[2];

    private int mBigSliceRadius; // 较长刻度半径
    private int mSmallSliceRadius; // 较短刻度半径
    private int mNumMeaRadius; // 数字刻度半径

    private int mSmallSliceCount; // 短刻度个数
    private float mBigSliceAngle; // 大刻度等分角度
    private float mSmallSliceAngle; // 小刻度等分角度
    private float radius;
    private int mStartAngle = 0; // 起始角度
    private int mSweepAngle; // 绘制角度
    private int mBigSliceCount = 12; // 大份数
    private int mSliceCountInOneBigSlice = 4; // 划分一大份长的小份数
    private int mSmallArcColor = 0xFFC2A3A3; // 弧度颜色
    private int mBigArcColor = 0xFFECE1E1; // 弧度颜色
    private int mMeasureTextSize = 30; // 刻度字体大小
    private int mTextColor = 0xffffffff; // 字体颜色
    private Paint mPaintArc;
    private int mViewWidth;
    private int mViewHeight;
    private float mCenterX;
    private float mCenterY;
    private String[] mGraduations = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "1", "2"};
    private Rect mRectMeasures;
    private int mWaiRadius;
    private float start = -90;
    private float startChange = start;

    /**
     * 是否允许拖动进度条
     */
    private boolean draggingEnabled = true;
    private boolean isFirst = true;
    private boolean isLast = false;
    private float firstDownY;
    private Bitmap bitmap;
    private int bitmapWidth;
    private int bitmapHeight;
    private Paint bitmapPaint;
    private Paint zhenPaint;
    private float minDegree;
    private float hourDegree;
    private float hourK;
    private float hourB;
    private float minK;
    private float minB;
    private float verHourX = 0;
    private float verMinX = 0;
    private RectF oval;

    public enum StripeMode {
        NORMAL,
        INNER,
        OUTER
    }

    public AlarmClockView(Context context) {
        this(context, null);
    }


    public AlarmClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public AlarmClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        textPaint = new Paint();
        paint = new Paint();
        jianPaint = new Paint();

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
        bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.biao);
        sleep_bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.sleep_icon);
        wake_bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.wake_up_icon);

        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        sleep_bitmap_weidth = sleep_bitmap.getWidth();
        sleep_bitmap_height = sleep_bitmap.getHeight();
        wake_bitmap_weidth = wake_bitmap.getWidth();
        wake_bitmap_height = wake_bitmap.getHeight();

        mCenterX = bitmapWidth / 2.0f + roundWidth;
        mCenterY = bitmapHeight / 2.0f + roundWidth;
        radius = bitmapWidth / 2.0f;
        icon_radius = bitmapHeight / 2.0f + 3;
        hourRadius = radius * 0.341f;
        minRadius = radius * 0.484f;
        oval = new RectF(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
    }

    private void initObject() {
        bitmapPaint = new Paint();
        bitmapPaint.setDither(true);

        mPaintArc = new Paint();
        mPaintArc.setAntiAlias(true);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setStrokeCap(Paint.Cap.ROUND);

        //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);  //消除锯齿

        textPaint.setStrokeWidth(0);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.MONOSPACE); //设置字体

        /**
         * 画圆弧 ，画圆环的进度
         */
        jianPaint.setStyle(Paint.Style.STROKE); //设置空心
        jianPaint.setStrokeCap(Paint.Cap.ROUND);
        jianPaint.setAntiAlias(true);  //消除锯齿
        //设置进度是实心还是空心
        jianPaint.setStrokeWidth(roundWidth); //设置圆环的宽度
        jianPaint.setColor(startColor);
        mRectMeasures = new Rect();

        zhenPaint = new Paint();
        zhenPaint.setColor(0Xffffffff);
        zhenPaint.setStrokeWidth(radius * 0.0544f);
        zhenPaint.setStrokeCap(Paint.Cap.ROUND);
        zhenPaint.setAntiAlias(true);  //消除锯齿
    }

    private void initSize() {
        mWaiRadius = (int) radius - dipToPx(16);
        mSmallSliceRadius = mWaiRadius - dipToPx(6);
        mBigSliceRadius = mWaiRadius - dipToPx(9);
        mNumMeaRadius = mWaiRadius - dipToPx(15);

        mSmallSliceCount = mBigSliceCount * mSliceCountInOneBigSlice;
        Log.e(TAG, "mSmallSliceCount : " + mSmallSliceCount);
        mBigSliceAngle = 360f / (float) mBigSliceCount;
        mSmallSliceAngle = 360f / (float) 48;
    }

    /**
     * 绘制刻度盘
     */
    private void drawMeasures(Canvas canvas) {
        mPaintArc.setStrokeWidth(dipToPx(2));
        for (int i = 0; i < mBigSliceCount; i++) {
            //绘制大刻度
            float angle = i * mBigSliceAngle + mStartAngle;
            float[] point1 = getCoordinatePoint(mWaiRadius, angle);
            float[] point2 = getCoordinatePoint(mBigSliceRadius, angle);
            mPaintArc.setColor(mBigArcColor);
            canvas.drawLine(point1[0], point1[1], point2[0], point2[1], mPaintArc);

            //绘制圆盘上的数字
            textPaint.setTextSize(mMeasureTextSize);
            String number = mGraduations[i];
            textPaint.getTextBounds(number, 0, number.length(), mRectMeasures);
            float singleWidth = textPaint.measureText("6");
            float[] numberPoint = getTextPoint(mNumMeaRadius, angle, singleWidth);
            if (i == 0 || i == mBigSliceCount) {
                canvas.drawText(number, numberPoint[0], numberPoint[1] + (mRectMeasures.height() / 2), textPaint);
            } else {
                canvas.drawText(number, numberPoint[0], numberPoint[1] + mRectMeasures.height(), textPaint);
            }
        }

        //绘制小的子刻度
        mPaintArc.setStrokeWidth(dipToPx(1));
        for (int i = 0; i < mSmallSliceCount; i++) {
            if (i % (mSliceCountInOneBigSlice) != 0) {
                float angle = i * mSmallSliceAngle + mStartAngle;
                float[] point1 = getCoordinatePoint(mWaiRadius, angle);
                float[] point2 = getCoordinatePoint(mSmallSliceRadius, angle);
                mPaintArc.setColor(mSmallArcColor);
                mPaintArc.setStrokeWidth(dipToPx(1));
                canvas.drawLine(point1[0], point1[1], point2[0], point2[1], mPaintArc);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCenterX, mCenterY, radius, paint); //画出圆环
        float endDegree = getEndDegree();
        float percent = (endDegree - startChange) / 360;
        int second = (int) (12 * 60 * percent * 60 + 0.5);
        String time = getTimeStr(second);
        String targer = getTargerTime(Math.abs(second));
        targer = (endDegree - startChange) <= 354 ? targer : "12:0";
        //drawMeasures(canvas);
        String timeStr;
        String[] split;
//        if (endDegree < -90 && endDegree > -450) {
//            float wakeUp = 12 * 60 * 60 - 12 * 60 * 60 * Math.abs(endDegree + 90) / 360;
//            timeStr = getTimeStr(wakeUp);
//            split = timeStr.split(":");
//            int i = Integer.valueOf(split[0]) + 12;
//            timeStr = i + ":" + split[1];
//            startDraw(canvas, split, targer, timeStr, endDegree);
//        } else if (endDegree >= -90 && endDegree <= 540) {
        float wakeUp = 12 * 60 * 60 * (endDegree + 90) / 360;
        timeStr = wakeUp == 0 ? "00:00" : getTimeStr(wakeUp);
        Log.e(TAG, "wakeUp : " + wakeUp + " timeStr : " + timeStr);
        split = timeStr.split(":");
        startDraw(canvas, split, targer, timeStr, endDegree, wakeUp);
//        }
    }


    public void startDraw(Canvas canvas, String[] split, String targer, String timeStr, float endDegree, float wakeUp) {
        //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                jianPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, startChange, 360 * progress * 1.0f / max - 7 + (startChange > -90 ? -(startChange + 90) : (-90 - startChange)), false, jianPaint);  //根据进度画圆弧
                break;
            }
            case FILL: {
                jianPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, startChange, 360 * progress * 1.0f / max - 7 + (startChange > -90 ? -(startChange + 90) : (-90 - startChange)), false, jianPaint);  //根据进度画圆弧
                break;
            }
        }
        canvas.drawBitmap(bitmap, roundWidth, roundWidth, bitmapPaint);
        canvas.drawCircle(mCenterX, mCenterY, radius * 0.0515f, zhenPaint); //画出时针分针圆点

        hourDegree = Integer.valueOf(split[0]) * 30 + Integer.valueOf(split[1]) / (12 * 60f) * 360f;
        minDegree = Integer.valueOf(split[1]) / 60f * 360;
        Log.e(TAG, "hourDegree :" + hourDegree);
        Log.e(TAG, "minDegree :" + minDegree);
        float wakeUpTime = Integer.valueOf(split[0]) * 3600 + Integer.valueOf(split[1]) * 60;
        String[] tartSplit = targer.split(":");
        float targerTime = Integer.valueOf(tartSplit[0]) * 3600 + Integer.valueOf(tartSplit[1]) * 60;
        float startSleepTime = wakeUpTime - targerTime;
        float[] hourLines = getLinePosition(hourDegree, hourRadius, HOUR);
        float[] minLines = getLinePosition(minDegree, minRadius, MIN);
        float[] endIconPosition = getIconPosition(icon_radius, END_ICON);
        float[] startIconPosition = getIconPosition(icon_radius, START_ICON);
        canvas.drawLine(mCenterX, mCenterY, hourLines[0], hourLines[1], zhenPaint); //画出圆环
        canvas.drawLine(mCenterX, mCenterY, minLines[0], minLines[1], zhenPaint); //画出圆环
        canvas.drawBitmap(wake_bitmap, endIconPosition[0], endIconPosition[1], bitmapPaint);
        canvas.drawBitmap(sleep_bitmap, startIconPosition[0], startIconPosition[1], bitmapPaint);
        if (!isFirst) {
            EventBus.getDefault().post(new AlarmBean(startChange, targer, timeStr, endDegree, startSleepTime));
        }
    }

    private float[] getLinePosition(float degree, float radius, int type) {
        float[] position = new float[2];
        if (degree < 90) {
            position[0] = mCenterX + (float) (radius * Math.sin(Math.toRadians(degree)));
            position[1] = mCenterY - (float) (radius * Math.cos(Math.toRadians(degree)));
        } else if (degree < 180) {
            position[0] = mCenterX + (float) (radius * Math.cos(Math.toRadians(degree - 90)));
            position[1] = mCenterY + (float) (radius * Math.sin(Math.toRadians(degree - 90)));
        } else if (degree < 270) {
            double sin = Math.sin(Math.toRadians(degree - 180));
            position[0] = mCenterX - (float) (radius * sin);
            position[1] = mCenterY + (float) (radius * Math.cos(Math.toRadians(degree - 180)));
        } else {
            position[0] = mCenterX - (float) (radius * Math.cos(Math.toRadians(degree - 270)));
            position[1] = mCenterY - (float) (radius * Math.sin(Math.toRadians(degree - 270)));
        }
        switch (type) {
            case HOUR:
                if ((position[0] - mCenterX) != 0) {
                    verHourX = 0;
                    if ((position[1] - mCenterY) == 0) {
                        hourK = 0;
                        hourB = mCenterY * 1.0f - hourK * mCenterX;
                    } else {
                        hourK = (position[1] - mCenterY) / (position[0] - mCenterX) * 1.0f;
                        hourB = mCenterY * 1.0f - hourK * mCenterX;
                    }
                } else {
                    verHourX = mCenterX;
                }
                break;
            case MIN:
                if ((position[0] - mCenterX) != 0) {
                    verMinX = 0;
                    if ((position[1] - mCenterY) == 0) {
                        minK = 0;
                        minB = mCenterY * 1.0f - minK * mCenterX;
                    } else {
                        minK = (position[1] - mCenterY) / (position[0] - mCenterX) * 1.0f;
                        minB = mCenterY * 1.0f - minK * mCenterX;
                    }
                } else {
                    verMinX = mCenterX;
                }
                break;
        }
        return position;
    }

    private float[] getIconPosition(float radius, int type) {
        float[] position = new float[2];
        float degree = 0f;
        float width = 0f;
        float height = 0f;
        switch (type) {
            case END_ICON:
                degree = getEndDegree() + 90 - 7;
                width = wake_bitmap_weidth / 2f;
                height = wake_bitmap_height / 2f;
                break;
            case START_ICON:
                degree = 90 + startChange;
                width = sleep_bitmap_weidth / 2f;
                height = sleep_bitmap_height / 2f;
                break;
        }
        if (degree < 90) {
            position[0] = mCenterX + (float) (radius * Math.sin(Math.toRadians(degree)));
            position[1] = mCenterY - (float) (radius * Math.cos(Math.toRadians(degree)));
        } else if (degree < 180) {
            position[0] = mCenterX + (float) (radius * Math.cos(Math.toRadians(degree - 90)));
            position[1] = mCenterY + (float) (radius * Math.sin(Math.toRadians(degree - 90)));
        } else if (degree < 270) {
            double sin = Math.sin(Math.toRadians(degree - 180));
            position[0] = mCenterX - (float) (radius * sin);
            position[1] = mCenterY + (float) (radius * Math.cos(Math.toRadians(degree - 180)));
        } else {
            position[0] = mCenterX - (float) (radius * Math.cos(Math.toRadians(degree - 270)));
            position[1] = mCenterY - (float) (radius * Math.sin(Math.toRadians(degree - 270)));
        }
        position[0] = position[0] - width;
        position[1] = position[1] - height;
        return position;
    }


    private String getTimeStr(int time) {
        int hourCount = (time / 3600);
        int min = time % 3600;
        int minCount = min / 60;
        return hourCount + "h" + minCount + "min";
    }

    private String getTargerTime(int time) {
        int hourCount = (time / 3600);
        int min = time % 3600;
        int minCount = min / 60;
        return hourCount + ":" + minCount;
    }

    private String getTimeStr(float time) {
        int hourCount = (int) (time / 3600);
        int min = (int) time % 3600;
        int minCount = min / 60;
        hourCount = hourCount == 24 ? 0 : hourCount;
        String wakeup_time = (hourCount >= 10 ? hourCount : "0" + hourCount) + ":" + (minCount >= 10 ? "" + minCount : "0" + minCount);
        Log.e(TAG, "wakeup_time : " + wakeup_time);
        return wakeup_time;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mViewWidth = widthSize;
        } else {
            if (widthMode == MeasureSpec.AT_MOST) {
                mViewWidth = Math.min(mViewWidth, widthSize);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mViewHeight = heightSize;
        } else {
            if (widthMode == MeasureSpec.AT_MOST) {
                mViewHeight = Math.min(mViewHeight, widthSize);
            }
        }
        initObject();
        initSize();
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     */
    public float[] getTextPoint(int radius, float cirAngle, float singleWidth) {
        float[] point = new float[2];
        double arcAngle = Math.toRadians(cirAngle); //将角度转换为弧度
        if (cirAngle == 0) {
            point[0] = mCenterX + radius - singleWidth;
            point[1] = mCenterY;
        } else if (cirAngle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius) - singleWidth / 2 - dipToPx(2);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius) - singleWidth / 2 - dipToPx(1);
        } else if (cirAngle == 90) {
            point[0] = mCenterX - singleWidth / 2;
            point[1] = mCenterY + radius - singleWidth - dipToPx(2);
        } else if (cirAngle > 90 && cirAngle < 180) {
            arcAngle = Math.PI * (180 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius) + singleWidth / 2 - dipToPx(1);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius) - singleWidth / 2 - dipToPx(1);
        } else if (cirAngle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY - singleWidth / 2;
        } else if (cirAngle > 180 && cirAngle < 270) {
            arcAngle = Math.PI * (cirAngle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius) - singleWidth / 2 - dipToPx(1);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius) + dipToPx(1);
        } else if (cirAngle == 270) {
            point[0] = mCenterX - singleWidth;
            point[1] = mCenterY - radius + dipToPx(1);
        } else {
            arcAngle = Math.PI * (360 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius) - singleWidth / 2 - dipToPx(2);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }

    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     */
    public float[] getCoordinatePoint(int radius, float cirAngle) {
        float[] point = new float[2];
        double arcAngle = Math.toRadians(cirAngle); //将角度转换为弧度
        if (cirAngle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (cirAngle > 90 && cirAngle < 180) {
            arcAngle = Math.PI * (180 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (cirAngle > 180 && cirAngle < 270) {
            arcAngle = Math.PI * (cirAngle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (cirAngle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }


    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    public synchronized int getMax() {
        return max;
    }


    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized float getProgress() {
        return progress;
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

    public float[] getStart() {
        float[] start = new float[2];
        return start;
    }

    private boolean isStartDragging = false;
    private boolean isEndDragging = false;
    private boolean isMinDragging = false;
    private boolean isHoueDragging = false;

    float downX;
    float downY;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!draggingEnabled) {
            return super.onTouchEvent(event);
        }
        //处理拖动事件
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                firstDownY = event.getY();
                downY = event.getY();
                //判断是否在进度条thumb位置
                if (checkOnArcEnd(downX, downY)) {
                    isEndDragging = true;
                    Log.e(TAG, "按下位置在进度条end附近...");
                } else if (checkOnArcStart(downX, downY)) {
                    isStartDragging = true;
                    Log.e(TAG, "按下位置在进度条start附近...");
                } else if (checkOnMinLine(downX, downY)) {
                    Log.e(TAG, "按下位置在分针线上附近...");
                    isMinDragging = true;
                } else if (checkOnHourLine(downX, downY)) {
                    Log.e(TAG, "按下位置在时针线上附近...");
                    isHoueDragging = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                float currentY = event.getY();
                if (isEndDragging) {
                    //判断拖动时是否移出去了
                    if (checkInArc(currentX, currentY)) {
                        startChange = start;
                        float degreeEndByPosition = calDegreeEndByPosition(currentX, currentY, downX, downY);
                        float progress = (degreeEndByPosition + 90) / 360 * max;
                        float wakeUp = 12 * 60 * 60 * (degreeEndByPosition + 90) / 360;
                        if (degreeEndByPosition - startChange >= 15 && degreeEndByPosition - startChange <= 355 && wakeUp >= 0) {
                            setProgressSync(progress);
                        }
                        downX = currentX;
                        downY = currentY;
                    } else {
                        isEndDragging = false;
                    }
                } else if (isStartDragging) {
                    if (checkInArc(currentX, currentY)) {
                        float change = calDegreeStartByPosition(currentX, currentY, downX, downY);
                        if (getEndDegree() - change >= 15 && getEndDegree() - change <= 355 && change <= 270) {
                            startChange = change;
                            setProgressSync(progress);
                        } else {
                            start = startChange;
                        }
                        downX = currentX;
                        downY = currentY;
                    } else {
                        isStartDragging = false;
                    }
                } else if (isHoueDragging) {
                    //时针滑动
                    float zhenByPosition = calDegreeZhenByPosition(currentX, currentY, downX, downY, HOUR);
                    float progress = (zhenByPosition + 90) / 360 * max;
                    float wakeUp = 12 * 60 * 60 * (zhenByPosition + 90) / 360;
                    if (zhenByPosition - startChange >= 15 && zhenByPosition - startChange <= 355 && wakeUp >= 0) {
//                    if (zhenByPosition - startChange <= 355 && startChange <= 268) {
                        setProgressSync(progress);
                    }
                    downX = currentX;
                    downY = currentY;
                } else if (isMinDragging) {
                    //分针滑动
                    float zhenByPosition = calDegreeZhenByPosition(currentX, currentY, downX, downY, MIN);
                    float progress = (zhenByPosition + 90) / 360 * max;
                    float wakeUp = 12 * 60 * 60 * (zhenByPosition + 90) / 360;
                    if (zhenByPosition - startChange >= 15 && zhenByPosition - startChange <= 355 && wakeUp >= 0) {
                        setProgressSync(progress);
                    }
                    downX = currentX;
                    downY = currentY;
                }
                break;
            case MotionEvent.ACTION_UP:
//                if (AppConfig.getInstance().getClockButtonOn()) {
//                    if (isStartDragging) {
//                        EventBus.getDefault().post(new TouchUp(MyConstans.START_CHANGE));
//                    } else {
//                        EventBus.getDefault().post(new TouchUp(MyConstans.WAKE_CHANGE));
//                    }
//                }
                isStartDragging = false;
                isEndDragging = false;
                isMinDragging = false;
                isHoueDragging = false;
                break;
        }
        return true;
    }

    private boolean checkOnHourLine(float downX, float downY) {
        if (verHourX == 0) {
            float y = downX * hourK + hourB;
            float x = (downY - hourB) / hourK;
            return Math.abs(downY - y) < 50 || Math.abs(downX - x) < 50;
        } else {
            return Math.abs(downX - verHourX) < 50;
        }
    }

    private boolean checkOnMinLine(float downX, float downY) {
        if (verMinX == 0) {
            float y = downX * minK + minB;
            float x = (downY - minB) / minK;
            return Math.abs(downY - y) < 50 || Math.abs(downX - x) < 50;
        } else {
            return Math.abs(downX - verMinX) < 50;
        }
    }


    private float calDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 判断按下时该点是否在弧线Start（附近）
     */
    private boolean checkOnArcStart(float downX, float downY) {
        float distance = calDistance(downX, downY, mCenterX, mCenterY);
        float degree = calDegreeStartByFirstPosition(downX, downY);
//        return distance > radius - roundWidth * 1.5 && distance < radius + roundWidth * 1.5;
        return distance > radius - roundWidth * 0.6 && distance < radius + roundWidth * 0.6
                && ((degree >= start - 8 && degree <= start + 8));
    }


    /**
     * 判断按下时该点是否在弧线end（附近）
     */
    private boolean checkOnArcEnd(float currentX, float currentY) {
        float distance = calDistance(currentX, currentY, mCenterX, mCenterY);
        float endDegree = getEndDegree();
        float degree = calDegreeEndByFirstPosition(currentX, currentY);
//        return distance > radius - roundWidth * 1.5 && distance < radius + roundWidth * 1.5;
        return distance > radius - roundWidth * 0.6 && distance < radius + roundWidth * 0.6
                && (degree >= endDegree - 8 && degree <= endDegree + 8);
    }

    /**
     * 判断移动过程该点是否还在环形圆弧轨迹上
     */
    private boolean checkInArc(float currentX, float currentY) {
        float distance = calDistance(currentX, currentY, mCenterX, mCenterY);
        return distance > radius - roundWidth * 0.8 && distance < radius + roundWidth * 0.8;
    }

    private float getEndDegree() {
        float degree = -90 + 360 * progress / 100;
        return degree;
    }

    private float calDegreeStartByFirstPosition(float currentX, float currentY) {
        float a1 = 90 - (float) (Math.atan(Math.abs(1.0f * (mCenterX - currentX)) / Math.abs(currentY - mCenterY)) / Math.PI * 180);
//        if (currentX > mCenterX && currentY > mCenterY) {
//        } else if (currentX > mCenterX && currentY < mCenterY) {
//            a1 = 0 - a1;
//        } else if (currentX < mCenterX && currentY > mCenterY) {
//            a1 = -180 - a1;
//        } else if (currentX < mCenterX && currentY < mCenterY) {
//            a1 = -180 + a1;
//        } else if (currentX == mCenterX) {
//            a1 = (currentY < mCenterY ? -90 : 90);
//        } else if (currentY == mCenterY) {
//            a1 = (currentX < mCenterX ? 180 : 0);
//        }
        if (currentX > mCenterX && currentY > mCenterY) {
            a1 = startChange > 0 ? a1 : -360 + a1;
        } else if (currentX > mCenterX && currentY < mCenterY) {
            a1 = startChange > -90 ? 0 - a1 : -360 - a1;
        } else if (currentX < mCenterX && currentY > mCenterY) {
            a1 = startChange > 0 ? 180 - a1 : -180 - a1;
        } else if (currentX < mCenterX && currentY < mCenterY) {
            a1 = startChange > 0 ? 180 + a1 : -180 + a1;
        } else if (currentX == mCenterX) {
            a1 = startChange > -90 ? (currentY < mCenterY ? -90 : 90) : (currentY < mCenterY ? -450 : -270);
        } else if (currentY == mCenterY) {
            a1 = startChange > -90 ? (currentX < mCenterX ? 180 : 0) : (currentX < mCenterX ? -180 : -360);
        }
        return a1;
    }

    private float calDegreeEndByFirstPosition(float currentX, float currentY) {
        float a1 = 90 - (float) (Math.atan(Math.abs(1.0f * (mCenterX - currentX)) / Math.abs(currentY - mCenterY)) / Math.PI * 180);
        if (currentX > mCenterX && currentY > mCenterY) {
        } else if (currentX > mCenterX && currentY < mCenterY) {
            a1 = 0 - a1;
        } else if (currentX < mCenterX && currentY > mCenterY) {
            a1 = 270 - a1 - 90;
        } else if (currentX < mCenterX && currentY < mCenterY) {
            a1 = 270 + a1 - 90;
        } else if (currentX == mCenterX) {
            a1 = currentY < mCenterY ? -90 : 90;
        } else if (currentY == mCenterY) {
            a1 = currentX < mCenterX ? 180 : 0;
        }
        return a1;
    }

    private synchronized float calDegreeZhenByPosition(float currentX, float currentY, float downX, float downY, int type) {
        float a1 = (float) (Math.atan(Math.abs(1.0f * (mCenterX - currentX)) / Math.abs(currentY - mCenterY)) / Math.PI * 180);
        float a2 = (float) (Math.atan(Math.abs(1.0f * (mCenterX - downX)) / Math.abs(downY - mCenterY)) / Math.PI * 180);
        float moveDegree = Math.abs(a2 - a1);
        moveDegree = type == HOUR ? moveDegree : moveDegree / 12;
//        switch (type) {
//            case HOUR:
        float degree = getEndDegree();
        if (currentX > downX) {
            Log.e(TAG, "end 沿着X轴右滑");
            if (currentY > mCenterY) {
                degree = degree - moveDegree;
            } else {
                degree = degree + moveDegree;
            }
        } else if (currentX < downX) {
            Log.e(TAG, "end 沿着X轴左滑");
            if (currentY > mCenterY) {
                degree = degree + moveDegree;
            } else {
                degree = degree - moveDegree;
            }
        } else {
            Log.e(TAG, "end X轴坐标不变的滑动");
            if (currentY > downY) {
                degree = currentX < mCenterX ? degree - moveDegree : degree + moveDegree;
            } else {
                degree = currentX < mCenterX ? degree + moveDegree : degree - moveDegree;
            }
        }
//                break;
//            case MIN:
//
//                break;
//        }
        return degree;
    }


    /**
     * 根据当前位置，计算出进度条END已经转过的角度。
     */
    private synchronized float calDegreeEndByPosition(float currentX, float currentY, float downX, float downY) {
        float a1 = (float) (Math.atan(Math.abs(1.0f * (mCenterX - currentX)) / Math.abs(currentY - mCenterY)) / Math.PI * 180);
        float a2 = (float) (Math.atan(Math.abs(1.0f * (mCenterX - downX)) / Math.abs(downY - mCenterY)) / Math.PI * 180);
        float moveDegree = Math.abs(a2 - a1);
        Log.e(TAG, "move a1 :" + a1 + "down a2 :" + a2);
        float degree = getEndDegree();
        if (currentX > downX) {
            Log.e(TAG, "end 沿着X轴右滑");
            if (currentY > mCenterY) {
                degree = degree - moveDegree;
            } else {
                degree = degree + moveDegree;
            }
        } else if (currentX < downX) {
            Log.e(TAG, "end 沿着X轴左滑");
            if (currentY > mCenterY) {
                degree = degree + moveDegree;
            } else {
                degree = degree - moveDegree;
            }
        } else {
            Log.e(TAG, "end X轴坐标不变的滑动");
            if (currentY > downY) {
                degree = currentX < mCenterX ? degree - moveDegree : degree + moveDegree;
            } else {
                degree = currentX < mCenterX ? degree + moveDegree : degree - moveDegree;
            }
        }
        return degree;
    }

    /**
     * 根据当前位置，计算出进度条START已经转过的角度。
     */
    private float calDegreeStartByPosition(float currentX, float currentY, float downX, float downY) {
        float a1 = (float) (Math.atan(Math.abs(1.0f * (mCenterX - currentX)) / Math.abs(currentY - mCenterY)) / Math.PI * 180);
        float a2 = (float) (Math.atan(Math.abs(1.0f * (mCenterX - downX)) / Math.abs(downY - mCenterY)) / Math.PI * 180);
        float moveDegree = Math.abs(a2 - a1);
        float s = 0;
        if (currentX > downX) {
            Log.e(TAG, "start 沿着X轴右滑");
            if (currentY < mCenterY) {
                start = start + moveDegree;
            } else {
                start = start - moveDegree;
            }
        } else if (currentX < downX) {
            Log.e(TAG, "start 沿着X轴左滑");
            if (currentY < mCenterY) {
                start = start - moveDegree;
            } else {
                start = start + moveDegree;
            }
        } else {
            Log.e(TAG, "start X轴坐标不变的滑动");
            if (currentY > downY) {
                start = currentX < mCenterX ? start - moveDegree : start + moveDegree;
            } else {
                start = currentX < mCenterX ? start + moveDegree : start - moveDegree;
            }
        }
        return start;
    }


    private float checkDegree(float degree) {
        if (degree >= 270) {
            degree = 270;
        } else if (degree <= -90) {
            degree = -90;
        }
        return degree;
    }


    public void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
        invalidate();
    }


    public void setProgress(float start, float progress, boolean isFirst) {
        final float validProgress = checkProgress(progress);
        this.start = start;
        startChange = start;
        this.isFirst = isFirst;
        //动画切换进度值
        new Thread(new Runnable() {
            @Override
            public void run() {
                float oldProgress = AlarmClockView.this.progress;
                for (int i = 1; i <= 100; i++) {
                    AlarmClockView.this.progress = oldProgress + (validProgress - oldProgress) * (1.0f * i / 100);
                    if (i == 100) {
                        isLast = true;
                    }
                    postInvalidate();
                    SystemClock.sleep(20);
                }
            }
        }).start();
    }


    public void setProgressSync(float progress) {
        this.progress = checkProgress(progress);
        isFirst = false;
        isLast = false;
        invalidate();
    }


    //保证progress的值位于[0,max]
    private float checkProgress(float progress) {
        if (progress < -100) {
            return -100;
        }
        return progress > 200 ? 200 : progress;
    }
}
