package com.core.compass.ui.setting;

import android.content.Intent;

import com.core.compass.di.PerActivity;
import com.core.compass.ui.base.BaseActivity;
import com.core.compass.ui.base.IPresenter;

/**
 * Package: com.core.ssvapp.ui.setting
 * Created by: CuongCK
 * Date: 3/12/18
 */
@PerActivity
public interface ISettingPresenter<V extends ISettingView> extends IPresenter<V> {
    void finish();

    void init(BaseActivity context);

    void updatePro();

    void buyBeer();
}
