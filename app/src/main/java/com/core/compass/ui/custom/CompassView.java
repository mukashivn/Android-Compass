package com.core.compass.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.utils.CommonUtils;
import com.core.compass.utils.ScreenUtils;
import com.core.compass.utils.ViewUtils;
import com.core.ssvapp.R;

import java.util.ArrayList;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/27/18
 */

public class CompassView extends View {
    private float OUTER_CIRCLE_RADIUS;
    private int VIEW_HEIGHT;
    private int VIEW_WIDTH;
    private Paint mCircleBgPaint;
    private Paint mCircleFgPaint;
    private Drawable mCompassDrawable;
    private Drawable mCompassPointer;
    private Drawable mCompassLiner;
    private BitmapDrawable mCompassPointerMask;
    private float mCurrentDirection;
    private Paint mMaskPaint;

    private Bitmap mPainting;
    private Bitmap mPaintingPointer;
    private Bitmap mPaintingLiner;

    private Canvas mPaintingCanvas;
    private Canvas mPaintingPointerCanvas;
    private Canvas mPaintingLinerCanvas;

    private Context mContext;
    private int mTextColor = Color.BLACK;
    private ArrayList<CDgree> mDegree = new ArrayList<>();
    private int scrWidth;
    private int scrHeight;

    public CompassView(final Context context) {
        this(context, null);
        mContext = context;
    }

    public CompassView(final Context context, final AttributeSet set) {
        this(context, set, 0);
        mContext = context;
    }

    public CompassView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        mContext = context;
        scrWidth = ScreenUtils.getScreenWidth(context);
        scrHeight = ScreenUtils.getScreenHeight(context);

        this.VIEW_WIDTH = 9 * scrHeight / 16;
        if (this.VIEW_WIDTH > scrWidth)
            this.VIEW_WIDTH = scrWidth;
        this.VIEW_HEIGHT = this.VIEW_WIDTH;
        this.OUTER_CIRCLE_RADIUS = (int) (VIEW_WIDTH / 2 - ViewUtils.dpToPx(32));

