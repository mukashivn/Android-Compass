package com.core.compass.ui.custom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

//import com.miui.internal.R.styleable;
//import com.miui.internal.util.PackageConstants;
import com.core.compass.ApplicationImpl;
import com.core.ssvapp.R;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/28/18
 */

public class Rotation3DLayout extends FrameLayout implements SensorEventListener
{
    private static final int LAYOUT_DIRECTION_LTR = 0;
    private static final int LAYOUT_DIRECTION_RTL = 1;
    private static final int XG = 8388659;
    private static final int XH = 10;
    private static final float XI;
    private static final int XJ = 45;
    private static final int XM = 300;
    private static final float[] XN;
    private float XK;
    private float XL;
    private int XO;
    private float[] XP;
    private float[] XQ;
    private float[] XR;
    private long XS;
    private boolean XT;
    private boolean XU;
    private boolean XV;
    private boolean XW;
    private float XX;
    private float XY;
    private Sensor XZ;
    private SensorManager Ya;
    private SensorEventListener Yb;
    private AnimatorSet sS;

    static {
        XI = scale(10.0f);
        XN = new float[] { 1.0f, -0.5f, 0.25f, 0.0f };
    }

    public Rotation3DLayout(final Context context) {
        this(context, null);
    }

    public Rotation3DLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public Rotation3DLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.XK = 10.0f;
        this.XL = Rotation3DLayout.XI;
        this.XO = 300;
        this.XP = Rotation3DLayout.XN;
        this.XQ = new float[Rotation3DLayout.XN.length];
        this.XR = new float[Rotation3DLayout.XN.length];
        this.XS = 0L;
        this.XT = true;
        this.XU = false;
        this.XV = false;
        this.XW = false;
        this.Yb = this;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.Rotation3DLayout, n, 0);
        this.setMaxRotationDegree(obtainStyledAttributes.getFloat(R.styleable.Rotation3DLayout_maxRotationDegree, this.XK));
        this.XW = obtainStyledAttributes.getBoolean(R.styleable.Rotation3DLayout_autoGravityRotation, this.XW);
        obtainStyledAttributes.recycle();
    }

    private int update(final int n, final float n2) {
        return (int)(Math.sin(Math.toRadians(n2)) * n);
    }

    @SuppressLint("WrongCall")
    private void apply(float xk, final boolean b) {
        if (Math.abs(xk) > this.XK) {
            if (xk > 0.0f) {
                xk = this.XK;
            }
            else {
                xk = -this.XK;
            }
        }
        if (xk != this.getRotationX() && (b || (!this.XU && !this.XV))) {
            super.setRotationX(xk);
            this.onLayout(false, this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        }
    }

    @SuppressLint("WrongCall")
    private void getValue(float xk, final boolean b) {
        if (Math.abs(xk) > this.XK) {
            if (xk > 0.0f) {
                xk = this.XK;
            }
            else {
                xk = -this.XK;
            }
        }
        if (xk != this.getRotationY() && (b || (!this.XU && !this.XV))) {
            super.setRotationY(xk);
            this.onLayout(false, this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        }
    }

    private static float scale(final float n) {
        return n / (ApplicationImpl.getAppContext().getResources().getDisplayMetrics().widthPixels / 2.0f);
    }

    public void enableAutoRotationByGravity(final boolean xw) {
        this.XW = xw;
        if (this.XW) {
            this.startGravityDetection();
            return;
        }
        this.stopGravityDetection();
    }

    public void enableTouchRotation(final boolean xt) {
        this.XT = xt;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.stopGravityDetection();
    }

    protected FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return (FrameLayout.LayoutParams)new Rotation3DLayout.LayoutParams(-1, -1);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(final ViewGroup.LayoutParams viewGroup$LayoutParams) {
        return (ViewGroup.LayoutParams)new Rotation3DLayout.LayoutParams(viewGroup$LayoutParams);
    }

    public FrameLayout.LayoutParams generateLayoutParams(final AttributeSet set) {
        return (FrameLayout.LayoutParams)new Rotation3DLayout.LayoutParams(this.getContext(), set);
    }

    public float getMaxRotationDegree() {
        return this.XK;
    }

    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return this.XT || super.onInterceptTouchEvent(motionEvent);
    }

    protected void onLayout(final boolean b, int gravity, int n, int i, int n2) {
        final int childCount = this.getChildCount();
        final int paddingLeft = this.getPaddingLeft();
        final int n3 = i - gravity - this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        n2 = n2 - n - this.getPaddingBottom();
        View child;
        Rotation3DLayout.LayoutParams layoutParams;
        int measuredWidth;
        int measuredHeight;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                layoutParams = (Rotation3DLayout.LayoutParams)child.getLayoutParams();
                measuredWidth = child.getMeasuredWidth();
                measuredHeight = child.getMeasuredHeight();
                gravity = layoutParams.gravity;
                if ((n = gravity) == -1) {
                    n = 8388659;
                }
                switch (Gravity.getAbsoluteGravity(n, 0) & 0x7) {
                    default: {
                        gravity = layoutParams.leftMargin + paddingLeft;
                        break;
                    }
                    case 3: {
                        gravity = layoutParams.leftMargin + paddingLeft;
                        break;
                    }
                    case 1: {
                        gravity = (n3 - paddingLeft - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                        break;
                    }
                    case 5: {
                        gravity = n3 - measuredWidth - layoutParams.rightMargin;
                        break;
                    }
                }
                switch (n & 0x70) {
                    default: {
                        n = layoutParams.topMargin + paddingTop;
                        break;
                    }
                    case 48: {
                        n = layoutParams.topMargin + paddingTop;
                        break;
                    }
                    case 16: {
                        n = (n2 - paddingTop - measuredHeight) / 2 + paddingTop + layoutParams.topMargin - layoutParams.bottomMargin;
                        break;
                    }
                    case 80: {
                        n = n2 - measuredHeight - layoutParams.bottomMargin;
                        break;
                    }
                }
                gravity += this.update(layoutParams.zdistance, this.getRotationY());
                n -= this.update(layoutParams.zdistance, this.getRotationX());
                if (b || gravity != child.getLeft() || n != child.getTop()) {
                    child.layout(gravity, n, gravity + measuredWidth, n + measuredHeight);
                }
            }
        }
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (!this.XT) {
            return super.onTouchEvent(motionEvent);
        }
        switch (motionEvent.getAction()) {
            case 2: {
                if (SystemClock.elapsedRealtime() - this.XS > ViewConfiguration.getTapTimeout()) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
            case 0: {
                if (motionEvent.getAction() == 0) {
                    this.XS = SystemClock.elapsedRealtime();
                }
                final float min = Math.min(Math.max(0.0f, motionEvent.getX()), this.getWidth());
                final float min2 = Math.min(Math.max(0.0f, motionEvent.getY()), this.getHeight());
                final int n = this.getWidth() / 2;
                final int n2 = this.getHeight() / 2;
                if (this.sS != null) {
                    this.sS.cancel();
                }
                this.apply((n2 - min2) * this.XL, true);
                this.getValue((min - n) * this.XL, true);
                this.XU = true;
                break;
            }
            case 1:
            case 3: {
                this.resetRotation();
                this.XU = false;
                break;
            }
        }
        super.onTouchEvent(motionEvent);
        return true;
    }

    protected void onVisibilityChanged(final View view, final int n) {
        super.onVisibilityChanged(view, n);
        if (n == 0) {
            if (this.XW) {
                this.startGravityDetection();
            }
            return;
        }
        this.stopGravityDetection();
    }

    protected void resetRotation() {
        if (this.sS == null) {
            (this.sS = new AnimatorSet()).addListener(new Animator.AnimatorListener() {
                public void onAnimationCancel(final Animator animator) {
                    Rotation3DLayout.this.XV = false;
                }

                public void onAnimationEnd(final Animator animator) {
                    Rotation3DLayout.this.XV = false;
                }

                public void onAnimationRepeat(final Animator animator) {
                }

                public void onAnimationStart(final Animator animator) {
                    Rotation3DLayout.this.XV = true;
                }
            });
            final ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator((TimeInterpolator)new DecelerateInterpolator(0.75f));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    Rotation3DLayout.this.apply((float)valueAnimator.getAnimatedValue(), true);
                }
            });
            valueAnimator.setDuration((long)this.XO);
            final ValueAnimator valueAnimator2 = new ValueAnimator();
            valueAnimator2.setInterpolator((TimeInterpolator)new DecelerateInterpolator(0.75f));
            valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    Rotation3DLayout.this.getValue((float)valueAnimator.getAnimatedValue(), true);
                }
            });
            valueAnimator2.setDuration((long)this.XO);
            this.sS.playTogether(new Animator[] { valueAnimator, valueAnimator2 });
        }
        this.sS.cancel();
        final float rotationX = this.getRotationX();
        final float rotationY = this.getRotationY();
        for (int i = 0; i < this.XP.length; ++i) {
            this.XQ[i] = this.XX + this.XP[i] * rotationX;
            this.XR[i] = this.XY + this.XP[i] * rotationY;
        }
        ValueAnimator valueAnimator1= (ValueAnimator) (this.sS.getChildAnimations().get(0));
        ValueAnimator valueAnimator2= (ValueAnimator) (this.sS.getChildAnimations().get(1));
        valueAnimator1.setFloatValues(this.XQ);
        valueAnimator2.setFloatValues(this.XR);

        this.sS.start();

    }

    public void setMaxRotationDegree(final float xk) {
        this.XK = xk;
        this.XL = scale(xk);
    }

    public void setResetAnimDuration(final int xo) {
        this.XO = xo;
    }

    public void setResetBouncePatterns(final float[] xp) {
        if (xp != null && xp.length > 1) {
            this.XP = xp;
            this.XQ = new float[xp.length];
            this.XR = new float[xp.length];
        }
    }

    public void setRotationX(final float n) {
        this.apply(n, false);
    }

    public void setRotationY(final float n) {
        this.getValue(n, false);
    }

    public void startGravityDetection() {
        if (this.Ya == null) {
            this.Ya = (SensorManager)this.getContext().getSystemService("sensor");
        }
        if (this.XZ == null) {
            this.XZ = this.Ya.getDefaultSensor(3);
            this.Ya.registerListener(this.Yb, this.XZ, 1);
        }
    }

    public void stopGravityDetection() {
        if (this.Ya != null && this.XZ != null) {
            this.Ya.unregisterListener(this.Yb, this.XZ);
            this.XZ = null;
            this.Ya = null;
            this.resetRotation();
        }
    }

    protected void stopResetRotation() {
        if (this.sS != null) {
            this.sS.cancel();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float n = sensorEvent.values[1];
        final float n2 = sensorEvent.values[2];
        if (Math.abs(n) < 45.0f) {
            Rotation3DLayout.this.XX = n * Rotation3DLayout.this.XK / 45.0f;
            Rotation3DLayout.this.setRotationX(Rotation3DLayout.this.XX);
        }
        if (Math.abs(n2) < 45.0f) {
            Rotation3DLayout.this.XY = n2 * -Rotation3DLayout.this.XK / 45.0f;
            Rotation3DLayout.this.setRotationY(Rotation3DLayout.this.XY);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class LayoutParams extends FrameLayout.LayoutParams{
        public int zdistance;

        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.zdistance = 0;
        }

        public LayoutParams(final int n, final int n2, final int n3) {
            super(n, n2, n3);
            this.zdistance = 0;
        }

        public LayoutParams(final int n, final int n2, final int n3, final int zdistance) {
            super(n, n2, n3);
            this.zdistance = 0;
            this.zdistance = zdistance;
        }

        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.zdistance = 0;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.Rotation3DLayout_Layout);
            this.zdistance = obtainStyledAttributes.getInt(R.styleable.Rotation3DLayout_Layout_layout_zdistance, this.zdistance);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(final ViewGroup.LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.zdistance = 0;
        }

        public LayoutParams(final ViewGroup.MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.zdistance = 0;
        }

        public LayoutParams(final FrameLayout.LayoutParams frameLayout$LayoutParams) {
            super((ViewGroup.MarginLayoutParams)frameLayout$LayoutParams);
            this.zdistance = 0;
        }
    }
}