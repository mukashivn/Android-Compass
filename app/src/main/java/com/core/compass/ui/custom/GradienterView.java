package com.core.compass.ui.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.utils.CommonUtils;
import com.core.compass.utils.ScreenUtils;
import com.core.compass.utils.ViewUtils;
import com.core.ssvapp.R;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/27/18
 */

public class GradienterView extends View {
    private int VIEW_HEIGHT;
    private int VIEW_WIDTH;
    private Drawable mCircleLying1;
    private Drawable mCircleLying2;
    private Drawable mCirclePortrait1;
    private Drawable mCirclePortrait2;
    private float mDirectionLying;
    private boolean mIsPortrait;
    private boolean mIsZero;
    private Drawable mPointerLying1;
    private Drawable mPointerLying2;
    private Drawable mPointerPortrait1;
    private Drawable mPointerPortrait2;
    private float mVY;
    private float mVZ;
    private float mZ;
    private ValueAnimator mZeroAnimator;
    private float mZeroProgress;
    private Context mContext;
    private float mSubCircleR;

    public GradienterView(final Context context) {
        this(context, null);
    }

    public GradienterView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public GradienterView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mContext = context;
        this.mIsZero = false;
        this.mZeroProgress = 0.0f;

        final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(this.getResources(), R.drawable.gradienter_circle_lying1, bitmapFactory$Options);
        Drawable drawable= getResources().getDrawable(R.drawable.gradienter_circle_lying1);
        this.VIEW_WIDTH = drawable.getIntrinsicWidth();
        this.VIEW_HEIGHT = this.VIEW_WIDTH;

    }

    private void drawGradienterLying(final Canvas canvas) {
        this.mCircleLying1.setAlpha((int) ((1.0f - this.mZeroProgress) * 255.0f));
        this.mCircleLying1.draw(canvas);
        this.mCircleLying2.setAlpha((int) (this.mZeroProgress * 255.0f));
        this.mCircleLying2.draw(canvas);
        if (this.mPointerLying1 == null) {
            this.mPointerLying1 = this.getResources().getDrawable(R.drawable.gradienter_pointer_lying1);
        }
        if (this.mPointerLying2 == null) {
            this.mPointerLying2 = this.getResources().getDrawable(R.drawable.gradienter_pointer_lying2);
        }
        final float n = this.getWidth() / 2.0f;
        final float n2 = this.getHeight() / 2.0f;
        final int n3 = (int) (this.mVZ * this.mSubCircleR + n - this.mPointerLying1.getIntrinsicWidth() / 2.0f);
        final int n4 = (int) (this.mVY * this.mSubCircleR + n2 - this.mPointerLying1.getIntrinsicHeight() / 2.0f);
        this.mPointerLying1.setBounds(n3, n4, this.mPointerLying1.getIntrinsicWidth() + n3, this.mPointerLying1.getIntrinsicHeight() + n4);
        this.mPointerLying1.setAlpha((int) ((1.0f - this.mZeroProgress) * 255.0f));
        this.mPointerLying1.draw(canvas);
        this.mPointerLying2.setBounds(n3, n4, this.mPointerLying1.getIntrinsicWidth() + n3, this.mPointerLying1.getIntrinsicHeight() + n4);
        this.mPointerLying2.setAlpha((int) (this.mZeroProgress * 255.0f));
        this.mPointerLying2.draw(canvas);
    }

    private void drawGradienterPortrait(final Canvas canvas) {
        if (this.mCirclePortrait1 == null) {
            (this.mCirclePortrait1 = this.getResources().getDrawable(R.drawable.gradienter_circle_portrait1)).setBounds(this.getCenterRect(this.mCirclePortrait1.getIntrinsicWidth(), this.mCirclePortrait1.getIntrinsicHeight()));
        }
        if (this.mCirclePortrait2 == null) {
            (this.mCirclePortrait2 = this.getResources().getDrawable(R.drawable.gradienter_circle_portrait2)).setBounds(this.getCenterRect(this.mCirclePortrait2.getIntrinsicWidth(), this.mCirclePortrait2.getIntrinsicHeight()));
        }
        if (this.mPointerPortrait1 == null) {
            (this.mPointerPortrait1 = this.getResources().getDrawable(R.drawable.gradienter_pointer_portrait1)).setBounds(this.getCenterRect(this.mPointerPortrait1.getIntrinsicWidth(), this.mPointerPortrait1.getIntrinsicHeight()));
        }
        if (this.mPointerPortrait2 == null) {
            (this.mPointerPortrait2 = this.getResources().getDrawable(R.drawable.gradienter_pointer_portrait2)).setBounds(this.getCenterRect(this.mPointerPortrait2.getIntrinsicWidth(), this.mPointerPortrait2.getIntrinsicHeight()));
        }
        this.mCirclePortrait1.setAlpha((int) ((1.0f - this.mZeroProgress) * 255.0f));
        this.mCirclePortrait1.draw(canvas);
        this.mCirclePortrait2.setAlpha((int) (this.mZeroProgress * 255.0f));
        this.mCirclePortrait2.draw(canvas);
        canvas.save();
        canvas.rotate(this.mZ, this.getWidth() / 2.0f, this.getHeight() / 2.0f);
        this.mPointerPortrait1.setAlpha((int) ((1.0f - this.mZeroProgress) * 255.0f));
        this.mPointerPortrait1.draw(canvas);
        this.mPointerPortrait2.setAlpha((int) (this.mZeroProgress * 255.0f));
        this.mPointerPortrait2.draw(canvas);
        canvas.restore();
    }

    private Rect getCenterRect(final int n, final int n2) {
        final int height = this.getHeight();
        final int width = this.getWidth();
        final int n3 = height / 2 - n / 2;
        final int n4 = width / 2 - n2 / 2;
        return new Rect(n3, n4, n3 + n, n4 + n2);
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

    private void startZeroAnimationIfNeed() {
        if (this.mZeroProgress == 1.0f) {
            this.mIsZero = true;
        }
        if (this.mIsZero) {
            if ((!this.mIsPortrait && Math.round(this.mDirectionLying) != 0) || (this.mIsPortrait && Math.round(this.mZ) != 0)) {
                this.mIsZero = false;
                if (this.mZeroProgress != 0.0f) {
                    if (this.mZeroAnimator != null) {
                        this.mZeroAnimator.cancel();
                    }
                    (this.mZeroAnimator = ValueAnimator.ofFloat(new float[]{this.mZeroProgress, 0.0f})).addUpdateListener(new LyingZeroAnimationListener());
                    this.mZeroAnimator.start();
                }
            }
        } else if ((!this.mIsPortrait && Math.round(this.mDirectionLying) == 0) || (this.mIsPortrait && Math.round(this.mZ) == 0)) {
            this.mIsZero = true;
            if (this.mZeroProgress != 1.0f) {
                if (this.mZeroAnimator != null) {
                    this.mZeroAnimator.cancel();
                }
                (this.mZeroAnimator = ValueAnimator.ofFloat(new float[]{this.mZeroProgress, 1.0f})).addUpdateListener(new LyingZeroAnimationListener());
                this.mZeroAnimator.start();
            }
        }
    }

    public float getDirectionLying() {
        return this.mDirectionLying;
    }

    public float getDirectionPortart() {
        return this.mZ;
    }

    protected void onDraw(final Canvas canvas) {
        if (!this.mIsPortrait) {
            this.drawGradienterLying(canvas);
            return;
        }
        this.drawGradienterPortrait(canvas);
    }

    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.setMeasuredDimension(this.getViewSize(n, this.VIEW_WIDTH), this.getViewSize(n2, this.VIEW_HEIGHT));
    }

    public void setDirection(float min, final float n) {
        float min2 = n;
        if (!this.mIsPortrait) {
            min = Math.min(Math.max(min, -90.0f), 90.0f);
            min2 = Math.min(Math.max(n, -90.0f), 90.0f);
            final double n2 = min / Math.abs(90.0f);
            final double n3 = min2 / Math.abs(90.0f);
            final double n4 = n2 * n2 + n3 * n3;
            double n5;
            if (Math.abs(n2) > Math.abs(n3)) {
                n5 = Math.abs(Math.sqrt(n4) / n2);
            } else if (n3 == 0.0) {
                n5 = 0.0;
            } else {
                n5 = Math.abs(Math.sqrt(n4) / n3);
            }
            if (n5 == 0.0) {
                min = 0.0f;
            } else {
                min = (float) (n2 / n5);
            }
            this.mVY = min;
            if (n5 == 0.0) {
                min = 0.0f;
            } else {
                min = (float) (n3 / n5);
            }
            this.mVZ = min;
            this.mDirectionLying = (float) (Math.sqrt(this.mVY * this.mVY + this.mVZ * this.mVZ) * 90.0);
        }

        if (this.mDirectionLying <= 1) {
            this.mZeroProgress = 1;
        } else {
            this.mZeroProgress = 0;
        }


        this.mZ = min2;
        this.startZeroAnimationIfNeed();
        this.invalidate();
    }

    public void setIsPortrait(final boolean mIsPortrait) {
        this.mIsPortrait = mIsPortrait;
    }

    private class LyingZeroAnimationListener implements ValueAnimator.AnimatorUpdateListener {
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            // GradienterView.this.mZeroProgress = (float)valueAnimator.getAnimatedValue();
            GradienterView.this.invalidate();
        }
    }

    public void setTheme(ThemeModel model) {
        int scrWidth = ScreenUtils.getScreenWidth(mContext);
        int scrHeight = ScreenUtils.getScreenHeight(mContext);

        int _VIEW_WIDTH = 9 * scrHeight / 16;
        if (_VIEW_WIDTH > scrWidth)
            _VIEW_WIDTH = scrWidth;
        int _VIEW_HEIGHT = _VIEW_WIDTH;
        int OUTER_CIRCLE_RADIUS = (int) (_VIEW_WIDTH / 2 - ViewUtils.dpToPx(32));

        int r = (int) 2*(OUTER_CIRCLE_RADIUS)/3;
        this.mSubCircleR = 2*r/5;
        Drawable temp = CommonUtils.getDrawableFromName(mContext, model.getBalance_group());
        Bitmap tempBitmap = ((BitmapDrawable) temp).getBitmap();

        Drawable temp2 = CommonUtils.getDrawableFromName(mContext, model.getBalance_group_focus());
        Bitmap tempBitmap2 = ((BitmapDrawable) temp2).getBitmap();

        (this.mCircleLying1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(tempBitmap, r, r, true))).setBounds(new Rect(VIEW_WIDTH/2 - r/2,VIEW_WIDTH/2 - r/2,VIEW_WIDTH/2 + r/2 ,VIEW_WIDTH/2 + r/2));
        (this.mCircleLying2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(tempBitmap2, r, r, true))).setBounds(new Rect(VIEW_WIDTH/2 - r/2,VIEW_WIDTH/2 - r/2,VIEW_WIDTH/2 + r/2 ,VIEW_WIDTH/2 + r/2));


    }
}
