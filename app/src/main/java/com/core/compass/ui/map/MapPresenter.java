package com.core.compass.ui.map;

import com.core.compass.data.DataManager;
import com.core.compass.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Package: com.core.ssvapp.ui.map
 * Created by: CuongCK
 * Date: 3/13/18
 */

public class MapPresenter<V extends IMapView> extends BasePresenter<V> implements IMapPresenter<V> {
    @Inject
    public MapPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void init() {
        loadBannerAds();
    }

    private void loadBannerAds() {
        if (!getDataManager().isProVersion()) {
            getMvpView().loadBannerAds();
        } else {
            getMvpView().hideBannerAds();
        }
    }

}
