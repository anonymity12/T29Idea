package com.paul.t29ideagarden2.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.paul.t29ideagarden2.BuildConfig;
import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.util.Constants;
import com.paul.t29ideagarden2.util.ViewUtil;

/**
 * Created by paul on 2018/8/11
 * last modified at 19:57.
 * Desc:
2018-08-26 04:49:12 添加 点的初始化设计 getPoint（boolean isR2L, float waveWidth）
2018-08-26 08:05:39 complete: drawWave
 */

public class WaveProgress extends View {
    public static final String TAG = WaveProgress.class.getSimpleName();
    public static final int R2L = 1;
    public static final int L2R = 0;
    private int mDefaultSize;
    private Point mCenterPoint;
    private float mRadius;
    private RectF mRectF;
    private float mLightWaveOffset;
    private float mDarkWaveOffset;
    private boolean isR2L;
    private boolean lockWave;
    private boolean antiAlias;
    private float mMaxValue;
    private float mValue;
    private float mPercent;
    //绘制提示
    private TextPaint mHintPaint;
    private CharSequence mHint;
    private int mHintColor;
    private float mHintSize;

    private Paint mPercentPaint;
    private float mValueSize;
    private int mValueColor;

    //圆环宽度
    private float mCircleWidth;
    //圆环
    private Paint mCirclePaint;
    //圆环颜色
    private int mCircleColor;
    //背景圆环颜色
    private int mBgCircleColor;

    private Path mWaveLimitPath;
    private Path mWavePath;
    private float mWaveHeight;
    private int mWaveNum;
    private Paint mWavePaint;
    private int mDarkWaveColor;
    private int mLightWaveColor;

    private Point[] mDarkPoints;
    private Point[] mLightPoints;

    private int mAllPointCount;
    private int mHalfPointCount;

    private ValueAnimator mProgressAnimator,mDarkWaveAnimator,mLightWaveAnimator;
    private long mDarkWaveAnimTime, mLightWaveAnimTime;

    public WaveProgress(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attrs){
        mDefaultSize = ViewUtil.dipToPx(context, Constants.DEFAULT_SIZE);
        mRectF = new RectF();
        mCenterPoint = new Point();

        initAttrs(context, attrs);
        initPaint();
        initPath();
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.WaveProgress);
        antiAlias = typedArray.getBoolean(R.styleable.WaveProgress_antiAlias, true);
        mDarkWaveAnimTime = typedArray.getInt(R.styleable.WaveProgress_darkWaveAnimTime, Constants.DEFAULT_ANIM_TIME);
        mLightWaveAnimTime = typedArray.getInt(R.styleable.WaveProgress_lightWaveAnimTime, Constants.DEFAULT_ANIM_TIME);
        mValue = typedArray.getInt(R.styleable.WaveProgress_value, 50);
        mMaxValue = typedArray.getInt(R.styleable.WaveProgress_maxValue, 100);
        mValueColor = typedArray.getColor(R.styleable.WaveProgress_valueColor, Color.BLACK);
        mValueSize = typedArray.getDimension(R.styleable.WaveProgress_valueSize, Constants.DEFAULT_VALUE_SIZE);

        mHint = typedArray.getString(R.styleable.WaveProgress_hint);//tt: notice here no defValue
        mHintColor = typedArray.getColor(R.styleable.WaveProgress_hintColor,Color.BLACK);
        mHintSize = typedArray.getDimension(R.styleable.WaveProgress_hintSize, Constants.DEFAULT_HINT_SIZE);

        mCircleWidth = typedArray.getDimension(R.styleable.WaveProgress_circleWidth, Constants.DEFAULT_ARC_WIDTH);
        mCircleColor = typedArray.getColor(R.styleable.WaveProgress_circleColor, Color.GREEN);
        mBgCircleColor = typedArray.getColor(R.styleable.WaveProgress_bgCircleColor, Color.GRAY);

