package com.core.compass.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.core.compass.ui.base.BaseActivity;
import com.core.ssvapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Package: com.core.ssvapp.ui.setting
 * Created by: CuongCK
 * Date: 3/9/18
 */

public class SetttingActivity extends BaseActivity implements ISettingView {
    @BindView(R.id.screen_title)
    TextView screenTitle;
    @BindView(R.id.magnetic_check)
    CheckBox magneticCheck;
    @BindView(R.id.keep_on_screen_check)
    CheckBox keepOnScreenCheck;
    @BindView(R.id.layout_pro)
    LinearLayout layoutPro;

    @Inject
    ISettingPresenter<ISettingView> mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void inject() {
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    public void init() {
        screenTitle.setText(R.string.settings);
    }

    @Override
    protected void setUp() {
        mPresenter.init(this);
    }

    @OnClick({R.id.arrow, R.id.pro_update, R.id.buy_beer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.arrow:
                mPresenter.finish();
                break;
            case R.id.pro_update:
                mPresenter.updatePro();
                break;
            case R.id.buy_beer:
                mPresenter.buyBeer();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        mPresenter.finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public CheckBox getMagenaticView() {
        return magneticCheck;
    }

    @Override
    public CheckBox getKeepOnView() {
        return keepOnScreenCheck;
    }

    @Override
    public void complete() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showOrHideProView(int vis) {
        layoutPro.setVisibility(vis);
    }

}
