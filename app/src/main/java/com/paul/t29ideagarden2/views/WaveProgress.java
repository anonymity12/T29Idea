package com.paul.t29ideagarden2.views;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.util.Constants;
import com.paul.t29ideagarden2.util.ViewUtil;

/**
 * Created by paul on 2018/8/11
 * last modified at 19:57.
 * Desc:
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
    private float mRightWaveOffset;
    private boolean isR2L;
    private boolean lockWave;
    private boolean antiAlias;
    private float mMaxValue;
    private float mValue;
    private float mPrecent;
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

    private ValueAnimator mProgressBarAnimator,mDarkWaveAnimator,mLightWaveAnimator;
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

    }

    private void initPath() {

    }



}
