package com.core.compass.ui.custom;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/27/18
 */

public class Rotate3dAnimation extends Animation
{
    private Camera mCamera;
    private final float mCenterX;
    private final float mCenterY;
    private final float mFromDegrees;
    private final float mToDegrees;

    public Rotate3dAnimation(final float mFromDegrees, final float mToDegrees, final float mCenterX, final float mCenterY) {
        this.mFromDegrees = mFromDegrees;
        this.mToDegrees = mToDegrees;
        this.mCenterX = mCenterX;
        this.mCenterY = mCenterY;
    }

    protected void applyTransformation(final float n, final Transformation transformation) {
        final float mFromDegrees = this.mFromDegrees;
        final float mToDegrees = this.mToDegrees;
        final float mCenterX = this.mCenterX;
        final float mCenterY = this.mCenterY;
        final Camera mCamera = this.mCamera;
        final Matrix matrix = transformation.getMatrix();
        mCamera.setLocation(0.0f, 0.0f, -25.0f);
        mCamera.save();
        mCamera.rotateX(mFromDegrees + (mToDegrees - mFromDegrees) * n);
        mCamera.getMatrix(matrix);
        mCamera.restore();
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
    }

    public void initialize(final int n, final int n2, final int n3, final int n4) {
        super.initialize(n, n2, n3, n4);
        this.mCamera = new Camera();
    }
}
