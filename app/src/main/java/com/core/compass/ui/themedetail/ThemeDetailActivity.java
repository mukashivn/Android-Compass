package com.core.compass.ui.themedetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.core.compass.utils.AppConstants;
import com.core.ssvapp.R;
import com.core.compass.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Package: com.core.ssvapp.ui.theme2
 * Created by: CuongCK
 * Date: 3/12/18
 */

public class ThemeDetailActivity extends BaseActivity implements IThemeDetailView {
    @BindView(R.id.screen_title)
    TextView screenTitle;
    @BindView(R.id.theme_style_name)
    TextView themeStyleName;
    @BindView(R.id.compass_image)
    ImageView compassImage;
    @BindView(R.id.choice)
    TextView choice;
    @BindView(R.id.rootView)
    RelativeLayout rootView;
    @BindView(R.id.arrow)
    ImageView arrow;
    @BindView(R.id.toolbar)
    RelativeLayout rootToolbar;

    @Inject
    IThemeDetailPresenter<IThemeDetailView> mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_theme_detail;
    }

    @Override
    public void inject() {
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    public void init() {
        int themeStyle = getIntent().getIntExtra(AppConstants.THEME_EXTRA, AppConstants.THEME_DARK);
        mPresenter.showTheme(this, themeStyle);
    }

    @Override
    protected void setUp() {

    }

    @OnClick(R.id.arrow)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void showTheme(String title, int titleColor, int compassId, int windowBg, Drawable chooseBG) {
        screenTitle.setText(title);
        themeStyleName.setTextColor(titleColor);
        rootToolbar.setBackgroundColor(windowBg);
        screenTitle.setTextColor(titleColor);
        arrow.setColorFilter(titleColor, android.graphics.PorterDuff.Mode.SRC_IN);

        compassImage.setImageResource(compassId);
        rootView.setBackgroundColor(windowBg);
        choice.setTextColor(titleColor);
        choice.setBackground(chooseBG);
    }

    @Override
    public void complete() {
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.choice)
    public void chooseTheme() {
        mPresenter.chooseTheme();
    }
}
