package com.core.compass.ui.setting;

import android.widget.CheckBox;

import com.core.compass.ui.base.IView;

/**
 * Package: com.core.ssvapp.ui.setting
 * Created by: CuongCK
 * Date: 3/12/18
 */

public interface ISettingView extends IView {
    CheckBox getMagenaticView();

    CheckBox getKeepOnView();

    void complete();

    void showOrHideProView(int vis);
}
