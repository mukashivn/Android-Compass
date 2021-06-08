package com.core.compass.data;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.Iterator;
import android.location.GpsStatus;
import android.os.Bundle;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.util.Log;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.location.LocationManager;
import android.location.Location;
import android.content.Context;
import android.net.ConnectivityManager;

import com.core.compass.data.prefs.CompassPreferences;
import com.core.compass.utils.CompassDeviceUtils;
import com.core.compass.utils.Utils;

/**
 * Package: com.core.ssvapp.data
 * Created by: CuongCK
 * Date: 2/28/18
 */

public class CompassDataManager {
    private boolean mCancelCalibrateAltitude;
    private CompassData mCompassData;
    private CompassDataObserver mCompassDataObserver;
    private CompassLocationListener mCompassLocationListener;
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private GpsStatusListener mGpsStatusListener;
    private GpsStatusNmeaListener mGpsStatusNmeaListener;
    private long mLastLogTime;
    private Location mLocation;
    private LocationGetter mLocationGetter;
    private LocationManager mLocationManager;
   // private PressureRequestor mPressureRequestor;
    private Sensor mPressureSensor;
    private SensorEventListener mPressureSensorEventListener;
    private float mSeaLevelPressure;
    private SensorManager mSensorManager;

    public CompassDataManager(final Context mContext) {
        this.mPressureSensorEventListener = (SensorEventListener)new SensorEventListener() {
            public void onAccuracyChanged(final Sensor sensor, final int n) {
                if (n != 2 && n != 3) {
                    CompassDataManager.this.mCompassData.mPressure = -404.0f;
                    CompassDataManager.this.notifyCompassDataChanged();
                }
            }

            public void onSensorChanged(final SensorEvent sensorEvent) {
                final float n = sensorEvent.values[0];
                CompassDataManager.this.mCompassData.mPressure = n;
                if (CompassDataManager.this.isSLPAccurate()) {
                    CompassDataManager.this.mCompassData.mAltitudeAccuracy = AltitudeAccuracy.RELIABLE;
                }
                else if (Utils.isNetworkAvailable(CompassDataManager.this.mConnectivityManager)) {
                    CompassDataManager.this.mCompassData.mAltitudeAccuracy = AltitudeAccuracy.IMPRECISE;
                }
                else {
                    CompassDataManager.this.mCompassData.mAltitudeAccuracy = AltitudeAccuracy.REFERENTIAL;
                }
                CompassDataManager.this.mCompassData.mAltitude = SensorManager.getAltitude(CompassDataManager.this.mSeaLevelPressure, n);
                CompassDataManager.this.notifyCompassDataChanged();
            }
        };
        this.mContext = mContext;
        this.mLocationManager = (LocationManager)this.mContext.getApplicationContext().getSystemService("location");
       // this.mPressureRequestor = new PressureRequestor(this.mContext);
        this.mConnectivityManager = (ConnectivityManager)this.mContext.getApplicationContext().getSystemService("connectivity");
        this.mSensorManager = (SensorManager)this.mContext.getApplicationContext().getSystemService("sensor");
        this.mPressureSensor = this.mSensorManager.getDefaultSensor(6);
        if (this.mPressureSensor == null) {
            Log.e("Compass:CompassDataManager", "PressureSensor is null");
        }
        this.mSeaLevelPressure = CompassPreferences.getSeaLevelPressure(this.mContext);
        this.mCompassData = new CompassData();
        this.mCompassLocationListener = new CompassLocationListener();
        this.mGpsStatusListener = new GpsStatusListener();
        this.mGpsStatusNmeaListener = new GpsStatusNmeaListener();
    }

    private int doCalibrateAltitude() {
        Log.i("Compass:CompassDataManager", "doCalibrateAltitude begin");
        final long currentTimeMillis = System.currentTimeMillis();
        while (true) {
            if (this.mLocation == null) {
                (this.mLocationGetter = new LocationGetter()).start();
                while (true) {
                    try {
                        this.mLocationGetter.join(20000L);
                        this.mLocationGetter.stopRunning();
                        Log.i("Compass:CompassDataManager", "doCalibrateAltitude get location complete");
                        if (this.mCancelCalibrateAltitude) {
                            return 3;
                        }
                    }
                    catch (InterruptedException ex) {
                        Log.e("Compass:CompassDataManager", "LocationGetter thread is interrupted mLocation", (Throwable)ex);
                        continue;
                    }
                    break;
                }
                int requestPressure=-1;
                if (this.mLocation != null) {
                   // requestPressure = this.mPressureRequestor.requestPressure(currentTimeMillis, this.mLocation.getLongitude(), this.mLocation.getLatitude());
                    this.mSeaLevelPressure = CompassPreferences.getSeaLevelPressure(this.mContext);
                }
                else {
                    requestPressure = 1;
                    Log.i("Compass:CompassDataManager", "doCalibrateAltitude mLocation is null");
                }
                if (this.mCancelCalibrateAltitude) {
                    requestPressure = 3;
                }
               // Log.i("Compass:CompassDataManager", "doCalibrateAltitude complete result:" + requestPressure);
                return requestPressure;
            }
            continue;
        }
    }

