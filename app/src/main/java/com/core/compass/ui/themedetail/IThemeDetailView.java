package com.core.compass.ui.themedetail;

import android.graphics.drawable.Drawable;

import com.core.compass.ui.base.IView;

/**
 * Package: com.core.ssvapp.ui.theme2
 * Created by: CuongCK
 * Date: 3/12/18
 */

public interface IThemeDetailView extends IView {
    void showTheme(String title, int titleColor, int compassId, int windowBg, Drawable choose);

    void complete();
}
