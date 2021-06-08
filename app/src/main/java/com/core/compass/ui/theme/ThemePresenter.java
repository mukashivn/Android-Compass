package com.core.compass.ui.theme;

import com.core.compass.data.DataManager;
import com.core.compass.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Package: com.core.ssvapp.ui.Theme
 * Created by: CuongCK
 * Date: 3/9/18
 */

public class ThemePresenter<V extends IThemeView> extends BasePresenter<V> implements IThemePresenter<V> {
    @Inject
    public ThemePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void init() {
        getMvpView().showLocalDB(getDataManager().getAllTheme());
    }
}
