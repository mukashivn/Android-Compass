package com.core.compass.data.realm;

import com.core.compass.data.model.ThemeModel;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Package: com.core.ssvapp.data.realm
 * Created by: CuongCK
 * Date: 3/13/18
 */

public interface IRealmHelper {
    Realm getRealm();

    void insertTheme(ThemeModel model);

    ThemeModel getDefaultTheme();

    ArrayList<ThemeModel> getAllTheme();

    void setThemeDefault(int id);

    void setThemePayment(int id);

    void insertThemeList(ArrayList<ThemeModel> themeModels);

    ThemeModel getThemeById(int id);
}
