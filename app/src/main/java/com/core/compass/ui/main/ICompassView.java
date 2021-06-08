package com.core.compass.ui.main;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.ui.base.IView;

/**
 * Package: com.core.ssvapp.ui.main
 * Created by: CuongCK
 * Date: 3/9/18
 */

public interface ICompassView extends IView {
    void updateCompassView(float mDirection, float n, float n2);

    void updateLatLon(double latitude, double longitude, String cityName);

    void showMagenatic(double tesla);

    void showTheme(ThemeModel currentTheme);

    void exitAppNow();

    void showAskDialog();

    void noRate();

    void rate();

    LinearLayout getLayoutMega();

    LinearLayout getLayoutPressure();

    LinearLayout getLayoutAlitude();

    TextView getAddressView();

    LinearLayout getActionMap();

    void gotoMapView(double lat, double lon, String addrs);

    void showOrHideAd(int i);

    void showPressure(String pressure, float value);

    void updateAltitude(float altitude);

    void loadBannerAds();
}