        mWaveHeight = typedArray.getInt(R.styleable.WaveProgress_waveHeight, Constants.DEFAULT_WAVE_HEIGHT);
        mWaveNum = typedArray.getInt(R.styleable.WaveProgress_waveNum, 4);

        mDarkWaveColor = typedArray.getColor(R.styleable.WaveProgress_darkWaveColor, getResources().getColor(android.R.color.holo_blue_dark));
        mLightWaveColor = typedArray.getColor(R.styleable.WaveProgress_lightWaveColor, getResources().getColor(android.R.color.holo_green_light));

        isR2L = typedArray.getInt(R.styleable.WaveProgress_lightWaveDirect,R2L)== R2L;
        lockWave = typedArray.getBoolean(R.styleable.WaveProgress_lockWave, false);

        typedArray.recycle();
    }

    private void initPaint(){
        mHintPaint = new TextPaint();
        mHintPaint.setAntiAlias(antiAlias);
        mHintPaint.setTextSize(mHintSize);
        mHintPaint.setColor(mHintColor);
        mHintPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(antiAlias);
        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(antiAlias);
        mWavePaint.setStyle(Paint.Style.FILL);

        mPercentPaint = new Paint();
        mPercentPaint.setAntiAlias(antiAlias);
        mPercentPaint.setTextAlign(Paint.Align.CENTER);
        mPercentPaint.setColor(mValueColor);
        mPercentPaint.setTextSize(mValueSize);
    }

    private void initPath() {
        mWaveLimitPath = new Path();
        mWavePath = new Path();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(ViewUtil.measure(widthMeasureSpec, mDefaultSize), ViewUtil.measure(heightMeasureSpec, mDefaultSize));
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        //在长和宽中取较小的形成一个能好好显示的正方形
        int minSize = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 2 * (int) mCircleWidth,
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - 2 * (int) mCircleWidth);
        mRadius = minSize / 2;
        mCenterPoint.x = getMeasuredWidth() / 2;
        mCenterPoint.y = getMeasuredHeight() / 2;
        //绘制圆弧的边界
        mRectF.left = mCenterPoint.x - mRadius - mCircleWidth / 2;
        mRectF.top = mCenterPoint.y - mRadius - mCircleWidth / 2;
        mRectF.right = mCenterPoint.x + mRadius + mCircleWidth / 2;
        mRectF.bottom = mCenterPoint.y + mRadius + mCircleWidth / 2;
        //Complete 完成下列三个 2018-08-22 15:11:41
        initWavePoints();
        //开始动画
        setValue(mValue);
        startWaveAnimator();
    }
    private void initWavePoints(){
        float waveWidth = (mRadius * 2) / mWaveNum;
        mAllPointCount = 8 * mWaveNum + 1;/*why + 1?*/
        mHalfPointCount = mAllPointCount / 2;
        mDarkPoints = getPoint(false, waveWidth);
        mLightPoints = getPoint(isR2L, waveWidth);
    }
    private Point[] getPoint(boolean isR2L, float waveWidth){
        Point [] points = new Point[mAllPointCount];
        points[mHalfPointCount] = new Point((int)(mCenterPoint.x + (isR2L?mRadius:-mRadius)), mCenterPoint.y);
        /*在球内的波浪点*/
        for (int i = mHalfPointCount; i < mAllPointCount; i ++){
            float xAnchor =points[mHalfPointCount].x + (i / 4 - mWaveNum) * waveWidth;
            points[i + 1] = new Point((int) (waveWidth * 1 / 4 + xAnchor), (int) (mCenterPoint.y - mWaveHeight));
            points[i + 2] = new Point((int) (waveWidth * 2 / 4 + xAnchor), (int) (mCenterPoint.y));
            points[i + 3] = new Point((int) (waveWidth * 3 / 4 + xAnchor), (int) (mCenterPoint.y + mWaveHeight));
            points[i + 4] = new Point((int) (waveWidth * 4 / 4 + xAnchor), (int) (mCenterPoint.y));
        }
        /*在球外的波澜点*/
        for(int i = 0; i < mHalfPointCount; i ++){
            int reversePartnerPoint = mAllPointCount - i - 1;
            points[i] = new Point((isR2L ? 2 : 1) * points[mHalfPointCount].x - points[reversePartnerPoint].x, points[mHalfPointCount].y * 2 - points[reversePartnerPoint].y);
        }
        /*数组取反，后用？*/
        return isR2L ? ViewUtil.reverse(points) : points;
    }   
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawCircle(canvas);
        drawLightWave(canvas);
        drawDarkWave(canvas);
        drawProgress(canvas);
    }
    private void drawCircle(Canvas canvas){
        canvas.save();
        canvas.rotate(270, mCenterPoint.x, mCenterPoint.y);
        int currentAngle = (int) (360 * mPercent);
        /*background circle*/
        mCirclePaint.setColor(mBgCircleColor);
        canvas.drawArc(mRectF, currentAngle, 360 - currentAngle, false, mCirclePaint);
        /*foreground circle*/
        mCirclePaint.setColor(mCircleColor);
        canvas.drawArc(mRectF, 0, currentAngle, false, mCirclePaint);
        canvas.restore();
    }
    private void drawDarkWave(Canvas canvas){
        mWavePaint.setColor(mDarkWaveColor);
        drawWave(canvas, mWavePaint, mDarkPoints, mDarkWaveOffset);
    }
    private void drawLightWave(Canvas canvas){
        mWavePaint.setColor(mLightWaveColor);
        drawWave(canvas, mWavePaint, mLightPoints, isR2L? -mLightWaveOffset : mLightWaveOffset);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void drawWave(Canvas canvas, Paint paint, Point [] points, float waveOffset){
        /*思路是画出波浪路径图，画出圆的路径（path），取他们的交集进行显示*/
        /* 1. reset(): Clear any lines and curves from the path, making it empty.*/
        mWaveLimitPath.reset();
        mWavePath.reset();
        /* 2. 设定波浪高度，aka 位置*/
        float waveHeight = lockWave ? 0 : mRadius - 2 * mRadius * mPercent;
        /* 3. 开始绘制*/
        mWavePath.moveTo(points[0].x + waveOffset, points[0].y + waveHeight);
        for (int i = 1; i < mAllPointCount; i += 2){
            /*之所以+= 2 是因为mAllPointCount 是8倍的波浪数*/
            mWavePath.quadTo(points[i].x + waveOffset, points[i].y + waveHeight, points[i + 1].x + waveOffset, points[i + 1].y + waveHeight);
        }
        /*使用lineTo（）进行mWavePath路径合并*/
        mWavePath.lineTo(points[mAllPointCount - 1].x, points[mAllPointCount - 1].y + mRadius);
        mWavePath.lineTo(points[0].x, points[0].y + mRadius);
        mWavePath.close();
        /* 使用addCircle()为mWaveLimitPath制作一个圆形路径*/
        mWaveLimitPath.addCircle(mCenterPoint.x, mCenterPoint.y, mRadius, Path.Direction.CW);
        /* 使用op对两个路径进行操作： 交集*/
        mWaveLimitPath.op(mWavePath, Path.Op.INTERSECT);
        /* 绘制的路径估计仅仅是圆和圆内部的波浪线*/
        canvas.drawPath(mWaveLimitPath, paint);
    }

    //前一次绘制时的进度
    private float mPrePercent;
    //当前进度值
    private String mPercentValue;

    /* 开始绘制进度文字*/
    private void drawProgress(Canvas canvas) {
        float y = mCenterPoint.y - (mPercentPaint.descent() + mPercentPaint.ascent()) / 2;
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "mPercent = " + mPercent + "; mPrePercent = " + mPrePercent);
        }
        // complete: 2018/8/28 翌日早起
        if (mPrePercent == 0.0f || Math.abs(mPercent - mPrePercent) >= 0.01f) {
            mPercentValue = String.format(".0f%%", mPercent * 100);
            mPrePercent = mPercent;
        }
        canvas.drawText(mPercentValue, mCenterPoint.x, y, mPercentPaint);
        /* draw hint text*/
        if (mHint != null) {
            float hy = mCenterPoint.y * 2 / 3 - (mHintPaint.descent() + mHintPaint.ascent()) / 2;
            canvas.drawText(mHint.toString(), mCenterPoint.x, hy, mHintPaint);
        }
    }
    public float getMaxValue() {
        return mMaxValue;
    }
    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }
    public float getValue(){
        return mValue;
    }

    public void setValue(float value){
        if (value > mMaxValue){
            value = mMaxValue;
        }
        float start = mPercent;
        float end = value / mMaxValue;
        Log.d(TAG, "setValue: value = " + value + "; start = " + start + "; end = " + end);
        startAnimator(start, end, mDarkWaveAnimTime);
    }
    
    private void startAnimator(final float start, float end, long animTime){
        Log.d(TAG, "startAnimator, value = " + mValue + "; start = " + start + "; end = " + end + ";time = " + animTime);

        if (start == 0 && end == 0) {
            /*此时不需要启动动画*/
            return;
        }
        mProgressAnimator = ValueAnimator.ofFloat(start, end);
        mProgressAnimator.setDuration(animTime);
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            public void onAnimationUpdate(ValueAnimator animation){
                mPercent = (float) animation.getAnimatedValue();
                if(mPercent == 0.0f || mPercent == 1.0f){
                    stopWaveAnimator();
                }else{
                    startWaveAnimator();
                }
                mValue = mPercent * mMaxValue;
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "onAnimationUpdate: percent = " + mPercent + "; value = " + mValue);
                }
                invalidate();
            }
        });
        mProgressAnimator.start();
    }
    private void startWaveAnimator(){
        startLightWaveAnimator();
        startDarkWaveAnimator();
    }
    private void stopWaveAnimator(){
        if(mDarkWaveAnimator != null && mDarkWaveAnimator.isRunning()){
            mDarkWaveAnimator.cancel();
            mDarkWaveAnimator.removeAllUpdateListeners();
            mDarkWaveAnimator = null;
        }
        if (mLightWaveAnimator != null && mLightWaveAnimator.isRunning()) {
            mLightWaveAnimator.cancel();
            mDarkWaveAnimator.removeAllUpdateListeners();
            mLightWaveAnimator = null;
        }
    }
