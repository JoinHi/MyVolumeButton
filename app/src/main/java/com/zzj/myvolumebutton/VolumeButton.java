package com.zzj.myvolumebutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by bjh on 16/7/14.
 */
public class VolumeButton extends View {

    private Paint mPaint;
    private Bitmap button;
    private Bitmap bm_light;
    private Bitmap bm_black;
    private int mWidth;
    private int mHeight;
    private Matrix matrix_button;
    private float degrees;
    private Bitmap bm;
    private Matrix matrix_black;
    private Matrix matrix_light;
    private int count;

    public VolumeButton(Context context) {
        this(context, null);
    }

    public VolumeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        matrix_button = new Matrix();
        matrix_black = new Matrix();
        matrix_light = new Matrix();
        button = BitmapFactory.decodeResource(getResources(), R.mipmap.button);
        bm_light = BitmapFactory.decodeResource(getResources(), R.mipmap.light);
        bm_black = BitmapFactory.decodeResource(getResources(), R.mipmap.black);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY){
            mHeight = DensityUtil.dip2px(getContext(),200);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (count>0){
            degrees = (count+1)*20f;
        }else {
            degrees = 0;
        }

        float centerX = button.getWidth() / 2;
        float centerY = button.getHeight() / 2;
        matrix_button.setTranslate(mWidth / 2 - centerX, mHeight / 2 - centerY);
        matrix_button.postRotate(180f + degrees, mWidth / 2, mHeight / 2);
        canvas.drawBitmap(button, matrix_button, mPaint);

        for (int i = 0;i <15;i++){
            matrix_black.setTranslate(mWidth/2-bm_black.getWidth()/2,
                    mHeight/2-bm_black.getHeight()/2+button.getHeight()/2+bm_black.getWidth()*2);
            matrix_black.postRotate(40 + i * 20, mWidth / 2, mHeight / 2);
            matrix_black.preRotate(-(40 + i * 20), bm_black.getWidth() / 2, bm_black.getHeight()/2);
            canvas.drawBitmap(bm_black, matrix_black,mPaint);
        }
        for (int i = 0;i <count;i++){
            matrix_light.setTranslate(mWidth/2-bm_light.getWidth()/2,
                    mHeight/2-bm_light.getHeight()/2+button.getHeight()/2+bm_black.getWidth()*2);
            matrix_light.postRotate(40 + i * 20, mWidth / 2, mHeight / 2);
            matrix_light.preRotate(-(40 + i * 20), bm_light.getWidth() / 2, bm_light.getHeight()/2);
            canvas.drawBitmap(bm_light, matrix_light,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float flag = -bm_light.getHeight()/2+button.getHeight()/2+bm_black.getWidth()*2;
                if (event.getX()<mWidth/2 - flag){
                    count++;
                }
                if (event.getX()>mWidth/2 + flag){
                    count--;
                }
                if (count<0){
                    count = 0;
                }
                if (count>15){
                    count = 15;
                }
                invalidate();
                break;
        }
        return true;
    }
}