    @SuppressLint("MissingPermission")
    private Location getGpsLocation() {
        return this.mLocationManager.getLastKnownLocation("gps");
    }

    @SuppressLint("MissingPermission")
    private Location getNetworkLocation() {
        return this.mLocationManager.getLastKnownLocation("network");
    }

    private boolean isSLPAccurate() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long lastSLPCalibrationTime = CompassPreferences.getLastSLPCalibrationTime(this.mContext);
        return CompassPreferences.getLastSLPAccuracy(this.mContext) == 9999.0f && currentTimeMillis - lastSLPCalibrationTime <= 900000L;
    }

    private void notifyCompassDataChanged() {
        if (this.mCompassDataObserver != null) {
            this.mCompassDataObserver.notifyCompassDataChanged(this.mCompassData);
        }
    }

    private void updateLocation(final Location mLocation) {
        this.mLocation = mLocation;
        if (this.mLocation != null) {
            this.mCompassData.mLongitude = this.mLocation.getLongitude();
            this.mCompassData.mLatitude = this.mLocation.getLatitude();
        }
        else {
            this.mCompassData.mLongitude = -404.0;
            this.mCompassData.mLatitude = -404.0;
        }
        this.notifyCompassDataChanged();
    }

    private static void updateSLP(final CompassDataManager compassDataManager, final float n, final float n2) {
        final long currentTimeMillis = System.currentTimeMillis();
        final long lastSLPCalibrationTime = CompassPreferences.getLastSLPCalibrationTime(compassDataManager.mContext);
        if (n2 > CompassPreferences.getLastSLPAccuracy(compassDataManager.mContext) || currentTimeMillis - lastSLPCalibrationTime > 3600000L) {
            compassDataManager.mSeaLevelPressure = (float)Utils.calculateSLP(n, compassDataManager.mCompassData.mPressure);
            CompassPreferences.saveSeaLevelPressure(compassDataManager.mContext, compassDataManager.mSeaLevelPressure, n2);
        }
    }

    public void cancelCalibrating() {
        if (this.mLocationGetter != null) {
            this.mLocationGetter.cancel();
        }
        this.mCancelCalibrateAltitude = true;
        //this.mPressureRequestor.abortPressureRequest();
    }

    public void enableLocationProvider() {
        Settings.Secure.putInt(this.mContext.getContentResolver(), "location_mode", 3);
        this.updateLocation(this.mLocation);
    }

    public boolean isLocationProviderEnabled() {
        return this.mLocationManager.isProviderEnabled("network") || this.mLocationManager.isProviderEnabled("gps");
    }

    public boolean isPressureSensorAvailable() {
        return this.mPressureSensor != null;
    }

    public void setCompassDataObserver(final CompassDataObserver mCompassDataObserver) {
        this.mCompassDataObserver = mCompassDataObserver;
    }

    public void startCalibrateAltitude(final boolean b) {
        Log.i("Compass:CompassDataManager", "startCalibrateAltitude");
        this.mCancelCalibrateAltitude = false;
        new CalibratAltitudeTask(b).execute((Void[]) new Void[0]);
    }

    public void startMonitor() {
        this.mLocation = this.getGpsLocation();
        if (this.mLocation == null) {
            this.mLocation = this.getNetworkLocation();
        }
        this.mCompassLocationListener.register(this.mLocationManager, this);
        if (CompassDeviceUtils.isSupportNmeaAlt()) {
            this.mGpsStatusNmeaListener.register(this.mLocationManager, this);
        }
        else {
            this.mGpsStatusListener.register(this.mLocationManager, this);
        }
        if (this.isPressureSensorAvailable()) {
            this.mSensorManager.registerListener(this.mPressureSensorEventListener, this.mPressureSensor, 3);
        }
        this.updateLocation(this.mLocation);
    }

    public void stopMonitor() {
        this.mCompassLocationListener.unregister(this.mLocationManager);
        if (CompassDeviceUtils.isSupportNmeaAlt()) {
            this.mGpsStatusNmeaListener.unregister(this.mLocationManager);
        }
        else {
            this.mGpsStatusListener.unregister(this.mLocationManager);
        }
        if (this.isPressureSensorAvailable()) {
            this.mSensorManager.unregisterListener(this.mPressureSensorEventListener);
        }
    }

    public enum AltitudeAccuracy
    {
        IMPRECISE,
        REFERENTIAL,
        RELIABLE;
    }

    private class CalibratAltitudeTask extends AsyncTask<Void, Void, Integer>
    {
        private boolean mNotifyUser;

        public CalibratAltitudeTask(final boolean mNotifyUser) {
            this.mNotifyUser = mNotifyUser;
        }

        protected Integer doInBackground(final Void... array) {
            return CompassDataManager.this.doCalibrateAltitude();
        }

        protected void onPostExecute(final Integer n) {
            if (CompassDataManager.this.mCompassDataObserver != null) {
                CompassDataManager.this.mCompassDataObserver.onCalibrateComplete(n, this.mNotifyUser);
            }
            CompassDataManager.this.updateLocation(CompassDataManager.this.mLocation);
        }

        protected void onPreExecute() {
            if (CompassDataManager.this.mCompassDataObserver != null) {
                CompassDataManager.this.mCompassDataObserver.onCalibrateStart(this.mNotifyUser);
            }
        }
    }

    public static class CompassData
    {
        private float mAltitude;
        private AltitudeAccuracy mAltitudeAccuracy;
        private double mLatitude;
        private double mLongitude;
        private float mPressure;

        public CompassData() {
            this.mLongitude = -404.0;
            this.mLatitude = -404.0;
            this.mPressure = -404.0f;
        }

        public float getAltitude() {
            return this.mAltitude;
        }

        public AltitudeAccuracy getAltitudeAccuracy() {
            return this.mAltitudeAccuracy;
        }

        public double getLatitude() {
            return this.mLatitude;
        }

        public double getLongitude() {
            return this.mLongitude;
        }

        public float getPressure() {
            return this.mPressure;
        }
    }

    public interface CompassDataObserver
    {
        void notifyCompassDataChanged(final CompassData p0);

        void onCalibrateComplete(final int p0, final boolean p1);

        void onCalibrateStart(final boolean p0);
    }

    private static class CompassLocationListener implements LocationListener
    {
        private static CompassDataManager mCompassDataManager;

        public void onLocationChanged(final Location location) {
            if (CompassLocationListener.mCompassDataManager != null) {
                CompassLocationListener.mCompassDataManager.updateLocation(location);
            }
        }

        public void onProviderDisabled(final String s) {
        }

        public void onProviderEnabled(final String s) {
        }

        public void onStatusChanged(final String s, final int n, final Bundle bundle) {
            if (CompassLocationListener.mCompassDataManager != null) {
                if (n != 0) {
                    CompassLocationListener.mCompassDataManager.updateLocation(CompassLocationListener.mCompassDataManager.getGpsLocation());
                }
                else if (System.currentTimeMillis() - CompassLocationListener.mCompassDataManager.mLastLogTime > 60000L) {
                    Log.w("Compass:CompassDataManager", "location provider:" + s + " is out of service");
                    CompassLocationListener.mCompassDataManager.mLastLogTime = System.currentTimeMillis();
                }
            }
        }

        @SuppressLint("MissingPermission")
        public void register(final LocationManager locationManager, final CompassDataManager mCompassDataManager) {
            CompassLocationListener.mCompassDataManager = mCompassDataManager;
            locationManager.requestLocationUpdates("gps", 2000L, 10.0f, (LocationListener)this);
        }

        public void unregister(final LocationManager locationManager) {
            locationManager.removeUpdates((LocationListener)this);
            CompassLocationListener.mCompassDataManager = null;
        }
    }

    private static class GpsStatusListener implements GpsStatus.Listener
    {
        private static CompassDataManager mCompassDataManager;

        public void onGpsStatusChanged(int n) {
            if (GpsStatusListener.mCompassDataManager != null) {
                @SuppressLint("MissingPermission") final GpsStatus gpsStatus = GpsStatusListener.mCompassDataManager.mLocationManager.getGpsStatus((GpsStatus)null);
                if (n == 4) {
                    final int maxSatellites = gpsStatus.getMaxSatellites();
                    n = 0;
                    final Iterator iterator = gpsStatus.getSatellites().iterator();
                    while (iterator.hasNext() && n <= maxSatellites) {
                        ++n;
                        iterator.next();
                    }
                    final Location access$1800 = GpsStatusListener.mCompassDataManager.getGpsLocation();
                    if (!GpsStatusListener.mCompassDataManager.isSLPAccurate() && access$1800 != null && access$1800.hasAltitude()) {
                        updateSLP(GpsStatusListener.mCompassDataManager, (float)access$1800.getAltitude(), n);
                    }
                }
            }
        }

        @SuppressLint("MissingPermission")
        public void register(final LocationManager locationManager, final CompassDataManager mCompassDataManager) {
            GpsStatusListener.mCompassDataManager = mCompassDataManager;
            locationManager.addGpsStatusListener((GpsStatus.Listener)this);
        }

        public void unregister(final LocationManager locationManager) {
            locationManager.removeGpsStatusListener((GpsStatus.Listener)this);
            GpsStatusListener.mCompassDataManager = null;
        }
    }

    private static class GpsStatusNmeaListener implements GpsStatus.NmeaListener
    {
        private static CompassDataManager mCompassDataManager;

        public void onNmeaReceived(final long n, final String s) {
            if (GpsStatusNmeaListener.mCompassDataManager != null && !GpsStatusNmeaListener.mCompassDataManager.isSLPAccurate() && !TextUtils.isEmpty((CharSequence)s) && s.contains("GPAAA")) {
                final String[] split = s.split("\\*")[0].split(",");
                updateSLP(GpsStatusNmeaListener.mCompassDataManager, Float.valueOf(split[6]), -1.0f * Float.valueOf(split[8]));
            }
        }

        @SuppressLint("MissingPermission")
        public void register(final LocationManager locationManager, final CompassDataManager mCompassDataManager) {
            GpsStatusNmeaListener.mCompassDataManager = mCompassDataManager;
            locationManager.addNmeaListener((GpsStatus.NmeaListener)this);
        }

        public void unregister(final LocationManager locationManager) {
            locationManager.removeNmeaListener((GpsStatus.NmeaListener)this);
            GpsStatusNmeaListener.mCompassDataManager = null;
        }
    }

    private class LocationGetter extends Thread
    {
        private LocationListener listener;
        private LocationManager locationManager;
        private boolean running;

        private LocationGetter() {
            this.running = true;
            this.listener = (LocationListener)new LocationListener() {
                public void onLocationChanged(final Location location) {
                }

                public void onProviderDisabled(final String s) {
                }

                public void onProviderEnabled(final String s) {
                }

                public void onStatusChanged(final String s, final int n, final Bundle bundle) {
                }
            };
        }

        public void cancel() {
            this.running = false;
        }

        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            if (CompassDataManager.this.mLocation == null) {
                this.locationManager = (LocationManager)CompassDataManager.this.mContext.getApplicationContext().getSystemService("location");
                ((Activity)CompassDataManager.this.mContext).runOnUiThread((Runnable)new Runnable() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void run() {
                        try {
                            LocationGetter.this.locationManager.requestLocationUpdates("network", 2000L, 10.0f, LocationGetter.this.listener);
                        }
                        catch (Exception ex) {
                            Log.e("Compass:CompassDataManager", "No network location provider found");
                            LocationGetter.this.running = false;
                        }
                    }
                });
                while (CompassDataManager.this.mLocation == null && this.running) {
                    CompassDataManager.this.mLocation = this.locationManager.getLastKnownLocation("network");
                }
                if (CompassDataManager.this.mLocation == null) {
                    Log.w("Compass:CompassDataManager", "doCalibrateAltitude get newwork location fail");
                    CompassDataManager.this.mLocation = CompassDataManager.this.getGpsLocation();
                }
                if (CompassDataManager.this.mLocation == null) {
                    Log.w("Compass:CompassDataManager", "doCalibrateAltitude can't get available location");
                }
                this.locationManager.removeUpdates(this.listener);
            }
        }

        public void stopRunning() throws InterruptedException {
            this.cancel();
            this.join();
        }
    }
}
