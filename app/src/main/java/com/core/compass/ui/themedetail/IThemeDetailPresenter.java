package com.core.compass.ui.themedetail;

import android.content.Context;

import com.core.compass.di.PerActivity;
import com.core.compass.ui.base.IPresenter;

/**
 * Package: com.core.ssvapp.ui.theme2
 * Created by: CuongCK
 * Date: 3/12/18
 */
@PerActivity
public interface IThemeDetailPresenter<V extends IThemeDetailView> extends IPresenter<V> {
    void showTheme(Context context, int themeStyle);

    void chooseTheme();
}
