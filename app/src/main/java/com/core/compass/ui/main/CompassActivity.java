package com.core.compass.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.core.compass.data.model.ThemeModel;
import com.core.compass.service.GoogleService;
import com.core.compass.ui.base.BaseActivity;
import com.core.compass.ui.custom.CompassScreen;
import com.core.compass.ui.custom.GradienterScreen;
import com.core.compass.ui.map.MapActivity;
import com.core.compass.ui.rate.DialogRateMe;
import com.core.compass.ui.setting.SetttingActivity;
import com.core.compass.ui.theme.ThemeActivity;
import com.core.compass.utils.AppConstants;
import com.core.compass.utils.CommonUtils;
import com.core.compass.utils.Utils;
import com.core.ssvapp.BuildConfig;
import com.core.ssvapp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hi on 2/24/18.
 */

public class CompassActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, SensorEventListener, ICompassView, DialogRateMe.OnDialogRateCB {

    @BindView(R.id.magenatic_index)
    TextView magenaticIndex;
    @BindView(R.id.address)
    TextView mAddressView;
    @BindView(R.id.rootView)
    FrameLayout rootView;
    @BindView(R.id.magenatic_label)
    TextView mMagenaticLabel;
    @BindView(R.id.action_theme)
    ImageView actionTheme;
    @BindView(R.id.action_setting)
    ImageView actionSetting;
    @BindView(R.id.action_share)
    ImageView actionShare;
    @BindView(R.id.action_flash)
    ImageView actionFlash;
    @BindView(R.id.action_rating)
    ImageView actionRating;
    @BindView(R.id.layout_mega)
    LinearLayout layoutMega;
    @BindView(R.id.ads_root)
    FrameLayout adsRoot;
    @BindView(R.id.pressure)
    TextView pressure;
    @BindView(R.id.pressure_title)
    TextView pressureTitle;
    @BindView(R.id.altitude_title)
    TextView altitudeTitle;
    @BindView(R.id.altitude_index)
    TextView altitudeIndex;
    @BindView(R.id.layout_press)
    LinearLayout layoutPress;
    @BindView(R.id.layout_altitude)
    LinearLayout layoutAltitude;
    @BindView(R.id.compass_screen)
    CompassScreen compassScreen;
    @BindView(R.id.scv_screen)
    FrameLayout scvScreen;
    @BindView(R.id.action_map)
    ImageView actionMap;
    @BindView(R.id.layout_map)
    LinearLayout layoutMap;
    @BindView(R.id.txt_direction_lying)
    TextView mAngleLayoutLying;
    @BindView(R.id.layout_direction_lying)
    TextView mDirectionTextViewLying;
    @BindView(R.id.ic_degree_lying)
    ImageView mDegreeLying;

    private CompassScreen mCompassScreen;
    private float mDirection;
    private GradienterScreen mGradienterScreen;
    private Sensor mOrientationSensor;
    private Sensor mMagenaticSensor;
    private Sensor mPressureSensor;
    private SensorEventListener mOrientationSensorEventListener;
    private SensorEventListener mPressureSensorEventListener;
    //private Rotation3DLayout mRotation3DLayout;
    private SensorManager mSensorManager;
    private boolean mViewAttached;
    private ViewStub mViewStubGradientScreen;

    Double latitude, longitude, altitude;
    Geocoder geocoder;
    private static final int REQUEST_CHANGE_THEME = 1001;
    private static final int REQUEST_CHANGE_SETTING = 1002;
    private DialogRateMe mDialogRateMe;
    @Inject
    ICompassPresenter<ICompassView> mPresenter;
    private boolean doubleBackToExitPressedOnce;

    private static final int CAMERA_REQUEST = 1003;
    private boolean hasCameraFlash;
    private boolean isEnabledCamera;

    Intent locationService;

    @Override
    public int getLayoutId() {
        return R.layout.compass_activity;
    }

    @Override
    public void inject() {
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        this.initServices();
        this.initResources();
        mPresenter.init(this);
        mPresenter.loadData(this);
    }

