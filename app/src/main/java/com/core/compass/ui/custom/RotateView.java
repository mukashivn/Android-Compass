package com.core.compass.ui.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.core.ssvapp.R;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/27/18
 */

public class RotateView  extends View
{
    private final int ANGLE_BALL_FIRST_LOCATION;
    private final int BIG_CIRCLE_COLOR;
    private final float CIRCLE_RADIUS;
    private final int DIMEN_BIG_CIRCLE_STROKE_WIDTH;
    private final int DIMEN_SPACE_OF_CIRCLES;
    private final int FIRST_DELAY_UPDATE_TIME;
    private final int FOOT_PRINT_COLOR;
    private final int INDICATOR_BALL_COLOR;
    private final int INDICATOR_CIRCLE_RADIUS;
    private final float LARGE_CIRCLE_RADIUS;
    private final int LEAST_TILT_ANGLE;
    private final int LIMITED_ANGULAR_DIFF_ANGLE_UPDATE;
    private final int LIMITED_TIME_INTERVAL_ANGLE_UPDATE;
    private final RectF OVAL;
    private Paint mBigCirclePaint;
    private float mCircleX;
    private float mCircleY;
    private long mFirstUpdateTime;
    private Paint mFootprintPaint;
    private Paint mIndicatorBallPaint;
    private long mLastAddAngleTime;
    private int mLastSituatedAngle;
    private boolean[] mRolledAngles;
    private int mRolledAnglesCount;
    private float mVY;
    private float mVZ;

    public RotateView(final Context context) {
        this(context, null);
    }

