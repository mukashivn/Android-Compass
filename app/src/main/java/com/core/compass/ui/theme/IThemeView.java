package com.core.compass.ui.theme;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.ui.base.IView;

import java.util.ArrayList;

/**
 * Package: com.core.ssvapp.ui.Theme
 * Created by: CuongCK
 * Date: 3/9/18
 */

public interface IThemeView extends IView {
    void setResult();

    void showLocalDB(ArrayList<ThemeModel> list);
}
