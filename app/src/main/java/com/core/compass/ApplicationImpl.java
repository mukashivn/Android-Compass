package com.core.compass;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.core.compass.di.component.ApplicationComponent;
import com.core.compass.di.component.DaggerApplicationComponent;
import com.core.compass.di.module.ApplicationModule;

import javax.inject.Inject;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

/**
 * Created by CuongCK on 2/13/17.
 */

public class ApplicationImpl extends MultiDexApplication {
    @Inject
    CalligraphyConfig mCalligraphyConfig;
    private ApplicationComponent mApplicationComponent;

    private static volatile Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        mCalligraphyConfig)).build());
        sContext = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext() {
        return sContext;
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}
