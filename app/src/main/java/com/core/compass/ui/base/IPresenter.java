package com.core.compass.ui.base;

/**
 * Created by CuongCK on 2/13/17.
 */

public interface IPresenter<V extends IView> {
    void onAttach(V mvpView);

    void onDetach();

    void handleApiError(int errorCode);

    void setUserAsLoggedOut();
}
