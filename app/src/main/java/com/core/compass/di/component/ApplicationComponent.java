package com.core.compass.di.component;

import android.app.Application;
import android.content.Context;

import com.core.compass.data.DataManager;
import com.core.compass.di.ApplicationContext;
import com.core.compass.ApplicationImpl;
import com.core.compass.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by CuongCK on 2/13/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(ApplicationImpl app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}
