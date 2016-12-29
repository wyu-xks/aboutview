package wy.aboutview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wy.aboutview.R;

/**
 * Created by Xie on 2016/12/29.
 */
public class WaveView extends View {

    private int mProgress;//进度
    private int mTimeStep = 10;//时间间隔
    private int mSpeed = 5;//波单次移动的距离
    private int mViewHeight;//视图宽高
    private int mViewWidth;//视图宽度
    private int mLevelLine;// 基准线


    private int mWaveLength;//波长 暂定view宽度为一个波长
    private int mStrokeWidth;//园的线宽
    private RectF rectF;//圆环区域
    private int mWaveHeight;//波峰高度
    private int mLeftWaveMoveLength;//波平移的距离，用来控制波的起点位置
    private int mWaveColor;//波的颜色
    private Paint mPaint;//画笔
    private Paint mCirclePaint;//圆环画笔
    private Paint mBorderPaint;//边界画笔
    private int mBorderWidth = 4;//边界宽度
    private Paint mTextPaint;//文字画笔
    private Path mPath;//绘画线
    private List<Point> mPoints;//点的集合
    private boolean isMeasure = false;//是否已测量过
    private boolean isCircle = false;//是否圆形默认false，可属性代码设置
    //处理消息
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            initWaveMove();
        }
    };

    /**
     * 初始化波的移动
     */
    private void initWaveMove() {
        mLeftWaveMoveLength += mSpeed;//波向右移动距离增加mSpeed;
        if (mLeftWaveMoveLength >= mWaveLength) {//当增加到一个波长时回复到0
            mLeftWaveMoveLength = 0;
        }
        invalidate();

    }

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getAttr(context, attrs, defStyleAttr);
        init();

    }

    /**
     * 初始化画笔
     */
    private void init() {
        mPoints = new ArrayList<Point>();
        //波浪轨迹画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mWaveColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);


        mPath = new Path();


        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(48);


        //圆环画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        //边界线画笔
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mWaveColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);


    }

    /**
     * 获取自定义的属性值
     *
     * @param attrs
     */
    private void getAttr(Context context, AttributeSet attrs, int defStyle) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveView, defStyle, 0);

        mWaveColor = a.getColor(R.styleable.WaveView_wave_color, Color.RED);
        isCircle = a.getBoolean(R.styleable.WaveView_wave_circle, false);
        a.recycle();

    }


    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasure && Math.abs(getMeasuredHeight() - getMeasuredWidth()) < 50) {//只计算一次就够了 ，relativelayout的时候要绘制两次 加个宽高判断
            mViewHeight = getMeasuredHeight();
            mViewWidth = getMeasuredWidth();
            mLevelLine = mViewHeight;  //初始化波的准位线       起始位视图最底部
            {
                mLevelLine = mViewHeight * (100 - mProgress) / 100;
                if (mLevelLine < 0) mLevelLine = 0;
            }
            //计算波峰值
            mWaveHeight = mViewHeight / 20;//波峰暂定为view高度的1/20，如果需要设置 可设置set方法赋值;
            mWaveLength = getMeasuredWidth();

            //计算所有的点 这里取宽度为整个波长  往左再延伸一个波长 两个波长则需要9个点
            for (int i = 0; i < 9; i++) {
                int y = 0;
                switch (i % 4) {
                    case 0:
                        y = mViewHeight;
                        break;
                    case 1:
                        y = mViewHeight + mWaveHeight;
                        break;
                    case 2:
                        y = mViewHeight;
                        break;
                    case 3:
                        y = mViewHeight - mWaveHeight;
                        break;
                }
                Point point = new Point(-mWaveLength + i * mWaveLength / 4, y);
                mPoints.add(point);
            }
            /**
             * 计算圆环宽度
             */
            int mIncircleRadius = mViewHeight < mViewWidth ? mViewHeight / 2 : mViewWidth / 2;//内切圆半径

            int mcircumcircleRadius = (int) (Math.sqrt((float) (Math.pow(mViewHeight / 2, 2) + Math.pow(mViewWidth / 2, 2))) + 0.5);//外接圆半径
            int radius = mcircumcircleRadius / 2 + mIncircleRadius / 2;

            rectF = new RectF(mViewWidth / 2 - radius, mViewHeight / 2 - radius, mViewWidth / 2 + radius, mViewHeight / 2 + radius);
            mStrokeWidth = mcircumcircleRadius - mIncircleRadius;
            mCirclePaint.setStrokeWidth(mStrokeWidth);//线是有宽度的  采用了这种方式画圆环
            isMeasure = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 绘制线条
         */
        mPath.reset();
        int i = 0;
        Point p;
        mPath.moveTo(mPoints.get(0).x + mLeftWaveMoveLength, mPoints.get(0).y - mViewHeight * mProgress / 100);
        for (; i < mPoints.size() - 2; i += 2) {
            mPath.quadTo(mPoints.get(i + 1).x + mLeftWaveMoveLength, mPoints.get(i + 1).y - mViewHeight * mProgress / 100, mPoints.get(i + 2).x + mLeftWaveMoveLength, mPoints.get(i + 2).y - mViewHeight * mProgress / 100);
        }
        mPath.lineTo(mPoints.get(i).x + mLeftWaveMoveLength, mViewHeight);
        mPath.lineTo(mPoints.get(0).x + mLeftWaveMoveLength, mViewHeight);
        mPath.close();
        /**
         * 绘制轨迹
         */
        canvas.drawPath(mPath, mPaint);
        Rect rect = new Rect();

        String progress = String.format("%d%%", mProgress);
        mTextPaint.getTextBounds(progress, 0, progress.length(), rect);
        int textHeight = rect.height();
        if (mProgress >= 50)//如果进度达到50 颜色变为白色，没办法啊，进度在中间 不变颜色看不到
            mTextPaint.setColor(Color.WHITE);
        else
            mTextPaint.setColor(mWaveColor);
        canvas.drawText(progress, 0, progress.length(), mViewWidth / 2, mViewHeight / 2 + textHeight / 2, mTextPaint);

        if (isCircle) {
            /**
             * 绘制圆环
             */

            canvas.drawArc(rectF, 0, 360, true, mCirclePaint);
            Paint circlePaint = new Paint();
            circlePaint.setStrokeWidth(5);
            circlePaint.setColor(Color.WHITE);
            circlePaint.setAntiAlias(true);
            circlePaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mViewHeight / 2, circlePaint);
            /**
             * 绘制边界
             */

            mBorderPaint.setStrokeWidth(mBorderWidth / 2);
            canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mViewHeight / 2 - mBorderWidth / 2, mBorderPaint);
        } else {
            /**
             * 绘制矩形边框
             */
            canvas.drawRect(0, 0, mViewWidth, mViewHeight, mBorderPaint);
        }
        //
        handler.sendEmptyMessageDelayed(0, mTimeStep);
    }

    /**
     * 设置进度  基准线
     *
     * @param mProgress
     */
    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        mLevelLine = (100 - mProgress) * mViewHeight / 100;
    }

    /**
     * 设置是否为圆形
     *
     * @param circle
     */
    public void setCircle(boolean circle) {
        isCircle = circle;
    }
}
