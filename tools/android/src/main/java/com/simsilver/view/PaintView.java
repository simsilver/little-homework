package com.simsilver.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 */
public class PaintView extends View {
    private int mBGColor, mPenColor, mViewWidth, mViewHeight;
    private static final int EDGE = 400;
    private float mRateW, mRateH, mMinRate;
    private int mPicW, mPicH;
    private Paint paint;
    private Canvas cacheCanvas;
    private Bitmap cachedBitmap;
    private Path path;
    private Rect srcRect, dstRect;


    public Bitmap getCachedBitmap() {
        return cachedBitmap;
    }

    public PaintView(Context context, int bgColor, int penColor) {
        super(context);
        mBGColor = bgColor;
        mPenColor = penColor;
    }

    private void calc() {
        int width = getWidth();
        int height = getHeight();
        if (width != mViewWidth || height != mViewHeight || cacheCanvas == null || cachedBitmap == null) {
            mViewWidth = width;
            mViewHeight = height;
            int mTimesW = 1, mTimesH = 1;
            if (mViewWidth > mViewHeight) {
                mTimesW = (mViewWidth + mViewHeight / 2) / mViewHeight;
            } else {
                mTimesH = (mViewHeight + mViewWidth / 2) / mViewWidth;
            }
            mPicW = EDGE * mTimesW;
            mPicH = EDGE * mTimesH;
            mRateW = mPicW * 1.0f / mViewWidth;
            mRateH = mPicH * 1.0f / mViewHeight;
            mMinRate = mRateW > mRateH ? mRateH : mRateW;
            cachedBitmap = Bitmap.createBitmap(mPicW, mPicH, Bitmap.Config.ARGB_8888);
            cacheCanvas = new Canvas(cachedBitmap);
            cacheCanvas.drawColor(mBGColor);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(mPenColor);
            path = new Path();
            srcRect = new Rect();
            dstRect = new Rect();
        }
    }

    public void clear() {
        if (cacheCanvas != null) {
            paint.setColor(mPenColor);
            cacheCanvas.drawColor(mBGColor);
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        calc();
        cacheCanvas.drawPath(path, paint);
        srcRect.set((int) calcPos(0, mRateW, mMinRate, mViewWidth),
                (int) calcPos(0, mRateH, mMinRate, mViewHeight),
                (int) calcPos(mViewWidth, mRateW, mMinRate, mViewWidth),
                (int) calcPos(mViewHeight, mRateH, mMinRate, mViewHeight));
        dstRect.set(0, 0, mViewWidth, mViewHeight);
        canvas.drawBitmap(cachedBitmap, srcRect, dstRect, paint);
    }

    private float cur_x, cur_y;

    private float calcPos(float pos, float rate, float minRate, int maxLength) {
//        return pos * (rate + minRate) / 2;
        return pos * minRate + maxLength * (rate - minRate) / 2;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        calc();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                cur_x = x;
                cur_y = y;
                path.moveTo(
                        calcPos(cur_x, mRateW, mMinRate, mViewWidth),
                        calcPos(cur_y, mRateH, mMinRate, mViewHeight)
                );
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                path.quadTo(
                        calcPos(cur_x, mRateW, mMinRate, mViewWidth),
                        calcPos(cur_y, mRateH, mMinRate, mViewHeight),
                        calcPos(x, mRateW, mMinRate, mViewWidth),
                        calcPos(y, mRateH, mMinRate, mViewHeight)
                );
                cur_x = x;
                cur_y = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
            }
        }

        invalidate();

        return true;
    }

}