// complete: 2018/9/03 startLight/Dark WaveAnimator
    private void startLightWaveAnimator(){
        if (mLightWaveAnimator != null && mLightWaveAnimator.isRunning()) {
            return;
        }
        mLightWaveAnimator = ValueAnimator.ofFloat(0, 2 * mRadius);
        mLightWaveAnimator.setDuration(mLightWaveAnimTime);
        mLightWaveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLightWaveAnimator.setInterpolator(new LinearInterpolator());
        mLightWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                mLightWaveOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mLightWaveAnimator.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animator){
                mLightWaveOffset = 0;
            }
            @Override
            public void onAnimationEnd(Animator animation){

            }
            @Override
            public void onAnimationCancel(Animator animation){

            }
            @Override
            public void onAnimationRepeat(Animator animation){

            }
        });
        mLightWaveAnimator.start();
    }
    private void startDarkWaveAnimator(){
        if (mDarkWaveAnimator != null && mDarkWaveAnimator.isRunning()) {
            return;
        }
        mDarkWaveAnimator = ValueAnimator.ofFloat(0, 2 * mRadius);
        mDarkWaveAnimator.setDuration(mDarkWaveAnimTime);
        mDarkWaveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mDarkWaveAnimator.setInterpolator(new LinearInterpolator());
        mDarkWaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                mDarkWaveOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mDarkWaveAnimator.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animation){
                mDarkWaveOffset = 0;
            }
                        @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mDarkWaveAnimator.start();
    }
    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        stopWaveAnimator();
        if (mProgressAnimator != null && mProgressAnimator.isRunning()) {
            mProgressAnimator.cancel();
            mProgressAnimator.removeAllUpdateListeners();
            mProgressAnimator = null;
        }
    }
}
