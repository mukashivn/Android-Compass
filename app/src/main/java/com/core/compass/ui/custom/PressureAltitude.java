package com.core.compass.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/27/18
 */

public class PressureAltitude extends AppCompatImageView {
    private static final int ANGLE_ALL;
    private final int VIEW_HEIGHT;
    private final int VIEW_WIDTH;
    private Paint mBgPaint;
    private float mDrawingValue;
    private Paint mFgPaint;
    private Handler mHandler;
    private boolean mIsPressure;
    private Runnable mUpdater;
    private float mValue;

    static {
        ANGLE_ALL = Math.abs(-150);
    }

    public PressureAltitude(final Context context) {
        this(context, null);
    }

    public PressureAltitude(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public PressureAltitude(final Context context, final AttributeSet set, int color) {
        super(context, set, color);
        this.mHandler = new Handler();
        this.mUpdater = new Runnable() {
            @Override
            public void run() {
                float n;
                if (PressureAltitude.this.mIsPressure) {
                    n = 300.0f;
                }
                else {
                    n = -500.0f;
                }
                float n2;
                if (PressureAltitude.this.mIsPressure) {
                    n2 = 1100.0f;
                }
                else {
                    n2 = 5000.0f;
                }
                if (Math.abs(PressureAltitude.this.mValue - PressureAltitude.this.mDrawingValue) <= (n2 - n) / 1000.0f) {
                    PressureAltitude.this.mDrawingValue = PressureAltitude.this.mValue;
                }
                else {
                    update((PressureAltitude.this.mValue - PressureAltitude.this.mDrawingValue) / 15.0f);
                    PressureAltitude.this.mHandler.postDelayed(PressureAltitude.this.mUpdater, 50L);
                }
                PressureAltitude.this.invalidate();
            }
        };
        final float density = this.getContext().getResources().getDisplayMetrics().density;
        this.VIEW_WIDTH = (int)(145.7f * density);
        this.VIEW_HEIGHT = (int)(291.4f * density);
        this.mValue = 0.0f;
        this.mIsPressure = false;
        this.mFgPaint = new Paint(1);
        final Paint mFgPaint = this.mFgPaint;
        if (this.mIsPressure) {
            color = Color.RED;
        }
        else {
            color = Color.GRAY;
        }
        mFgPaint.setColor(color);
        this.mFgPaint.setStrokeWidth(5.0f);
        this.mFgPaint.setStyle(Paint.Style.STROKE);
        (this.mBgPaint = new Paint(1)).setColor(Color.DKGRAY);
        this.mBgPaint.setStrokeWidth(3.0f);
        this.mBgPaint.setStyle(Paint.Style.STROKE);
    }

    private int getViewSize(int size, final int n) {
        final int mode = View.MeasureSpec.getMode(size);
        size = View.MeasureSpec.getSize(size);
        switch (mode) {
            default: {
                return size;
            }
            case Integer.MIN_VALUE:
            case 0: {
                return n;
            }
        }
    }

    private void update(float mDrawingValue){
        this.mDrawingValue += mDrawingValue;
    }

    @SuppressLint("WrongConstant")
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final int height = this.getHeight();
        final int width = this.getWidth();
        final float n = height / 2.0f;
        float n2 = 0.0f;
        if (this.getLayoutDirection() == 1) {
            n2 = height / 2.0f;
        }
        final float n3 = width - 2 - 1;
        float n4;
        if (this.mIsPressure) {
            n4 = 300.0f;
        }
        else {
            n4 = -500.0f;
        }
        float n5;
        if (this.mIsPressure) {
            n5 = 1100.0f;
        }
        else {
            n5 = 5000.0f;
        }
        canvas.save();
        if (this.mIsPressure) {
            canvas.scale(-1.0f, 1.0f, (float)(width / 2 + 1), (float)(height / 2));
        }
        float n6;
        if ((n6 = (this.mDrawingValue - n4) * PressureAltitude.ANGLE_ALL / (n5 - n4)) > PressureAltitude.ANGLE_ALL) {
            n6 = PressureAltitude.ANGLE_ALL;
        }
        if (n6 < 0.0f) {
            n6 = 0.0f;
        }
        if (n6 > 1.0E-7) {
            canvas.drawArc(new RectF(n2 - n3, n - n3, n2 + n3, n + n3), 68.0f, -n6, false, this.mFgPaint);
        }
        else {
            canvas.drawArc(new RectF(n2 - n3, n - n3, n2 + n3, n + n3), 112.0f, -n6, false, this.mFgPaint);
        }
        final float n7 = 68.0f - n6 - 0.5f;
        final float n8 = -82.0f - n7;
        if (this.getLayoutDirection() == 1) {
            canvas.drawArc(new RectF(n2 - n3, n - n3, n2 + n3, n + n3), 180.0f - n7, -n8, false, this.mBgPaint);
        }
        else {
            canvas.drawArc(new RectF(n2 - n3, n - n3, n2 + n3, n + n3), n7, n8, false, this.mBgPaint);
        }
        canvas.restore();
    }

    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.setMeasuredDimension(this.getViewSize(n, this.VIEW_WIDTH), this.getViewSize(n2, this.VIEW_HEIGHT));
    }

    public void setIsPressure(final boolean mIsPressure) {
        this.mIsPressure = mIsPressure;
        float mDrawingValue;
        if (mIsPressure) {
            mDrawingValue = 300.0f;
        }
        else {
            mDrawingValue = -500.0f;
        }
        this.mDrawingValue = mDrawingValue;
        final Paint mFgPaint = this.mFgPaint;
        int color;
        if (this.mIsPressure) {
            color = Color.RED;
        }
        else {
            color = Color.GRAY;
        }
        mFgPaint.setColor(color);
    }

    public void setValue(final float mValue) {
        this.mValue = mValue;
        this.mHandler.post(this.mUpdater);
    }

}