        Drawable tempCompass;
        (tempCompass = this.getResources().getDrawable(R.drawable.compass_light)).setFilterBitmap(true);
        Bitmap bitmap = ((BitmapDrawable) tempCompass).getBitmap();
        this.mCompassDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (OUTER_CIRCLE_RADIUS * 2) - ViewUtils.dpToPx(72), (int) (OUTER_CIRCLE_RADIUS * 2) - ViewUtils.dpToPx(72), true));

        ((BitmapDrawable) this.mCompassDrawable).setAntiAlias(true);

        this.mCompassDrawable.setBounds(0, 0, this.mCompassDrawable.getIntrinsicWidth(), this.mCompassDrawable.getIntrinsicHeight());
        (this.mCompassPointer = this.getResources().getDrawable(R.drawable.compass_pointer)).setFilterBitmap(true);
        (this.mCompassLiner = this.getResources().getDrawable(R.drawable.line)).setFilterBitmap(true);
        ((BitmapDrawable) this.mCompassPointer).setAntiAlias(true);
        ((BitmapDrawable) this.mCompassLiner).setAntiAlias(true);
        (this.mCompassPointerMask = (BitmapDrawable) this.getResources().getDrawable(R.drawable.compass_pointer_mask)).setFilterBitmap(true);
        this.mCompassPointerMask.setAntiAlias(true);
        (this.mCircleBgPaint = new Paint(1)).setFilterBitmap(true);
        this.mCircleBgPaint.setColor(Color.DKGRAY);
        this.mCircleBgPaint.setStrokeWidth(5.0f);
        this.mCircleBgPaint.setStyle(Paint.Style.STROKE);
        (this.mCircleFgPaint = new Paint(1)).setFilterBitmap(true);
        this.mCircleFgPaint.setColor(Color.RED);
        this.mCircleFgPaint.setStrokeWidth(5.0f);
        this.mCircleFgPaint.setStyle(Paint.Style.STROKE);
        (this.mMaskPaint = new Paint(1)).setFilterBitmap(true);
        this.mMaskPaint.setXfermode((Xfermode) new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
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

    boolean isInit = false;

    protected void onDraw(final Canvas canvas) {
        final int width = this.getWidth();
        final int height = this.getHeight();
        if (this.mPainting == null) {
            this.mPainting = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.mPaintingCanvas = new Canvas(this.mPainting);

            this.mPaintingPointer = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
            this.mPaintingPointerCanvas = new Canvas(this.mPaintingPointer);

            this.mPaintingLiner = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
            this.mPaintingLinerCanvas = new Canvas(this.mPaintingLiner);
        }

        this.mPainting.eraseColor(0);
        this.mPaintingPointer.eraseColor(0);
        this.mPaintingLiner.eraseColor(0);

        final float n = width / 2.0f;
        final float n2 = height / 2.0f;
        final float outer_CIRCLE_RADIUS = this.OUTER_CIRCLE_RADIUS;
        final float n3 = (width - this.mCompassDrawable.getIntrinsicWidth()) / 2.0f;
        final float n4 = (height - this.mCompassDrawable.getIntrinsicHeight()) / 2.0f;
        this.mCompassDrawable.setBounds((int) n3, (int) n4, (int) (this.mCompassDrawable.getIntrinsicWidth() + n3), (int) (this.mCompassDrawable.getIntrinsicHeight() + n4));
        float n5;
        if (this.mCurrentDirection > 180.0f) {
            n5 = -this.mCurrentDirection + 0.5f;
        } else {
            n5 = 360.0f - this.mCurrentDirection - 0.5f;
        }
        //TODO: Comment draw arc. version up 1.2
        //this.mPaintingCanvas.drawArc(new RectF(n - outer_CIRCLE_RADIUS, n2 - outer_CIRCLE_RADIUS, n + outer_CIRCLE_RADIUS, n2 + outer_CIRCLE_RADIUS), -90.0f, n5, false, this.mCircleBgPaint);
        float n6;
        if (this.mCurrentDirection > 180.0f) {
            n6 = 360.0f - this.mCurrentDirection - 0.5f;
        } else {
            n6 = -this.mCurrentDirection + 0.5f;
        }
        //TODO: Comment draw arc version up 1.2
        //this.mPaintingCanvas.drawArc(new RectF(n - outer_CIRCLE_RADIUS, n2 - outer_CIRCLE_RADIUS, n + outer_CIRCLE_RADIUS, n2 + outer_CIRCLE_RADIUS), -90.0f, n6, false, this.mCircleFgPaint);
        this.mPaintingPointerCanvas.drawBitmap(this.mCompassPointerMask.getBitmap(), n - this.mCompassPointerMask.getIntrinsicWidth() / 2, 0.0f, this.mMaskPaint);

        final float n8 = n - this.mCompassLiner.getIntrinsicWidth() / 2;
        this.mCompassLiner.setBounds((int) n8, 10, (int) (this.mCompassLiner.getIntrinsicWidth() + n8), this.mCompassLiner.getIntrinsicHeight() + 20);
        this.mCompassLiner.draw(this.mPaintingLinerCanvas);

        final float n7 = n - this.mCompassPointer.getIntrinsicWidth() / 2;
        this.mCompassPointer.setBounds((int) n7, 0, (int) (this.mCompassPointer.getIntrinsicWidth() + n7), this.mCompassPointer.getIntrinsicHeight());
        this.mCompassPointer.draw(this.mPaintingPointerCanvas);
        canvas.drawBitmap(this.mPaintingLiner, 0.0f, ViewUtils.dpToPx(48), (Paint) null);
        canvas.save();

        canvas.rotate(this.mCurrentDirection, (float) (this.getWidth() / 2), (float) (this.getHeight() / 2));
        canvas.drawBitmap(this.mPainting, 0.0f, 0.0f, (Paint) null);
        this.mCompassDrawable.draw(canvas);
        canvas.drawBitmap(this.mPaintingPointer, 0.0f, ViewUtils.dpToPx(48), (Paint) null);
        canvas.save();
        //Draw text:
        if (!isInit) {
            isInit = true;
            initDegree();
        }
        drawDirectionDegree(canvas);
        canvas.save();
        canvas.restore();
    }

    void drawDirectionDegree(Canvas canvas) {
        for (CDgree item : mDegree) {
            canvas.restore();
            canvas.save();
            item.draw(canvas, -mCurrentDirection);
        }
    }

    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.setMeasuredDimension(this.getViewSize(n, this.VIEW_WIDTH), this.getViewSize(n2, this.VIEW_HEIGHT));
    }

    public void setTargetDirection(float n) {
        float mCurrentDirection;
        while (true) {
            mCurrentDirection = n;
            if (n >= 0.0f) {
                break;
            }
            n += 360.0f;
        }
        while (mCurrentDirection >= 360.0f) {
            mCurrentDirection -= 360.0f;
        }
        this.mCurrentDirection = mCurrentDirection;
        this.invalidate();
    }

    public void setTheme(ThemeModel theme) {
        Drawable tempCompass;
        isInit = false;
        (tempCompass = CommonUtils.getDrawableFromName(mContext, theme.getCompassImage())).setFilterBitmap(true);
        Bitmap bitmap = ((BitmapDrawable) tempCompass).getBitmap();
        this.mCompassDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (OUTER_CIRCLE_RADIUS * 2 - ViewUtils.dpToPx(72)), (int) (OUTER_CIRCLE_RADIUS * 2 - ViewUtils.dpToPx(72)), true));

        this.mTextColor = Color.parseColor(theme.getTextColor());
        ((BitmapDrawable) this.mCompassDrawable).setAntiAlias(true);
        this.mCompassDrawable.setBounds(0, 0, this.mCompassDrawable.getIntrinsicWidth(), this.mCompassDrawable.getIntrinsicHeight());
    }

    private void initDegree() {
        int dxCenter = getWidth() / 2;
        int dyCenter = getHeight() / 2;

        TextPaint paint = new TextPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(ViewUtils.dipToPixels(mContext, 18));

        Rect rect = new Rect();
        paint.getTextBounds("E", 0, 1, rect);
        float x = (float) (dxCenter) + OUTER_CIRCLE_RADIUS / 2 + ViewUtils.dpToPx(8) - rect.width();
        float y = (float) (dyCenter + rect.height() / 2);

        CDgree e_d = new CDgree("E", rect, paint, x, y, mTextColor, ViewUtils.dpToPx(20));
        mDegree.add(e_d);

        rect = new Rect();
        paint.getTextBounds("W", 0, 1, rect);
        x = (float) (dxCenter - rect.width() / 2) - OUTER_CIRCLE_RADIUS / 2 - ViewUtils.dpToPx(4);
        y = (float) (dyCenter + rect.height() / 2);
        CDgree w_d = new CDgree("W", rect, paint, x, y, mTextColor, ViewUtils.dpToPx(20));
        mDegree.add(w_d);

        rect = new Rect();
        paint.getTextBounds("N", 0, 1, rect);
        paint.setColor(Color.RED);
        x = (float) (dxCenter - rect.width() / 2);
        y = (float) (dyCenter - OUTER_CIRCLE_RADIUS / 2 + ViewUtils.dpToPx(8));
        CDgree n_d = new CDgree("N", rect, paint, x, y, Color.RED, ViewUtils.dpToPx(20));
        mDegree.add(n_d);

        rect = new Rect();
        paint.getTextBounds("S", 0, 1, rect);
        paint.setColor(mTextColor);
        x = (float) (dxCenter - rect.width() / 2);
        y = (float) (dyCenter + OUTER_CIRCLE_RADIUS / 2) + ViewUtils.dpToPx(8);
        CDgree s_d = new CDgree("S", rect, paint, x, y, mTextColor, ViewUtils.dpToPx(20));
        mDegree.add(s_d);


        //Draw Degree
        float dx, dy;
        int r = (int) (OUTER_CIRCLE_RADIUS);
        for (int i = 0; i < 360; i = i + 30) {
            String text = String.valueOf(i);
            rect = new Rect();
            paint.setColor(mTextColor);
            paint.setTextSize(ViewUtils.dipToPixels(mContext, 14));
            paint.getTextBounds(text, 0, text.length(), rect);

            dx = (float) (dxCenter + (r - ViewUtils.dpToPx(7)) * Math.sin(Math.toRadians(i)));
            dy = (float) (dyCenter - (r - ViewUtils.dpToPx(7)) * Math.cos(Math.toRadians(i)));
            if (i == 0) {
                dy += rect.height() - 5;
                dx -= rect.width() / 2;
            } else if (i == 30 || i == 60) {
                dy += rect.height();
                dx -= rect.width();
            } else if (i == 90) {
                dx -= rect.width();
                dy += rect.height() / 2;
            } else if (i == 120) {
                dx -= 3 * rect.width() / 4;
            } else if (i == 150) {
                dx -= 2 * rect.width() / 3;
            } else if (i == 180) {
                dx -= rect.width() / 2;
            } else if (i == 210) {
                dy += rect.height() / 3;
                dx -= rect.width() / 4;
            } else if (i == 240) {
                dx -= rect.width() / 4;
            } else if (i == 270) {
                dy += rect.height() / 2;
                dx -= rect.width() / 4;
            } else if (i == 300) {
                dx -= rect.width() / 3;
                dy += rect.height();
            } else if (i == 330) {
                dy += 2 * rect.height() / 3;
            }

            CDgree item = new CDgree(text, rect, paint, dx, dy, mTextColor, ViewUtils.dipToPixels(mContext, 14));
            mDegree.add(item);
        }

    }

    private class CDgree {
        private float dx, dy;
        private String text;
        private Rect rect;
        private TextPaint textPaint;
        private int textColor;
        private float textSize;

        public CDgree(String text, Rect rect, TextPaint textPaint, float x, float y, int textColor, float textSize) {
            this.text = text;
            this.textPaint = textPaint;
            this.dx = x;
            this.dy = y;
            this.textColor = textColor;
            this.rect = rect;
            this.textSize = textSize;
        }

        public void draw(Canvas canvas, float dgree) {
            canvas.rotate(dgree, dx + rect.exactCenterX(), dy + rect.exactCenterY());
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);
            canvas.drawText(text, dx, dy, textPaint);
        }
    }
}
