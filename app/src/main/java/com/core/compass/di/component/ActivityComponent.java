package com.core.compass.di.component;

import com.core.compass.di.PerActivity;
import com.core.compass.ui.main.CompassActivity;
import com.core.compass.ui.map.MapActivity;
import com.core.compass.ui.setting.SetttingActivity;
import com.core.compass.ui.theme.ThemeActivity;
import com.core.compass.ui.themedetail.ThemeDetailActivity;
import com.core.compass.di.module.ActivityModule;

import dagger.Component;

/**
 * Created by CuongCK on 2/13/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(CompassActivity mainActivity);

    void inject(ThemeActivity themeActivity);

    void inject(SetttingActivity setttingActivity);

    void inject(ThemeDetailActivity activity);

    void inject(MapActivity activity);

}
