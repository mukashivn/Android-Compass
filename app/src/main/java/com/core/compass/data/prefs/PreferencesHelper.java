package com.core.compass.data.prefs;

/**
 * Created by CuongCK on 2/13/17.
 */

public interface PreferencesHelper {
    //Define all method here
    String prefGetAccessToken();

    void prefSaveAccessToken(String token);

    void saveLastLongtitude(double lon);

    void saveLastLat(double lat);

    void saveLastAddress(String address);

    double getLastLong();

    double getLastLan();

    String getLastAddress();

    void saveCurrentTheme(int current);

    int getCurrentTheme();

    void setDontAskAgain();

    boolean getDontAskAgain();

    void setShowMagenatic(boolean isShow);

    boolean getShowMagenatic();

    void setKeepOnScreen(boolean isKeep);

    boolean getKeepOnScreen();

    void setFirstRun();

    boolean getFirstRun();

    void setUpdateProVersion();

    boolean isProVersion();

    void setDeveloperPayLoad(String payLoad);

    String getDeveloperPayLoad();

    void saveLSP(float slp);

    float getSlp();
}
