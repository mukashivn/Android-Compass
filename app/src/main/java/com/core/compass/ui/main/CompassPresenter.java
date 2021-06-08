package com.core.compass.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Camera;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.WindowManager;

import com.core.compass.data.DataManager;
import com.core.compass.data.model.ThemeModel;
import com.core.compass.data.model.ThemeStore;
import com.core.compass.data.model.UserPurchaseItems;
import com.core.compass.ui.base.BaseActivity;
import com.core.compass.ui.base.BasePresenter;
import com.core.compass.utils.AppConstants;
import com.core.compass.utils.CommonUtils;
import com.core.compass.utils.SensorHelper;
import com.core.compass.utils.Utils;
import com.core.ssvapp.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmChangeListener;

/**
 * Package: com.core.ssvapp.ui.main
 * Created by: CuongCK
 * Date: 3/9/18
 */

public class CompassPresenter<V extends ICompassView> extends BasePresenter<V> implements ICompassPresenter<V>, RealmChangeListener {
    boolean isFlashOn;
    Camera cam;
    CameraManager mCameraManager;
    private BaseActivity mContext;
    private float mSeaLevelPressure;
    private float mPresssure = -404.0f;
    private ServiceConnection mServiceConn;

    @Inject
    public CompassPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
        getDataManager().getRealm().addChangeListener(this);
    }

    public void init(BaseActivity context) {
        mContext = context;
        boolean isFirstRun = getDataManager().getFirstRun();
        if (isFirstRun) {
            //Load Data from ass
            try {
                String data = CommonUtils.loadJSONFromAsset(context, AppConstants.THEME_LOCAL_NAME);
                Gson gson = new Gson();
                ThemeStore themeStore = gson.fromJson(data, ThemeStore.class);
                getDataManager().insertThemeList(themeStore.getThemes());
                getDataManager().setFirstRun();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (getDataManager().getDefaultTheme() == null) {
                //Load Data from ass
                try {
                    String data = CommonUtils.loadJSONFromAsset(context, AppConstants.THEME_LOCAL_NAME);
                    Gson gson = new Gson();
                    ThemeStore themeStore = gson.fromJson(data, ThemeStore.class);
                    getDataManager().insertThemeList(themeStore.getThemes());
                    getDataManager().setFirstRun();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        loadBannerAds();
    }

    @Override
    public void loadBannerAds() {
        if (!getDataManager().isProVersion()) {
            getMvpView().loadBannerAds();
        }
    }

    @Override
    public void gotoMap() {
        double lat = getDataManager().getLastLan();
        double lon = getDataManager().getLastLong();

        if (lat == 0 || lon == 0) {
            return;
        }

        getMvpView().gotoMapView(lat, lon, getDataManager().getLastAddress());
    }

    @Override
    public void resume() {
    }

    @Override
    public void updateCompass(float[] values) {
        if (Utils.isAndroidO()) {
            final float[] array = values.clone();
            final float[] array2 = new float[9];
            SensorHelper.quatToRotMat(array2, array);
            SensorHelper.rotMatToOrient(values, array2);
        }
        float mDirection = Utils.normalizeDegree(values[0] * -1.0f);
        final float n = values[1];
        final float n2 = values[2];

        getMvpView().updateCompassView(mDirection, n, n2);
    }

    @Override
    public void updateLatLon(double latitude, double longitude, String cityName, double altitude) {
        getDataManager().saveLastAddress(cityName == null ? getDataManager().getLastAddress() : cityName);
        getDataManager().saveLastLat(latitude);
        getDataManager().saveLastLongtitude(longitude);
        getMvpView().getActionMap().setVisibility(View.VISIBLE);
        this.mSeaLevelPressure = (float) Utils.calculateSLP(altitude, mPresssure);
        getDataManager().saveLSP(this.mSeaLevelPressure);
        if (mPresssure > 0)
            getMvpView().updateAltitude(SensorManager.getAltitude(getDataManager().getSlp(), this.mPresssure));
        getMvpView().updateLatLon(latitude, longitude, cityName == null ? getDataManager().getLastAddress() : cityName);
    }

    @Override
    public void loadData(BaseActivity context) {
        mContext = context;
        if (!getDataManager().getFirstRun()) {
            ThemeModel defaultTheme = getDataManager().getDefaultTheme();
            if (defaultTheme != null) {
                //Show it
                getMvpView().showTheme(defaultTheme);
            }
        }

        getMvpView().getActionMap().setVisibility(getDataManager().getLastLan() > 0 ? View.VISIBLE : View.GONE);
        getMvpView().updateLatLon(getDataManager().getLastLan(), getDataManager().getLastLong(), getDataManager().getLastAddress());
        //getMvpView().showTheme(getDataManager().getCurrentTheme());
        handleShowHideIndex();
        if (getDataManager().getKeepOnScreen())
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    private void handleShowHideIndex() {
        getMvpView().getLayoutMega().setVisibility(getDataManager().getShowMagenatic() ? View.VISIBLE : View.INVISIBLE);
        getMvpView().getLayoutAlitude().setVisibility(getDataManager().getShowMagenatic() ? View.VISIBLE : View.INVISIBLE);
        //getMvpView().getLayoutLatitude().setVisibility(getDataManager().getShowMagenatic() ? View.VISIBLE : View.INVISIBLE);
        //getMvpView().getLayoutLongitude().setVisibility(getDataManager().getShowMagenatic() ? View.VISIBLE : View.INVISIBLE);
        getMvpView().getLayoutPressure().setVisibility(getDataManager().getShowMagenatic() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onMagenaticChange(SensorEvent sensorEvent) {
        float azmuth = Math.round(sensorEvent.values[0]);
        float pitch = Math.round(sensorEvent.values[1]);
        float round = Math.round(sensorEvent.values[2]);

        double tesla = Math.sqrt((azmuth * azmuth) + (pitch * pitch) + (round * round));

        getMvpView().showMagenatic(tesla);
    }

    @Override
    public void changeTheme() {
        //getMvpView().showTheme(getDataManager().getCurrentTheme());
    }

    @Override
    public void handleExitApp() {
        if (!getDataManager().getDontAskAgain()) {
            getMvpView().showAskDialog();
        } else {
            getMvpView().exitAppNow();
        }
    }

    @Override
    public void handleFlash(Context context) {
        if (!isFlashOn) {
            flashLightOn(context);
        } else {
            flashLightOff(context);
        }
        isFlashOn = !isFlashOn;
    }

    @Override
    public void noRate(boolean isDontShow, boolean onExit) {
        if (onExit) {
            if (isDontShow) {
                getDataManager().setDontAskAgain();
            }
            getMvpView().noRate();
        }
    }

    @Override
    public void rate(boolean isDontShow, boolean onExit) {
        if (onExit) {
            if (isDontShow) {
                getDataManager().setDontAskAgain();
            }
        }
        getMvpView().rate();
    }

    @Override
    public void changeSetting() {
        handleShowHideIndex();
        if (getDataManager().getKeepOnScreen()) {
            mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void flashLightOn(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mCameraManager == null)
                mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = null;
                if (mCameraManager != null) {
                    cameraId = mCameraManager.getCameraIdList()[0];
                }
                if (mCameraManager != null) {
                    mCameraManager.setTorchMode(cameraId, true);
                }
            } catch (Exception e) {
            }
        } else {
            try {
                if (cam == null) {
                    cam = Camera.open();
                    Camera.Parameters p = cam.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                } else {
                    cam.startPreview();
                }
            } catch (Exception e) {
                e.printStackTrace();
                cam.release();
                cam = null;
            } finally {

            }
        }
    }

    private void flashLightOff(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mCameraManager == null)
                mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = null;
                if (mCameraManager != null) {
                    cameraId = mCameraManager.getCameraIdList()[0];
                }
                if (mCameraManager != null) {
                    mCameraManager.setTorchMode(cameraId, false);
                }

            } catch (Exception e) {
            }
        } else {
            if (cam != null) {
                cam.stopPreview();
                cam.release();
                cam = null;
            }
        }
    }

    @Override
    public void onDetach() {
        if (cam != null) {
            cam.stopPreview();
            cam.release();
        }

        if (mCameraManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                try {
                    String cameraId = mCameraManager.getCameraIdList()[0];
                    mCameraManager.setTorchMode(cameraId, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        getDataManager().getRealm().removeChangeListener(this);
        super.onDetach();
    }

    @Override
    public void onChange(Object element) {
        ThemeModel defaultTheme = getDataManager().getDefaultTheme();
        if (defaultTheme != null) {
            //Show it
            getMvpView().showTheme(defaultTheme);
        }
    }

    @Override
    public void updatePressure(float v) {
        this.mPresssure = v;
        if (getDataManager().getSlp() > 0)
            getMvpView().updateAltitude(SensorManager.getAltitude(getDataManager().getSlp(), this.mPresssure));
        String pressure = String.format(mContext.getString(R.string.pressure), v);
        getMvpView().showPressure(pressure, v);
    }

}
