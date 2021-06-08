package com.core.compass.ui.themedetail;

import android.content.Context;

import com.core.compass.data.DataManager;
import com.core.compass.data.model.ThemeModel;
import com.core.compass.utils.CommonUtils;
import com.core.compass.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Package: com.core.ssvapp.ui.theme2
 * Created by: CuongCK
 * Date: 3/12/18
 */

public class ThemeDetailPresenter<V extends IThemeDetailView> extends BasePresenter<V> implements IThemeDetailPresenter<V> {
    private int themeId;
    @Inject
    public ThemeDetailPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void showTheme(Context context, int themeStyle) {
        ThemeModel themeModel = getDataManager().getThemeById(themeStyle);
        themeId = themeStyle;

        String titleID = themeModel.getThemeName();
        int titleColor = CommonUtils.parserColor(themeModel.getTextColor());
        int compassId = CommonUtils.getDrawableIDFromName(context,themeModel.getImage_theme());
        int windowBg = CommonUtils.parserColor(themeModel.getWindowBgColor());
        getMvpView().showTheme(titleID, titleColor, compassId, windowBg, CommonUtils.getDrawableFromName(context,themeModel.getChoose_bg()));
    }

    @Override
    public void chooseTheme() {
        getDataManager().setThemeDefault(themeId);
        getMvpView().complete();
    }
}
