package com.core.compass.data;

import android.content.Context;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.data.network.ApiHelper;
import com.core.compass.data.prefs.PreferencesHelper;
import com.core.compass.data.realm.AppRealmHelper;
import com.core.compass.di.ApplicationContext;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;

/**
 * Created by CuongCK on 2/13/17.
 */
@Singleton
public class AppDataManager implements DataManager {
    private static final String TAG = "AppDataManager";


    private final Context mContext;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;


    @Inject
    public AppDataManager(@ApplicationContext Context mContext, PreferencesHelper mPreferencesHelper, ApiHelper mApiHelper) {
        this.mContext = mContext;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mApiHelper = mApiHelper;
    }

    @Override
    public String prefGetAccessToken() {
        return mPreferencesHelper.prefGetAccessToken();
    }

    @Override
    public void prefSaveAccessToken(String token) {
        mPreferencesHelper.prefSaveAccessToken(token);
    }

    @Override
    public void saveLastLongtitude(double lon) {
        mPreferencesHelper.saveLastLongtitude(lon);
    }

    @Override
    public void saveLastLat(double lat) {
        mPreferencesHelper.saveLastLat(lat);
    }

    @Override
    public void saveLastAddress(String address) {
        mPreferencesHelper.saveLastAddress(address);
    }

    @Override
    public double getLastLong() {
        return mPreferencesHelper.getLastLong();
    }

    @Override
    public double getLastLan() {
        return mPreferencesHelper.getLastLan();
    }

    @Override
    public String getLastAddress() {
        return mPreferencesHelper.getLastAddress();
    }

    @Override
    public void saveCurrentTheme(int current) {
        mPreferencesHelper.saveCurrentTheme(current);
    }

    @Override
    public int getCurrentTheme() {
        return mPreferencesHelper.getCurrentTheme();
    }

    @Override
    public void setDontAskAgain() {
        mPreferencesHelper.setDontAskAgain();
    }

    @Override
    public boolean getDontAskAgain() {
        return mPreferencesHelper.getDontAskAgain();
    }

    @Override
    public void setShowMagenatic(boolean isShow) {
        mPreferencesHelper.setShowMagenatic(isShow);
    }

    @Override
    public boolean getShowMagenatic() {
        return mPreferencesHelper.getShowMagenatic();
    }

    @Override
    public void setKeepOnScreen(boolean isKeep) {
        mPreferencesHelper.setKeepOnScreen(isKeep);
    }

    @Override
    public boolean getKeepOnScreen() {
        return mPreferencesHelper.getKeepOnScreen();
    }

    @Override
    public void setFirstRun() {
        mPreferencesHelper.setFirstRun();
    }

    @Override
    public boolean getFirstRun() {
        return mPreferencesHelper.getFirstRun();
    }

    @Override
    public void setUpdateProVersion() {
        mPreferencesHelper.setUpdateProVersion();
    }

    @Override
    public boolean isProVersion() {
        return mPreferencesHelper.isProVersion();
    }

    @Override
    public void setDeveloperPayLoad(String payLoad) {
        mPreferencesHelper.setDeveloperPayLoad(payLoad);
    }

    @Override
    public String getDeveloperPayLoad() {
        return mPreferencesHelper.getDeveloperPayLoad();
    }

    @Override
    public void saveLSP(float slp) {
        mPreferencesHelper.saveLSP(slp);
    }

    @Override
    public float getSlp() {
        return mPreferencesHelper.getSlp();
    }

    @Override
    public Realm getRealm() {
        return AppRealmHelper.with(mContext).getRealm();
    }

    @Override
    public void insertTheme(ThemeModel model) {
        AppRealmHelper.with(mContext).insertTheme(model);
    }

    @Override
    public ThemeModel getDefaultTheme() {
        return AppRealmHelper.with(mContext).getDefaultTheme();
    }

    @Override
    public ArrayList<ThemeModel> getAllTheme() {
        return AppRealmHelper.with(mContext).getAllTheme();
    }

    @Override
    public void setThemeDefault(int id) {
        AppRealmHelper.with(mContext).setThemeDefault(id);
    }

    @Override
    public void setThemePayment(int id) {
        AppRealmHelper.with(mContext).setThemePayment(id);
    }

    @Override
    public void insertThemeList(ArrayList<ThemeModel> themeModels) {
        AppRealmHelper.with(mContext).insertThemeList(themeModels);
    }

    @Override
    public ThemeModel getThemeById(int id) {
        return AppRealmHelper.with(mContext).getThemeById(id);
    }
}
