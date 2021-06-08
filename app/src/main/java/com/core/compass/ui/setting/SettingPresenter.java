package com.core.compass.ui.setting;

import android.view.View;

import com.core.compass.data.DataManager;
import com.core.compass.ui.base.BaseActivity;
import com.core.compass.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Package: com.core.ssvapp.ui.setting
 * Created by: CuongCK
 * Date: 3/12/18
 */

public class SettingPresenter<V extends ISettingView> extends BasePresenter<V> implements ISettingPresenter<V> {

    private BaseActivity mContext;

    @Inject
    public SettingPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void finish() {
        getDataManager().setShowMagenatic(getMvpView().getMagenaticView().isChecked());
        getDataManager().setKeepOnScreen(getMvpView().getKeepOnView().isChecked());
        getMvpView().complete();
    }

    @Override
    public void init(BaseActivity context) {
        mContext = context;
        getMvpView().getMagenaticView().setChecked(getDataManager().getShowMagenatic());
        getMvpView().getKeepOnView().setChecked(getDataManager().getKeepOnScreen());
        getMvpView().showOrHideProView(getDataManager().isProVersion() ? View.GONE : View.VISIBLE);

    }

    @Override
    public void updatePro() {

    }

    @Override
    public void buyBeer() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