    @Override
    public void init() {
        this.mOrientationSensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(final Sensor sensor, final int n) {
                if (!mViewAttached) {
                    return;
                }
            }

            public void onSensorChanged(final SensorEvent sensorEvent) {
                if (mViewAttached) {
                    final float[] values = sensorEvent.values;
                    mPresenter.updateCompass(values);
                }
            }
        };

        this.mPressureSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                final float n = sensorEvent.values[0];
                mPresenter.updatePressure(n);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                if (i != 2 && i != 3) {
                    mPresenter.updatePressure(-404.0f);
                }
            }

        };

        geocoder = new Geocoder(this, Locale.getDefault());
        mDialogRateMe = new DialogRateMe(this, R.style.ThemeDialog);
        mDialogRateMe.setCallBack(this);
        hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        isEnabledCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    protected void setUp() {
        checkPermissionAndStart();
    }

    private void startLocationService() {
        locationService = new Intent(getApplicationContext(), GoogleService.class);
        startService(locationService);
    }

    private void checkPermissionAndStart() {
        if (this.isPermissionGranted("android.permission.ACCESS_FINE_LOCATION")) {
            Log.d("Compass:CompassActivity", "checkPermissionAndStart startCompass");
            startLocationService();
            return;
        }
        Log.d("Compass:CompassActivity", "checkPermissionAndStart request permission");
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
    }

    private void initGradientScreenViewStub() {
        if (this.mGradienterScreen == null) {
            this.mViewStubGradientScreen.inflate();
            this.mGradienterScreen = this.findViewById(R.id.gradienter_screen);
        }
    }

    private void initResources() {
        this.mDirection = 0.0f;
        this.mCompassScreen = this.findViewById(R.id.compass_screen);
        this.mViewStubGradientScreen = this.findViewById(R.id.view_stub_gradient_screen);
        //this.mRotation3DLayout = this.findViewById(R.id.compass_layout_lying);
        initGradientScreenViewStub();
    }

    private void initServices() {
        this.mSensorManager = (SensorManager) this.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        if (Utils.isAndroidO()) {
            this.mOrientationSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        } else {
            this.mOrientationSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        this.mMagenaticSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.mPressureSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    }

    private boolean isPermissionGranted(final String s) {
        return ContextCompat.checkSelfPermission(this, s) == 0;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mViewAttached = true;
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }

    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
        if (locationService != null) {
            stopService(locationService);
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mViewAttached = false;
    }

    protected void onPause() {
        super.onPause();
        unregister();
    }

    public void onRequestPermissionsResult(int i, final String[] array, final int[] array2) {
        super.onRequestPermissionsResult(i, array, array2);
        switch (i) {
            case 1: {
                if (i == array2.length && i != 0) {
                    Log.d("Compass:CompassActivity", "onRequestPermissionsResult startCompass");
                    startLocationService();
                    return;
                }
                break;
            }
            case CAMERA_REQUEST: {
                if (array2.length > 0 && array2[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.handleFlash(this);
                }
                break;
            }
        }
    }

    protected void onResume() {
        super.onResume();
        register();
        mPresenter.resume();
    }

    private void register() {
        if (this.mOrientationSensor != null) {
            Log.i("Compass:CompassActivity", "register OrientationSensor :" + this.mSensorManager.registerListener(this.mOrientationSensorEventListener, this.mOrientationSensor, 1));

        } else {
            Log.e("Compass:CompassActivity", "OrientationSensor is null");
        }

        if (this.mMagenaticSensor != null) {
            Log.i("Compass:CompassActivity", "register OrientationSensor :" + this.mSensorManager.registerListener(this, this.mMagenaticSensor, SensorManager.SENSOR_DELAY_NORMAL));
        }

        if (this.mPressureSensor != null) {
            Log.i("Compass:CompassActivity", "register OrientationSensor :" + this.mSensorManager.registerListener(this.mPressureSensorEventListener, this.mPressureSensor, SensorManager.SENSOR_DELAY_NORMAL));
        }

        //this.mRotation3DLayout.startGravityDetection();
        registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
    }

    private void unregister() {
        if (this.mOrientationSensor != null) {
            this.mSensorManager.unregisterListener(this.mOrientationSensorEventListener);
        }
        if (this.mMagenaticSensor != null) {
            this.mSensorManager.unregisterListener(this);
        }
        if (this.mPressureSensor != null) {
            this.mSensorManager.unregisterListener(this.mPressureSensorEventListener);
        }
        //this.mRotation3DLayout.stopGravityDetection();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mPresenter.onMagenaticChange(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            latitude = intent.getDoubleExtra("latutide", 0.0);
            longitude = intent.getDoubleExtra("longitude", 0.0);
            altitude = intent.getDoubleExtra("altitude", 0.0);

            mPresenter.updateLatLon(latitude, longitude, null, altitude);
            List<Address> addresses = null;
            try {
                if (addresses != null && addresses.size() > 0) {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String cityName = addresses.get(0).getAddressLine(0);
                    mPresenter.updateLatLon(latitude, longitude, cityName, altitude);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    };

    @OnClick({R.id.action_theme, R.id.action_setting, R.id.action_share, R.id.action_flash, R.id.action_rating})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_theme:
                startActivityForResult(new Intent(this, ThemeActivity.class), REQUEST_CHANGE_THEME);
                break;
            case R.id.action_setting:
                startActivityForResult(new Intent(this, SetttingActivity.class), REQUEST_CHANGE_SETTING);
                break;
            case R.id.action_share:
                shareMe();
                break;
            case R.id.action_flash:
                if (hasCameraFlash) {
                    if (isEnabledCamera) {
                        mPresenter.handleFlash(this);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                    }
                    Log.d("ImoLog", "state is: " + view.isSelected());
                    view.setSelected(!view.isSelected());
                }
                break;
            case R.id.action_rating:
                mDialogRateMe.show(false);
                break;
        }
    }

    private void shareMe() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    @Override
    public void updateCompassView(float mDirection, float n, float n2) {
        mCompassScreen.setCompassDirection(mDirection);
        mGradienterScreen.setGradienterDirection(n, n2);
        updateDirection(mDirection);
    }

    @Override
    public void updateLatLon(double latitude, double longitude, String cityName) {
        if (cityName != null && !TextUtils.isEmpty(cityName)) {
            mAddressView.setText(cityName);
            mAddressView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMagenatic(double tesla) {
        String mage = String.format(getString(R.string.magenatic_unit), tesla);
        magenaticIndex.setText(mage);
    }

    @Override
    public void showTheme(ThemeModel currentTheme) {
        int windowColor = CommonUtils.parserColor(currentTheme.getWindowBgColor());
        int textColor = CommonUtils.parserColor(currentTheme.getTextColor());

        mCompassScreen.setTheme(currentTheme);
        rootView.setBackgroundColor(windowColor);

        magenaticIndex.setTextColor(textColor);
        mAddressView.setTextColor(textColor);

        mMagenaticLabel.setTextColor(textColor);
        pressure.setTextColor(textColor);
        pressureTitle.setTextColor(textColor);
        altitudeTitle.setTextColor(textColor);
        altitudeIndex.setTextColor(textColor);

        mDirectionTextViewLying.setTextColor(textColor);
        mAngleLayoutLying.setTextColor(textColor);

        mDegreeLying.setImageResource(CommonUtils.getDrawableIDFromName(this, currentTheme.getDegreeImage()));
        actionMap.setImageResource(CommonUtils.getDrawableIDFromName(this, currentTheme.getIcon_location()));
        actionFlash.setImageResource(CommonUtils.getDrawableIDFromName(this, currentTheme.getIconFlash()));
        actionRating.setImageResource(CommonUtils.getDrawableIDFromName(this, currentTheme.getIconRate()));
        actionSetting.setImageResource(CommonUtils.getDrawableIDFromName(this, currentTheme.getIconSetting()));
        actionShare.setImageResource(CommonUtils.getDrawableIDFromName(this, currentTheme.getIconShare()));
        actionTheme.setImageResource(CommonUtils.getDrawableIDFromName(this, currentTheme.getIconTheme()));

        mGradienterScreen.setTheme(currentTheme);
    }

    @Override
    public void exitAppNow() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void showAskDialog() {
        mDialogRateMe.show(true);
    }

    @Override
    public void noRate() {
        finish();
    }

    @Override
    public void rate() {
        rateMe();
        finish();
    }

    @Override
    public LinearLayout getLayoutMega() {
        return layoutMega;
    }

    @Override
    public LinearLayout getLayoutPressure() {
        return layoutPress;
    }

    @Override
    public LinearLayout getLayoutAlitude() {
        return layoutAltitude;
    }

    @Override
    public TextView getAddressView() {
        return mAddressView;
    }

    @Override
    public LinearLayout getActionMap() {
        return layoutMap;
    }

    @Override
    public void gotoMapView(double lat, double lon, String addr) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(AppConstants.LOCATION_LAN, lat);
        intent.putExtra(AppConstants.LOCATION_LONG, lon);
        intent.putExtra(AppConstants.ADDRESS_EXTRA, addr);
        startActivity(intent);
    }

    @Override
    public void showOrHideAd(int i) {
        adsRoot.setVisibility(i);
    }

    @Override
    public void showPressure(String pressure, float value) {
        this.pressure.setText(pressure);
        this.mCompassScreen.setPressure(value);
    }

    @Override
    public void updateAltitude(float altitude) {
        //Log.e("ALtutide", "Al: " + altitude);
        this.mCompassScreen.setAltitude(altitude);
        this.altitudeIndex.setText(String.format(getString(R.string.altitude), altitude));
    }

    @Override
    public void loadBannerAds() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHANGE_THEME) {
                mPresenter.changeTheme();
            }
            if (requestCode == REQUEST_CHANGE_SETTING) {
                mPresenter.changeSetting();
            }
        }
    }

    @Override
    public void onBackPressed() {
        mPresenter.handleExitApp();
    }

    @Override
    public void onNoRate(boolean dontAgain, boolean onExit) {
        mPresenter.noRate(dontAgain, onExit);
    }

    @Override
    public void onRate(boolean dontAgain, boolean onExit) {
        mPresenter.rate(dontAgain, onExit);
    }

    private void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    @OnClick(R.id.address)
    public void onGotoMap() {
        mPresenter.gotoMap();
    }

    @OnClick(R.id.action_map)
    public void onViewClicked() {
        mPresenter.gotoMap();
    }

    private float normalizeDegree(final float n) {
        return (720.0f + n) % 360.0f;
    }

    private void setTextToTextView(final TextView textView, final String text) {
        if (text == null || !text.equals(textView.getText())) {
            textView.setText((CharSequence) text);
        }
    }

    @SuppressLint("WrongConstant")
    private void updateDirection(float normalizeDegree) {
        final Context context = this;
        normalizeDegree = this.normalizeDegree(-1.0f * normalizeDegree);
        Locale.getDefault().getLanguage();
        String s;
        if (normalizeDegree > 22.5f && normalizeDegree < 75.5f) {
            s = context.getString(R.string.direction_north_east);
        } else if (normalizeDegree > 75.5f && normalizeDegree < 112.5f) {
            s = context.getString(R.string.direction_east);
        } else if (normalizeDegree > 112.5f && normalizeDegree < 157.5f) {
            s = context.getString(R.string.direction_south_east);
        } else if (normalizeDegree > 157.5f && normalizeDegree < 202.5f) {
            s = context.getString(R.string.direction_south);
        } else if (normalizeDegree > 202.5f && normalizeDegree < 247.5f) {
            s = context.getString(R.string.direction_south_west);
        } else if (normalizeDegree > 247.5f && normalizeDegree < 292.5f) {
            s = context.getString(R.string.direction_west);
        } else if (normalizeDegree > 292.5f && normalizeDegree < 337.5f) {
            s = context.getString(R.string.direction_north_west);
        } else {
            s = context.getString(R.string.direction_north);
        }

        this.setTextToTextView(this.mDirectionTextViewLying, s);
        this.setTextToTextView(this.mAngleLayoutLying, Utils.formatToArabicNums(this, R.string.angle, normalizeDegree));

    }
}
