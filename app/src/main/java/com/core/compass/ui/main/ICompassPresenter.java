package com.core.compass.ui.main;

import android.content.Context;
import android.hardware.SensorEvent;

import com.core.compass.di.PerActivity;
import com.core.compass.ui.base.BaseActivity;
import com.core.compass.ui.base.IPresenter;

/**
 * Package: com.core.ssvapp.ui.main
 * Created by: CuongCK
 * Date: 3/9/18
 */
@PerActivity
public interface ICompassPresenter<V extends ICompassView> extends IPresenter<V> {
    void updateCompass(float[] values);

    void updateLatLon(double latitude, double longitude, String cityName, double altitude);

    void loadData(BaseActivity context);

    void onMagenaticChange(SensorEvent sensorEvent);

    void changeTheme();

    void handleExitApp();

    void handleFlash(Context context);

    void noRate(boolean isDontShow, boolean onExit);

    void rate(boolean isDontShow, boolean onExit);

    void changeSetting();

    void init(BaseActivity context);

    void loadBannerAds();

    void gotoMap();

    void resume();

    void updatePressure(float v);
}