    public RotateView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public RotateView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.FIRST_DELAY_UPDATE_TIME = 2000;
        this.DIMEN_SPACE_OF_CIRCLES = 30;
        this.DIMEN_BIG_CIRCLE_STROKE_WIDTH = 4;
        this.ANGLE_BALL_FIRST_LOCATION = 60;
        this.LEAST_TILT_ANGLE = 25;
        this.LIMITED_TIME_INTERVAL_ANGLE_UPDATE = 70;
        this.LIMITED_ANGULAR_DIFF_ANGLE_UPDATE = 20;
        this.BIG_CIRCLE_COLOR = this.getResources().getColor(R.color.colorAccent);
        this.FOOT_PRINT_COLOR = this.getResources().getColor(R.color.colorAccent);
        this.INDICATOR_BALL_COLOR = this.getResources().getColor(R.color.colorAccent);
        this.INDICATOR_CIRCLE_RADIUS = this.getResources().getDimensionPixelSize(R.dimen.indicator_circle_radius);
        this.CIRCLE_RADIUS = this.getResources().getDimensionPixelSize(R.dimen.circle_radius);
        this.LARGE_CIRCLE_RADIUS = this.CIRCLE_RADIUS + this.INDICATOR_CIRCLE_RADIUS + 30.0f;
        this.mRolledAngles = new boolean[360];
        this.OVAL = new RectF();
        this.mVZ = (float)Math.cos(1.0471975511965976);
        this.mVY = (float)Math.sin(1.0471975511965976);
        this.initPaint();
        this.clearRolledAngels();
    }

    private void addToFootprintAngles(final int mLastSituatedAngle) {
        if (this.saveAngle(mLastSituatedAngle)) {
            final long currentTimeMillis = System.currentTimeMillis();
            final long mLastAddAngleTime = this.mLastAddAngleTime;
            final int mLastSituatedAngle2 = this.mLastSituatedAngle;
            final boolean b = false;
            int n2;
            if (currentTimeMillis - mLastAddAngleTime < 70L) {
                final int abs = Math.abs(mLastSituatedAngle - mLastSituatedAngle2);
                if (abs < 20) {
                    final int min = Math.min(mLastSituatedAngle, this.mLastSituatedAngle);
                    int n = 1;
                    while (true) {
                        n2 = (b ? 1 : 0);
                        if (n >= abs) {
                            break;
                        }
                        this.saveAngle((min + n) % 360);
                        ++n;
                    }
                }
                else if (abs > 340) {
                    final int max = Math.max(mLastSituatedAngle, this.mLastSituatedAngle);
                    int n3 = 1;
                    while (true) {
                        n2 = (b ? 1 : 0);
                        if (n3 >= 360 - abs) {
                            break;
                        }
                        this.saveAngle((max + n3) % 360);
                        ++n3;
                    }
                }
                else {
                    n2 = 1;
                }
            }
            else {
                n2 = 1;
            }
            if (n2 != 0) {
                for (int i = 0; i < 5; ++i) {
                    this.saveAngle((mLastSituatedAngle + i) % 360);
                }
            }
            this.mLastSituatedAngle = mLastSituatedAngle;
            this.mLastAddAngleTime = System.currentTimeMillis();
        }
    }

    private int calculateAngle() {
        int n;
        if (this.mVZ == 0.0f) {
            if (this.mVY > 0.0f) {
                n = 90;
            }
            else {
                n = 270;
            }
        }
        else {
            final int n2 = (int)(Math.atan(this.mVY / this.mVZ) * 57.29577951308232);
            if (this.mVZ < 0.0f) {
                n = n2 + 180;
            }
            else {
                n = n2;
                if (this.mVY < 0.0f) {
                    n = n2 + 360;
                }
            }
        }
        return n % 360;
    }

    private void clearRolledAngels() {
        for (int i = 0; i < 360; ++i) {
            this.mRolledAngles[i] = false;
        }
        this.mRolledAnglesCount = 0;
    }

    private void drawBigCircle(final Canvas canvas) {
        canvas.drawCircle(this.mCircleX, this.mCircleY, this.LARGE_CIRCLE_RADIUS, this.mBigCirclePaint);
    }

    private void drawFootprints(final Canvas canvas) {
        int n3;
        for (int i = -1; i < 360; i = n3) {
            int n;
            do {
                n = i + 1;
                if (n >= 360) {
                    break;
                }
                i = n;
            } while (!this.mRolledAngles[n]);
            if (n == 360) {
                break;
            }
            int n2 = n;
            do {
                n3 = n2 + 1;
                if (n3 >= 360) {
                    break;
                }
                n2 = n3;
            } while (this.mRolledAngles[n3]);
            canvas.drawArc(this.OVAL, (float)n, (float)(n3 - n), false, this.mFootprintPaint);
        }
    }

    private void drawIndicatorBall(final Canvas canvas) {
        canvas.drawCircle(this.CIRCLE_RADIUS * this.mVZ + this.mCircleX, this.mVY * this.CIRCLE_RADIUS + this.mCircleY, (float)this.INDICATOR_CIRCLE_RADIUS, this.mIndicatorBallPaint);
    }

    private void initPaint() {
        (this.mBigCirclePaint = new Paint()).setAntiAlias(true);
        this.mBigCirclePaint.setColor(this.BIG_CIRCLE_COLOR);
        this.mBigCirclePaint.setStyle(Paint.Style.STROKE);
        this.mBigCirclePaint.setStrokeWidth(4.0f);
        (this.mFootprintPaint = new Paint()).setAntiAlias(true);
        this.mFootprintPaint.setColor(this.FOOT_PRINT_COLOR);
        this.mFootprintPaint.setStyle(Paint.Style.STROKE);
        this.mFootprintPaint.setStrokeWidth(4.0f);
        (this.mIndicatorBallPaint = new Paint()).setAntiAlias(true);
        this.mIndicatorBallPaint.setColor(this.INDICATOR_BALL_COLOR);
        this.mIndicatorBallPaint.setStyle(Paint.Style.FILL);
    }

    private boolean saveAngle(final int n) {
        boolean b = false;
        if (!this.mRolledAngles[n]) {
            this.mRolledAngles[n] = true;
            ++this.mRolledAnglesCount;
            b = true;
        }
        return b;
    }

    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.drawBigCircle(canvas);
        this.drawIndicatorBall(canvas);
        this.drawFootprints(canvas);
    }

    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.mCircleX = this.getMeasuredWidth() / 2;
        this.mCircleY = this.getMeasuredHeight() / 2;
        this.OVAL.set(this.mCircleX - this.LARGE_CIRCLE_RADIUS, this.mCircleY - this.LARGE_CIRCLE_RADIUS, this.mCircleX + this.LARGE_CIRCLE_RADIUS, this.mCircleY + this.LARGE_CIRCLE_RADIUS);
    }

    public void reset() {
        this.mFirstUpdateTime = 0L;
        this.mLastAddAngleTime = 0L;
        this.mVZ = (float)Math.cos(1.0471975511965976);
        this.mVY = (float)Math.sin(1.0471975511965976);
        this.clearRolledAngels();
    }

    public void update(float n, float n2) {
        if (this.mFirstUpdateTime == 0L) {
            this.mFirstUpdateTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - this.mFirstUpdateTime > 2000L) {
            n = -Math.min(Math.max(n, -90.0f), 90.0f);
            n2 = -Math.min(Math.max(n2, -90.0f), 90.0f);
            final double n3 = n / Math.abs(90.0f);
            final double n4 = n2 / Math.abs(90.0f);
            final double sqrt = Math.sqrt(n3 * n3 + n4 * n4);
            if (sqrt == 0.0) {
                this.mVY = 0.0f;
                this.mVZ = 1.0f;
            }
            else {
                this.mVY = (float)(n3 / sqrt);
                this.mVZ = (float)(n4 / sqrt);
            }
            if (Math.abs(n) >= 25.0f || Math.abs(n2) >= 25.0f) {
                this.addToFootprintAngles(this.calculateAngle());
            }
            if (this.mRolledAnglesCount * 100 / 360 == 100 && this.getVisibility() == View.VISIBLE) {
               // Toast.makeText(this.getContext(), 2131427375, 1).show();
                this.clearRolledAngels();
            }
        }
        this.invalidate();
    }
}
