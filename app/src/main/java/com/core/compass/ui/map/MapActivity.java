package com.core.compass.ui.map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.core.compass.ui.base.BaseActivity;
import com.core.compass.utils.AppConstants;
import com.core.compass.utils.Utils;
import com.core.ssvapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Package: com.core.ssvapp.ui.map
 * Created by: CuongCK
 * Date: 3/13/18
 */

public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, IMapView {
    @BindView(R.id.screen_title)
    TextView screenTitle;
    @Inject
    IMapPresenter<IMapView> mPresenter;
    @BindView(R.id.ads_root)
    FrameLayout adsRoot;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.lat)
    TextView lat;
    @BindView(R.id.lon)
    TextView lon;

    private GoogleMap mMap;

    private double mLat, mLon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public void inject() {
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        mPresenter.init();

    }

    @Override
    public void init() {
        screenTitle.setText(R.string.location);
        mLat = getIntent().getDoubleExtra(AppConstants.LOCATION_LAN, 0);
        mLon = getIntent().getDoubleExtra(AppConstants.LOCATION_LONG, 0);
        lat.setText(Utils.getLocationString(mLat));
        lon.setText(Utils.getLocationString(mLon));
        String addressStr = getIntent().getStringExtra(AppConstants.ADDRESS_EXTRA);
        if (!TextUtils.isEmpty(addressStr)) {
            address.setText(addressStr);
            address.setVisibility(View.VISIBLE);
        } else {
            address.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setUp() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @OnClick(R.id.arrow)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraMoveListener(this);
        LatLng current = new LatLng(mLat, mLon);
        mMap.addMarker(new MarkerOptions().position(current).title(getString(R.string.you_are_here)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLon), 15);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void loadBannerAds() {
    }

    @Override
    public void hideBannerAds() {
        adsRoot.setVisibility(View.GONE);
    }
}
