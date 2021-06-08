package com.core.compass.ui.theme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.ui.adapter.ThemeAdapter;
import com.core.compass.ui.themedetail.ThemeDetailActivity;
import com.core.compass.utils.AppConstants;
import com.core.ssvapp.R;
import com.core.compass.ui.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Package: com.core.ssvapp.ui.Theme
 * Created by: CuongCK
 * Date: 3/9/18
 */

public class ThemeActivity extends BaseActivity implements IThemeView, ThemeAdapter.IThemeListCB {
    @BindView(R.id.screen_title)
    TextView screenTitle;
    @BindView(R.id.reycleView)
    RecyclerView reycleView;

    private static final int REQUEST_CHANGE_THEME = 1001;
    @Inject
    IThemePresenter<IThemeView> mPresenter;

    private ThemeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_theme;
    }

    @Override
    public void inject() {
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mPresenter.init();
    }

    @Override
    public void init() {
        screenTitle.setText(R.string.theme_gallery);
    }

    @Override
    protected void setUp() {
        reycleView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @OnClick({R.id.arrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.arrow:
                finish();
                break;
        }
    }

    @Override
    public void setResult() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showLocalDB(ArrayList<ThemeModel> list) {
        mAdapter = new ThemeAdapter(this, list, this);
        reycleView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_THEME) {
            if (resultCode == RESULT_OK) {
                setResult();
            }
        }
    }

    @Override
    public void onThemeDetail(int themeId) {
        Intent iten = new Intent(this, ThemeDetailActivity.class);
        iten.putExtra(AppConstants.THEME_EXTRA,themeId);
        startActivityForResult(iten,REQUEST_CHANGE_THEME);
    }
}
