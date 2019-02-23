package com.king.view.waveview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class WaveView extends View {

    /**
     * 画笔
     */
    private Paint mPaint;

     /**
      * 波纹数
      */
    private int mWaveCount = 2;

    /**
     * 波纹颜色
     */
    private int mWaveColor = 0x3F00B9D2;
    /**
     * 着色器
     */
    private Shader mShader;

    /**
     * 波纹振幅
     */
    private float mWaveAmplitude;

    /**
     * 波纹宽
     */
    private int mWaveWidth;
    /**
     * 波纹高
     */
    private int mWaveHeight;

    /**
     * 波纹X轴偏移最大速度
     */
    private float mMaxSpeed;
    /**
     * 波纹X轴偏移最小速度
     */
    private float mMinSpeed;

    /**
     * 波纹X轴偏移速度
     */
    private float[] mOffsetSpeeds;

    /**
     * 波纹X轴起始偏移量
     */
    private int[] mOffsets;

    /**
     * 波纹Y轴位置
     */
    private float[] mPositions;

    /**
     * 波纹周期因素ω， 最小正周期：T=2π/ω（其中ω必须>0）
     */
    private float mWaveCycleW;

    /**
     * 是否执行动画
     */
    private boolean isAnim = true;

    /**
     * 刷新间隔时间
     */
    private int mRefreshInterval = 15;

    /**
     * 随机
     */
    private Random mRandom;

    /**
     * 密度
     */
    private float mDensity;

    /**
     * 刷新视图
     */
    private final int REFRESH_VIEW = 1;

    /**
     * 是否倒置
     */
    private boolean mIsInverted = false;

    /**
     * 是否竖立
     */
    private boolean mIsVertical = false;

    /**
     * 波纹方向，默认从右到左
     */
    private DIRECTION mDirection  = DIRECTION.LEFT_TO_RIGHT;

    public enum DIRECTION{
        LEFT_TO_RIGHT(1),
        RIGHT_TO_LEFT(2);

        private int mValue;

        DIRECTION(int value){
            this.mValue = value;
        }

        private static DIRECTION getFromInt(int value){
            for(DIRECTION direction : DIRECTION.values()){
                if(direction.mValue == value){
                    return direction;
                }
            }
            return LEFT_TO_RIGHT;
        }

    }

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,@Nullable AttributeSet attrs){

        mRandom = new Random();

        DisplayMetrics displayMetrics = getDisplayMetrics();
        mDensity = displayMetrics.density;
        mWaveAmplitude = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,displayMetrics);
        float maxSpeed = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4,displayMetrics);
        float minSpeed = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,2,displayMetrics);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.WaveView);
        final int n = a.getIndexCount();
        for(int i = 0;i < n;i++){
            int attr = a.getIndex(i);
            if (attr == R.styleable.WaveView_waveCount) {
                mWaveCount = a.getInt(attr,mWaveCount);
            }else if(attr == R.styleable.WaveView_waveColor){
                mWaveColor = a.getColor(attr,mWaveColor);
            }else if(attr == R.styleable.WaveView_waveAmplitude){
                mWaveAmplitude = a.getDimension(attr,mWaveAmplitude);
            }else if(attr == R.styleable.WaveView_waveMaxSpeed){
                maxSpeed = a.getDimension(attr,mMaxSpeed);
            }else if(attr == R.styleable.WaveView_waveMinSpeed){
                maxSpeed = a.getDimension(attr,mMinSpeed);
            }else if(attr == R.styleable.WaveView_waveRefreshInterval){
                mRefreshInterval = a.getInt(attr,mRefreshInterval);
            }else if(attr == R.styleable.WaveView_waveAutoAnim){
                isAnim = a.getBoolean(attr,isAnim);
            }else if(attr == R.styleable.WaveView_waveInverted){
                mIsInverted = a.getBoolean(attr,mIsInverted);
            }else if(attr == R.styleable.WaveView_waveDirection){
                mDirection = DIRECTION.getFromInt(a.getInt(attr,mDirection.mValue));
            }else if(attr == R.styleable.WaveView_waveVertical){
                mIsVertical = a.getBoolean(attr,mIsVertical);
            }
        }

        a.recycle();
        //换算实际速度
        mMaxSpeed = maxSpeed/mDensity;
        mMinSpeed = minSpeed/mDensity;

        mPaint = new Paint();
        updatePaint();
        mOffsetSpeeds = new float[mWaveCount];
        //初始化速度
        for (int i = 0; i < mWaveCount; i++) {
            mOffsetSpeeds[i] = randomSpeed();
        }

        mOffsets = new int[mWaveCount];

    }


    private DisplayMetrics getDisplayMetrics(){
        return getResources().getDisplayMetrics();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int defaultWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getDisplayMetrics());
        int defaultHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,getDisplayMetrics());

        int width = measureHandler(widthMeasureSpec,mIsVertical ? defaultHeight : defaultWidth);
        int height = measureHandler(heightMeasureSpec,mIsVertical ? defaultWidth : defaultHeight);

        setMeasuredDimension(width,height);

    }

    /**
     * 测量处理
     * @param measureSpec
     * @param defaultSize
     * @return
     */
    private int measureHandler(int measureSpec,int defaultSize){

        int result = defaultSize;
        int measureMode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        if(measureMode == MeasureSpec.EXACTLY){
            result = measureSize;
        }else if(measureMode == MeasureSpec.AT_MOST){
            result = Math.min(defaultSize,measureSize);
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWaveWidth = w;
        mWaveHeight = h;
        //波长为总宽度或高度
        int waveLength = mIsVertical ? mWaveHeight : mWaveWidth;
        //根据波纹数初始化波纹的偏移量
        for (int i = 0; i < mWaveCount ; i++) {
            mOffsets[i] = waveLength / mWaveCount * i;
        }

        //Sin函数周期 ω = 2π/T
        mWaveCycleW = (float) (2 * Math.PI / waveLength);

        mPositions = new float[waveLength];

        //正弦曲线  y = Asin(ωx + φ)

        for (int i = 0; i < waveLength; i++) {
            mPositions[i] = (float)(mWaveAmplitude * Math.sin(mWaveCycleW * i));
        }
    }

    /**
     * 更新画笔
     */
    private void updatePaint(){
        mPaint.reset();
        // 去除画笔锯齿
        mPaint.setAntiAlias(true);
        // 设置风格为实线
        mPaint.setStyle(Paint.Style.FILL);

        if(mShader!=null){//不为空则设置着色器支持渐变
            mPaint.setShader(mShader);
        }else{//设置纯色
            mPaint.setColor(mWaveColor);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWave(canvas);
    }

    /**
     * 绘制波纹
     * @param canvas
     */
    private void drawWave(Canvas canvas){
        if(mWaveCount>0){

            int waveLength = mIsVertical ? mWaveHeight : mWaveWidth;
            //遍历波纹数
            for (int i = 0; i < mWaveCount; i++) {

                for (int j = 0; j < waveLength; j++) {
                    int offset = mOffsets[i];
                    //根据偏移量绘制波纹线

                    if(mIsVertical){//纵向
                        if(mIsInverted){//是否倒置
                            canvas.drawLine(0,j, mWaveWidth - mWaveAmplitude - mPositions[(offset + j)%waveLength],j,mPaint);
                        }else {
                            canvas.drawLine(mWaveAmplitude - mPositions[(offset + j)%waveLength] ,j,mWaveWidth,j,mPaint);
                        }
                    }else{//横向
                        if(mIsInverted){//是否倒置
                            canvas.drawLine(j, 0 ,j,mWaveHeight - mWaveAmplitude - mPositions[(offset + j)%waveLength],mPaint);
                        }else {
                            canvas.drawLine(j, mWaveAmplitude - mPositions[(offset + j)%waveLength] ,j,mWaveHeight,mPaint);
                        }
                    }

                }
                //根据方向来进行偏移
                if(mDirection == DIRECTION.LEFT_TO_RIGHT){
                    //偏移
                    mOffsets[i] -= mOffsetSpeeds[i];
                    //超过周期宽度则重置
                    if(mOffsets[i] <= 0){
                        mOffsets[i] = waveLength;
                        //一个周期后，随机一个速度
                        mOffsetSpeeds[i] = randomSpeed();

                    }
                }else{
                    //偏移
                    mOffsets[i] += mOffsetSpeeds[i];
                    //超过周期宽度则重置
                    if(mOffsets[i] >= waveLength){
                        mOffsets[i] = 0;
                        //一个周期后，随机一个速度
                        mOffsetSpeeds[i] = randomSpeed();

                    }
                }

            }

            if(isAnim){//是否显示动画，延迟刷新视图
                mHandler.removeMessages(REFRESH_VIEW);
                mHandler.sendEmptyMessageDelayed(REFRESH_VIEW,mRefreshInterval);
//                postInvalidateDelayed(mRefreshInterval);
            }

        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_VIEW:
                    invalidate();
                    break;
            }
        }
    };


    /**
     * 随机一个速度
     * @return
     */
    private float randomSpeed(){
        if(mMinSpeed!= mMaxSpeed){
            return random(mMinSpeed,mMaxSpeed);
        }

        return mMaxSpeed;
    }

    /**
     * 随机一个范围内的数
     * @param min
     * @param max
     * @return
     */
    private float random(float min,float max){
        return mRandom.nextFloat() * (max - min) + min;
    }


    /**
     * 开始动画
     */
    public void start(){
        if(!isAnim){
            isAnim = true;
            invalidate();
        }
    }

    /**
     * 停止动画
     */
    public void stop(){
        if(isAnim){
            isAnim = false;
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(1);
        isAnim = false;
        super.onDetachedFromWindow();
    }

    /**
     * 设置波纹颜色
     * @param waveColor
     */
    public void setWaveColor(@ColorInt int waveColor) {
        this.mWaveColor = waveColor;
        this.mShader = null;
        updatePaint();
        invalidate();
    }

    /**
     * 设置波纹颜色
     * @param resId
     */
    public void setWaveColorResource(@ColorRes int resId){
        int color = getResources().getColor(resId);
        setWaveColor(color);
    }

    /**
     * 设置波纹着色器
     * @param shader
     */
    public void setWaveShader(Shader shader){
        this.mShader = shader;
        updatePaint();
        invalidate();
    }

    /**
     * 设置波纹最大速度
     * @param maxSpeed
     */
    public void setMaxSpeed(float maxSpeed) {
        this.mMaxSpeed = maxSpeed;
    }

    /**
     * 设置波纹最大速度
     * @param minSpeed
     */
    public void setMinSpeed(float minSpeed) {
        this.mMinSpeed = minSpeed;
    }

    /**
     * 设置波纹最大速度
     * @param resIds
     */
    public void setMaxSpeedResource(@DimenRes int resIds) {
        this.mMaxSpeed = getResources().getDimension(resIds)/mDensity;
    }

    /**
     * 设置波纹最小速度
     * @param resIds
     */
    public void setMinSpeedResource(@DimenRes int resIds) {
        this.mMinSpeed = getResources().getDimension(resIds)/mDensity;
    }

    /**
     * 设置刷新间隔时间(保证每秒刷新超过60帧，让肉眼无感知卡顿现象)
     * @param refreshInterval 默认15ms  帧率(fps) = 1秒钟/刷新间隔时间
     */
    public void setRefreshInterval(int refreshInterval) {
        this.mRefreshInterval = refreshInterval;
    }
}
