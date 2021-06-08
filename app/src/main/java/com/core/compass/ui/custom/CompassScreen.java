package com.core.compass.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.utils.Utils;
import com.core.ssvapp.R;


/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/27/18
 */

public class CompassScreen extends LyingPortraitView {
    private PressureAltitude mAltitude;
    //private TextView mAngleLayoutLying;
    private TextView mAngleLayoutPortrait;
    private CompassView mCompassView;
    private ImageView mDegreeLying;
    private ImageView mDirectionLeft;
    private ImageView mDirectionRight;
    private TextView mDirectionTextViewLying;
    private TextView mDirectionTextViewPortrait;
    private PressureAltitude mPressure;
    private ImageView mArrowIndexView;
    private Context mContext;

    public CompassScreen(final Context context) {
        this(context, null);
        mContext = context;
    }

    public CompassScreen(final Context context, final AttributeSet set) {
        super(context, set);
        mContext = context;
        ((LayoutInflater) this.getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.compass_screen, (ViewGroup) this, true);
        (this.mPressure = (PressureAltitude) this.findViewById(R.id.pressure_show)).setIsPressure(true);
        (this.mAltitude = (PressureAltitude) this.findViewById(R.id.altitude_show)).setIsPressure(false);
        this.mCompassView = (CompassView) this.findViewById(R.id.compass_view);
        //this.mAngleLayoutLying = (TextView) this.findViewById(R.id.txt_direction_lying);
        this.mAngleLayoutPortrait = (TextView) this.findViewById(R.id.layout_direction_portrait);
        this.mDirectionTextViewLying = (TextView) this.findViewById(R.id.layout_direction_lying);
        this.mDirectionTextViewPortrait = (TextView) this.findViewById(R.id.txt_direction_lying);
        this.mDirectionLeft = (ImageView) this.findViewById(R.id.direction_left);
        this.mDirectionRight = (ImageView) this.findViewById(R.id.direction_right);
        this.mDegreeLying = (ImageView) this.findViewById(R.id.ic_degree_lying);
        this.mArrowIndexView = (ImageView) this.findViewById(R.id.arrow_index);
        final Typeface typeface = Utils.getTypeface(this.getContext().getAssets(), "SourceSansPro-Light.ttf");
        //this.mAngleLayoutLying.setTypeface(typeface);
        this.mAngleLayoutPortrait.setTypeface(typeface);
    }

    protected int getLyingViewId() {
        return R.id.compass_screen_lying;
    }

    protected int getPortraitViewId() {
        return R.id.compass_screen_portrait;
    }

    protected float getRotationCenterY() {
        return this.getHeight() / 2.0f;
    }

    public Bitmap getViewShot() {
        Bitmap bitmap;
        if (this.getWidth() == 0 || this.getHeight() == 0) {
            Log.d("Compass:CompassScreen", "width:" + this.getWidth() + ",height:" + this.getHeight());
            bitmap = null;
        } else {

            //final ViewGroup viewGroup = (ViewGroup)this.findViewById(2131492879);
            final Bitmap bitmap2 = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
            // viewGroup.draw(new Canvas(bitmap2));
            final Bitmap scaleBitmap = getResizedBitmap(bitmap2, bitmap2.getWidth() / 2, bitmap2.getHeight() / 2);
            if ((bitmap = scaleBitmap) != bitmap2) {
                bitmap2.recycle();
                return scaleBitmap;
            }

        }
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public void setAltitude(final float value) {
        this.mAltitude.setValue(value);
    }

    public void setCompassDirection(final float targetDirection) {
        this.mCompassView.setTargetDirection(targetDirection);
    }

    public void setPressure(final float value) {
        this.mPressure.setValue(value);
    }

    public void setTheme(ThemeModel theme){
        mCompassView.setTheme(theme);
    }
}
