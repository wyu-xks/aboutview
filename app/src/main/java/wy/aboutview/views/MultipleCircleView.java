package wy.aboutview.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import wy.aboutview.R;


/**
 * Created by User on 2016/8/19.
 */
public class MultipleCircleView extends View {

    private String UNCONTECTED = "title1";
    private String POOL_SIGNAL = "title2";
    private String NORMAL_SIGNAL = "title3";

    private String relaxText = "content1";
    private String appreciationText = "content2";
    private String attentionText ="content3";
    private String energyText = "content4";
    private final float MARGIN = 10F;
    private final float DASH_WIDTH = 1.F;
    private final float LINE_WIDTH = 3F;
    private final float TEXT_SICE_CENTER = 18;
    private final float TEXT_SIZE_SMALL = 12;

    private final int DATA_LENGTH = 4;

    private final int BACKGROUND_COLOR = Color.parseColor("#034976");
    private final int CIRCLE_BACKGROUND_COLOR = Color.parseColor("#025184");
    private final int RELAX_COLOR = Color.parseColor("#03EAD2");
    private final int APPRECIATION_COLOR = Color.parseColor("#FF2645");
    private final int ATTENTION_COLOR = Color.parseColor("#00B7FF");
    private final int ENERGY_COLOR = Color.parseColor("#FF6000");
    private final int DASH_1_COLOR = Color.parseColor("#00B7EE");
    private final int CENTER_TEXT_COLOR = Color.parseColor("#0D86A3");
    private final int SMALL_TEXT_COLOR = Color.parseColor("#95F9FF");

    private final int DASH_1_LENGTH = 3;
    private final int DASH_2_LENGTH = 6;
    private final int START_ANGLE = -90;
    private final int START_ANGLE2 = 183;
    private final int SWEPED_ANGLE = 270;
    private final int SWEPED_ANGLE2 = 87;

    private  float lineWidth;
    private  float dashWidth;

    private Paint dashPaint;
    private Paint linePaint;
    private Paint dashPaint2;
    private Paint textPaint;

    private float density;
    private float textSizeCenter;
    private float textSizeSmall;

    private float radius;
    private float oneBlankWidth;
    private int w;
    private int h;

    private int data[];
    private float currentData[];
    private int dataInterval[];

    private float margin ;

    private PathEffect pathEffect1;
    private PathEffect pathEffect2;

    private RectF rectFDashLong;
    private RectF rectFDashShort;
    private RectF rectFRelax;
    private RectF rectFAppreciation;
    private RectF rectFAttention;
    private RectF rectFEnergy;

    private String centerText;

    private boolean isAnimationing;


    private int noiseThreshold;

    public MultipleCircleView(Context context) {
        this(context, null);
    }

    public MultipleCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        density = context.getResources().getDisplayMetrics().density;
        initText(context);
        margin = density*MARGIN;
        textSizeCenter = TEXT_SICE_CENTER*density;
        textSizeSmall = TEXT_SIZE_SMALL*density;

        centerText = "";

