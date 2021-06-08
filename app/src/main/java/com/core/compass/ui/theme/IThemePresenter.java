package com.core.compass.ui.theme;

import com.core.compass.di.PerActivity;
import com.core.compass.ui.base.IPresenter;

/**
 * Package: com.core.ssvapp.ui.Theme
 * Created by: CuongCK
 * Date: 3/9/18
 */
@PerActivity
public interface IThemePresenter<V extends IThemeView> extends IPresenter<V> {
    void init();
}
