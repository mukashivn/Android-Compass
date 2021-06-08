package com.core.compass.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.core.compass.di.ApplicationContext;
import com.core.compass.di.PreferenceInfo;
import com.core.compass.utils.AppConstants;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by CuongCK on 2/13/17.
 */
@Singleton
public class AppPreferencesHelper implements PreferencesHelper {
    private final SharedPreferences mPrefs;
    private final static String PREF_KEY_ACCESS_TOKEN = "Access_Token";
    private final static String PREF_KEY_LAST_LONG = "PREF_KEY_LAST_LONG";
    private final static String PREF_KEY_LAST_LAN = "PREF_KEY_LAST_LAN";
    private final static String PREF_KEY_LAST_ADDR = "PREF_KEY_LAST_ADDR";
    private final static String PREF_KEY_CURRENT_THEME = "PREF_KEY_CURRENT_THEME";
    private final static String PREF_KEY_DONT_ASK_AGAIN = "PREF_KEY_DONT_ASK_AGAIN";
    private final static String PREF_KEY_SHOW_MEGA = "PREF_KEY_SHOW_MEGA";
    private final static String PREF_KEY_KEEP_ON_SCREEN = "PREF_KEY_KEEP_ON_SCREEN";
    private final static String PREF_KEY_SET_FIRST_RUN = "PREF_KEY_SET_FIRST_RUN";
    private final static String PREF_KEY_SET_PRO_VERSION = "PREF_KEY_SET_PRO_VERSION";
    private final static String PREF_KEY_DEV_PAYLOAD = "PREF_KEY_DEV_PAYLOAD";
    private final static String PREF_KEY_SLP = "PREF_KEY_SLP";

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String prefGetAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, "");
    }

    @Override
    public void prefSaveAccessToken(String token) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply();
    }

    @Override
    public void saveLastLongtitude(double lon) {
        mPrefs.edit().putString(PREF_KEY_LAST_LONG, String.valueOf(lon)).apply();
    }

    @Override
    public void saveLastLat(double lat) {
        mPrefs.edit().putString(PREF_KEY_LAST_LAN, String.valueOf(lat)).apply();
    }

    @Override
    public void saveLastAddress(String address) {
        mPrefs.edit().putString(PREF_KEY_LAST_ADDR, address).apply();
    }

    @Override
    public double getLastLong() {
        String value =  mPrefs.getString(PREF_KEY_LAST_LONG,"0");
        return Double.valueOf(value);
    }

    @Override
    public double getLastLan() {
        String value =  mPrefs.getString(PREF_KEY_LAST_LAN,"0");
        return Double.valueOf(value);
    }

    @Override
    public String getLastAddress() {
        return mPrefs.getString(PREF_KEY_LAST_ADDR,"--");
    }

    @Override
    public void saveCurrentTheme(int current) {
        mPrefs.edit().putInt(PREF_KEY_CURRENT_THEME,current).apply();
    }

    @Override
    public int getCurrentTheme() {
        return mPrefs.getInt(PREF_KEY_CURRENT_THEME, AppConstants.THEME_DARK);
    }

    @Override
    public void setDontAskAgain() {
        mPrefs.edit().putBoolean(PREF_KEY_DONT_ASK_AGAIN,true).apply();
    }

    @Override
    public boolean getDontAskAgain() {
        return mPrefs.getBoolean(PREF_KEY_DONT_ASK_AGAIN,false);
    }

    @Override
    public void setShowMagenatic(boolean isShow) {
        mPrefs.edit().putBoolean(PREF_KEY_SHOW_MEGA,isShow).apply();
    }

    @Override
    public boolean getShowMagenatic() {
        return mPrefs.getBoolean(PREF_KEY_SHOW_MEGA,true);
    }

    @Override
    public void setKeepOnScreen(boolean isKeep) {
        mPrefs.edit().putBoolean(PREF_KEY_KEEP_ON_SCREEN,isKeep).apply();
    }

    @Override
    public boolean getKeepOnScreen() {
        return mPrefs.getBoolean(PREF_KEY_KEEP_ON_SCREEN,false);
    }

    @Override
    public void setFirstRun() {
        mPrefs.edit().putBoolean(PREF_KEY_SET_FIRST_RUN,false).apply();
    }

    @Override
    public boolean getFirstRun() {
        return mPrefs.getBoolean(PREF_KEY_SET_FIRST_RUN,true);
    }

    @Override
    public void setUpdateProVersion() {
        mPrefs.edit().putBoolean(PREF_KEY_SET_PRO_VERSION,true).apply();
    }

    @Override
    public boolean isProVersion() {
        return mPrefs.getBoolean(PREF_KEY_SET_PRO_VERSION,false);
    }

    @Override
    public void setDeveloperPayLoad(String payLoad) {
        mPrefs.edit().putString(PREF_KEY_DEV_PAYLOAD,payLoad).apply();
    }

    @Override
    public String getDeveloperPayLoad() {
        return mPrefs.getString(PREF_KEY_DEV_PAYLOAD,null);
    }

    @Override
    public void saveLSP(float slp) {
        mPrefs.edit().putFloat(PREF_KEY_SLP,slp).apply();
    }

    @Override
    public float getSlp() {
        return mPrefs.getFloat(PREF_KEY_SLP,0);
    }
}
