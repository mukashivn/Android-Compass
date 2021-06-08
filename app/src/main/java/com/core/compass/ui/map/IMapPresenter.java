package com.core.compass.ui.map;

import com.core.compass.ui.base.IPresenter;
import com.core.compass.di.PerActivity;

/**
 * Package: com.core.ssvapp.ui.map
 * Created by: CuongCK
 * Date: 3/13/18
 */
@PerActivity
public interface IMapPresenter<V extends IMapView> extends IPresenter<V> {
    void init();
}
