package com.core.compass.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ViewSwitcher;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/27/18
 */

public abstract class LyingPortraitView extends ViewSwitcher {
    private AnimationSet mInAnimationLying;
    private AnimationSet mInAnimationPortrait;
    private boolean mIsAnimationsInited;
    private AnimationSet mOutAnimationLying;
    private AnimationSet mOutAnimationPortrat;

    public LyingPortraitView(final Context context) {
        super(context);
        this.mIsAnimationsInited = false;
    }

    public LyingPortraitView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mIsAnimationsInited = false;
    }

    private void initAnimations() {
        if (this.getRotationCenterY() == 0.0f) {
            return;
        }
        this.mOutAnimationLying = new AnimationSet(true);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        ((Animation)alphaAnimation).setDuration(500L);
        this.mOutAnimationLying.addAnimation((Animation)alphaAnimation);
        final Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(0.0f, 90.0f, (float)(this.getWidth() / 2), this.getRotationCenterY());
        ((Animation)rotate3dAnimation).setDuration(500L);
        this.mOutAnimationLying.addAnimation((Animation)rotate3dAnimation);
        this.mOutAnimationPortrat = new AnimationSet(true);
        final AlphaAnimation alphaAnimation2 = new AlphaAnimation(1.0f, 0.0f);
        ((Animation)alphaAnimation2).setDuration(500L);
        this.mOutAnimationPortrat.addAnimation((Animation)alphaAnimation2);
        final Rotate3dAnimation rotate3dAnimation2 = new Rotate3dAnimation(0.0f, -90.0f, (float)(this.getWidth() / 2), this.getRotationCenterY());
        ((Animation)rotate3dAnimation2).setDuration(500L);
        this.mOutAnimationPortrat.addAnimation((Animation)rotate3dAnimation2);
        this.mInAnimationPortrait = new AnimationSet(true);
        final AlphaAnimation alphaAnimation3 = new AlphaAnimation(0.0f, 1.0f);
        ((Animation)alphaAnimation3).setDuration(500L);
        this.mInAnimationPortrait.addAnimation((Animation)alphaAnimation3);
        final Rotate3dAnimation rotate3dAnimation3 = new Rotate3dAnimation(-90.0f, 0.0f, (float)(this.getWidth() / 2), this.getRotationCenterY());
        ((Animation)rotate3dAnimation3).setDuration(500L);
        this.mInAnimationPortrait.addAnimation((Animation)rotate3dAnimation3);
        this.mInAnimationLying = new AnimationSet(true);
        final AlphaAnimation alphaAnimation4 = new AlphaAnimation(0.0f, 1.0f);
        ((Animation)alphaAnimation4).setDuration(500L);
        this.mInAnimationLying.addAnimation((Animation)alphaAnimation4);
        final Rotate3dAnimation rotate3dAnimation4 = new Rotate3dAnimation(90.0f, 0.0f, (float)(this.getWidth() / 2), this.getRotationCenterY());
        ((Animation)rotate3dAnimation4).setDuration(500L);
        this.mInAnimationLying.addAnimation((Animation)rotate3dAnimation4);
        this.mIsAnimationsInited = true;
    }

    protected abstract int getLyingViewId();

    protected abstract int getPortraitViewId();

    protected abstract float getRotationCenterY();

    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
    }

    public void setFaceDirection(float abs) {
        if ((this.getInAnimation() == null || this.getInAnimation().hasStarted() || this.getInAnimation().hasEnded()) && (this.getOutAnimation() == null || !this.getOutAnimation().hasStarted() || this.getOutAnimation().hasEnded())) {
            if (!this.mIsAnimationsInited) {
                this.initAnimations();
            }
            final int id = this.getCurrentView().getId();
            abs = Math.abs(abs);
            if (abs >= 45.0f && abs <= 135.0f) {
                if (id == this.getLyingViewId()) {
                    this.setOutAnimation((Animation)this.mOutAnimationLying);
                    this.setInAnimation((Animation)this.mInAnimationPortrait);
                    this.showNext();
                }
            }
            else if (id == this.getPortraitViewId()) {
                this.setOutAnimation((Animation)this.mOutAnimationPortrat);
                this.setInAnimation((Animation)this.mInAnimationLying);
                this.showNext();
            }
        }
    }

    public void setInAnimation(final Animation inAnimation) {
        super.setInAnimation(inAnimation);
    }
}