        textPaint = new TextPaint();
        dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);


        lineWidth = density*LINE_WIDTH;
        dashWidth = density*DASH_WIDTH;

        dashPaint.setStrokeWidth(dashWidth);
        linePaint.setStrokeWidth(lineWidth);
        dashPaint2.setStrokeWidth(lineWidth);

        dashPaint.setStyle(Paint.Style.STROKE);
        linePaint.setStyle(Paint.Style.STROKE);
        dashPaint2.setStyle(Paint.Style.STROKE);

        pathEffect1 = new DashPathEffect(new float[]{DASH_1_LENGTH*density/2, DASH_1_LENGTH*density, DASH_1_LENGTH*density/2, DASH_1_LENGTH*density},1);
        pathEffect2 = new DashPathEffect(new float[]{DASH_2_LENGTH*density, DASH_2_LENGTH*density/2, DASH_2_LENGTH*density, DASH_2_LENGTH*density/2}, 1);

        dashPaint2.setPathEffect(pathEffect2);

        data = new int[DATA_LENGTH];
        currentData = new float[DATA_LENGTH];
        dataInterval = new int[DATA_LENGTH];
    }

    private void initText(Context context){
        UNCONTECTED = "status1";
        POOL_SIGNAL ="status2";
        NORMAL_SIGNAL ="status3";

        relaxText = "content1";
        appreciationText = "content2";
        attentionText = "content3";
        energyText = "content4";
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        radius = (float) Math.min(w, h)/2 - margin;
        oneBlankWidth = radius/10;

        initRect(w, h);
    }

    private void initRect(int w, int h){
        float radius1 = radius/2+3*lineWidth;
        noiseThreshold = 5;
        rectFDashLong = new RectF(w/2-radius1, h/2-radius1, w/2+radius1, h/2+radius1);
        rectFDashShort = rectFDashLong;
        radius1 += oneBlankWidth;
        rectFEnergy = new RectF(w/2-radius1, h/2-radius1, w/2+radius1, h/2+radius1);

        radius1 += oneBlankWidth;
        rectFAttention = new RectF(w/2-radius1, h/2-radius1, w/2+radius1, h/2+radius1);

        radius1 += oneBlankWidth;
        rectFAppreciation = new RectF(w/2-radius1, h/2-radius1, w/2+radius1, h/2+radius1);

        radius1 += oneBlankWidth;
        rectFRelax = new RectF(w/2-radius1, h/2-radius1, w/2+radius1, h/2+radius1);


    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawData(canvas);
    }

    private void drawBackground(Canvas canvas){
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(BACKGROUND_COLOR);
        canvas.drawCircle(w/2, h/2, radius+margin, linePaint);
        linePaint.setStyle(Paint.Style.STROKE);

        linePaint.setStrokeWidth(oneBlankWidth);
        linePaint.setColor(CIRCLE_BACKGROUND_COLOR);
        canvas.drawCircle(w/2, h/2, radius/2, linePaint);

        dashPaint.setColor(DASH_1_COLOR);
        dashPaint.setStrokeWidth(dashWidth);
        dashPaint.setPathEffect(pathEffect1);
        canvas.drawCircle(w/2, h/2, radius/2-oneBlankWidth/2-dashWidth, dashPaint);
        dashPaint2.setColor(CIRCLE_BACKGROUND_COLOR);
        canvas.drawArc(rectFDashLong, START_ANGLE, SWEPED_ANGLE, false, dashPaint2);

        dashPaint.setColor(CIRCLE_BACKGROUND_COLOR);
        canvas.drawArc(rectFDashShort, START_ANGLE2, SWEPED_ANGLE2, false, dashPaint);



        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(CIRCLE_BACKGROUND_COLOR);
        canvas.drawArc(rectFEnergy, START_ANGLE, SWEPED_ANGLE, false, linePaint);
        canvas.drawArc(rectFAttention, START_ANGLE, SWEPED_ANGLE, false, linePaint);
        canvas.drawArc(rectFAppreciation, START_ANGLE, SWEPED_ANGLE, false, linePaint);
        canvas.drawArc(rectFRelax, START_ANGLE, SWEPED_ANGLE, false, linePaint);

        textPaint.setTextSize(textSizeCenter);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float height = fontMetrics.ascent+fontMetrics.descent+fontMetrics.leading;
        float width = textPaint.measureText(centerText);
        textPaint.setColor(CENTER_TEXT_COLOR);
        canvas.drawText(centerText, (w-width)/2, (h-height)/2, textPaint);

        textPaint.setTextSize(textSizeSmall);
        textPaint.setColor(SMALL_TEXT_COLOR);
        fontMetrics = textPaint.getFontMetrics();
        height = fontMetrics.ascent+fontMetrics.descent+fontMetrics.leading;
        width = textPaint.measureText("0");
        canvas.drawText("0", (w-width)/2, h/2-radius, textPaint);

        float fraction = 1.1f;
        width = textPaint.measureText(relaxText);
        canvas.drawText(relaxText, (w/2-width*fraction), margin+oneBlankWidth*0.8f, textPaint);
        dashPaint.setColor(RELAX_COLOR);
        canvas.drawArc(rectFRelax, START_ANGLE2, SWEPED_ANGLE2-getSwipedAngle(width, radius-margin-oneBlankWidth), false, dashPaint);

        width = textPaint.measureText(appreciationText);
        canvas.drawText(appreciationText, (w/2-width*fraction), margin+oneBlankWidth*1.8f, textPaint);
        dashPaint.setColor(APPRECIATION_COLOR);
        canvas.drawArc(rectFAppreciation, START_ANGLE2, SWEPED_ANGLE2-getSwipedAngle(width, radius-margin-2*oneBlankWidth), false, dashPaint);

        width = textPaint.measureText(attentionText);
        canvas.drawText(attentionText, (w/2-width*fraction), margin+oneBlankWidth*2.8f, textPaint);
        dashPaint.setColor(ATTENTION_COLOR);
        canvas.drawArc(rectFAttention, START_ANGLE2, SWEPED_ANGLE2-getSwipedAngle(width, radius-margin-3*oneBlankWidth), false, dashPaint);

        width = textPaint.measureText(energyText);
        canvas.drawText(energyText, (w/2-width*fraction), margin+oneBlankWidth*3.8f, textPaint);
        dashPaint.setColor(ENERGY_COLOR);
        canvas.drawArc(rectFEnergy, START_ANGLE2, SWEPED_ANGLE2-getSwipedAngle(width, radius-margin-4*oneBlankWidth), false, dashPaint);

        width = textPaint.measureText("100");
        canvas.drawText("100", w/2-radius-width/2, h/2, textPaint);
    }

    private void drawData(Canvas canvas){
        float swipedAngle = currentData[0]/100f*SWEPED_ANGLE;
        linePaint.setColor(RELAX_COLOR);
        canvas.drawArc(rectFRelax, START_ANGLE, swipedAngle, false, linePaint);

        swipedAngle = currentData[1]/100f*SWEPED_ANGLE;
        linePaint.setColor(APPRECIATION_COLOR);
        canvas.drawArc(rectFAppreciation, START_ANGLE, swipedAngle, false, linePaint);

        swipedAngle = currentData[2]/100f*SWEPED_ANGLE;
        linePaint.setColor(ATTENTION_COLOR);
        canvas.drawArc(rectFAttention, START_ANGLE, swipedAngle, false, linePaint);

        swipedAngle = currentData[3]/100f*SWEPED_ANGLE;
        linePaint.setColor(ENERGY_COLOR);
        canvas.drawArc(rectFEnergy, START_ANGLE, swipedAngle, false, linePaint);
    }



    private float getSwipedAngle(float width, float radius){
        return (float)(Math.atan(width/radius)*180/ Math.PI);
    }

    public void setData(int data[], int noisze){
        if(null==data || data.length<this.data.length || isAnimationing){
            return;
        }

        if(noisze>noiseThreshold){
            centerText = POOL_SIGNAL;
        }else if(noisze>=0){
            centerText = NORMAL_SIGNAL;
        }else {
            centerText = UNCONTECTED;
        }

        for(int i=0;i<this.data.length;i++){
            dataInterval[i] = data[i]-this.data[i];
            this.data[i] = data[i]>100?100:data[i];
            this.data[i] = data[i]<0?0:this.data[i];
        }

        startAnimation();
    }

    private void startAnimation(){
        isAnimationing = true;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for(int i=0;i<currentData.length;i++){
                    currentData[i] = data[i] -dataInterval[i]*(1-(Float)animation.getAnimatedValue());
                }
                invalidate();
                if(1.0f==(Float)animation.getAnimatedValue()){
                    isAnimationing = false;
                }
            }
        });
        valueAnimator.setDuration(1500);
        valueAnimator.setRepeatCount(0);
        valueAnimator.start();
    }

    public void setNoiseThreshold(int noiseThreshold) {
        this.noiseThreshold = noiseThreshold;
    }

}
