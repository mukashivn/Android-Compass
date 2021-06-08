package com.core.compass.di.module;

import android.app.Activity;
import android.content.Context;

import com.core.compass.ui.main.CompassPresenter;
import com.core.compass.ui.main.ICompassPresenter;
import com.core.compass.ui.main.ICompassView;
import com.core.compass.ui.map.IMapPresenter;
import com.core.compass.ui.map.IMapView;
import com.core.compass.ui.map.MapPresenter;
import com.core.compass.ui.setting.ISettingPresenter;
import com.core.compass.ui.setting.ISettingView;
import com.core.compass.ui.setting.SettingPresenter;
import com.core.compass.ui.theme.IThemePresenter;
import com.core.compass.ui.theme.IThemeView;
import com.core.compass.ui.theme.ThemePresenter;
import com.core.compass.ui.themedetail.IThemeDetailPresenter;
import com.core.compass.ui.themedetail.IThemeDetailView;
import com.core.compass.ui.themedetail.ThemeDetailPresenter;
import com.core.compass.di.ActivityContext;


import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by CuongCK on 2/13/17.
 */
@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    ICompassPresenter<ICompassView> provideCompassPresenter(CompassPresenter<ICompassView> presenter){
        return presenter;
    }

    @Provides
    IThemePresenter<IThemeView> provideThemePresenter(ThemePresenter<IThemeView> themePresenter){
        return themePresenter;
    }

    @Provides
    IThemeDetailPresenter<IThemeDetailView> provideTheme2Presenter(ThemeDetailPresenter<IThemeDetailView> theme2Presenter){
        return theme2Presenter;
    }

    @Provides
    ISettingPresenter<ISettingView> proviceSettingPresenter(SettingPresenter<ISettingView> settingPresenter){
        return settingPresenter;
    }

    @Provides
    IMapPresenter<IMapView> provideMapPresenter(MapPresenter<IMapView> mapPresenter){
        return mapPresenter;
    }
}
