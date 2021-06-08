package com.core.compass.ui.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.utils.Utils;
import com.core.ssvapp.R;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/27/18
 */

public class GradienterScreen extends LyingPortraitView {
    private GradienterView mGradienterLying;
    private GradienterView mGradienterPortrait;
    private TextView mLayoutAngleLying;
    private TextView mLayoutAnglePortrait;

    public GradienterScreen(final Context context) {
        this(context, null);
    }

    public GradienterScreen(final Context context, final AttributeSet set) {
        super(context, set);
        ((LayoutInflater) this.getContext().getApplicationContext().getSystemService("layout_inflater")).inflate(R.layout.gradienter_screen, (ViewGroup) this, true);
        (this.mGradienterLying = (GradienterView) this.findViewById(R.id.gradienter_view_lying)).setIsPortrait(false);
        (this.mGradienterPortrait = (GradienterView) this.findViewById(R.id.gradienter_view_portrait)).setIsPortrait(true);
        this.mLayoutAngleLying = (TextView) this.findViewById(R.id.layout_angle_lying);
        this.mLayoutAnglePortrait = (TextView) this.findViewById(R.id.layout_angle_portrait);
        final Typeface typeface = Utils.getTypeface(this.getContext().getAssets(), "SourceSansPro-Light.ttf");
        this.mLayoutAngleLying.setTypeface(typeface);
        this.mLayoutAnglePortrait.setTypeface(typeface);
    }

    protected int getLyingViewId() {
        return R.id.gradienter_layout_lying;
    }

    protected int getPortraitViewId() {
        return R.id.gradienter_layout_portrait;
    }

    protected float getRotationCenterY() {
        return this.getHeight() / 2.0f;
    }

    public void setGradienterDirection(final float n, final float n2) {
        this.mGradienterLying.setDirection(n, n2);
        this.mLayoutAngleLying.setText((CharSequence) Utils.formatToArabicNums(this.getContext(), R.string.angle, this.mGradienterLying.getDirectionLying()));
        this.mGradienterPortrait.setDirection(n, n2);
        this.mLayoutAnglePortrait.setText((CharSequence) Utils.formatToArabicNums(this.getContext(), R.string.angle, this.mGradienterLying.getDirectionPortart()));
    }

    public void setTheme(ThemeModel theme) {
        this.mGradienterLying.setTheme(theme);
    }
}
