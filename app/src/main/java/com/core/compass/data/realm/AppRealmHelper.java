package com.core.compass.data.realm;

import android.app.Application;
import android.content.Context;

import com.core.compass.data.entity.Theme;
import com.core.compass.data.model.ThemeModel;
import com.core.compass.ui.base.BaseActivity;
import com.core.compass.ui.base.BaseFragment;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Package: com.core.ssvapp.data.realm
 * Created by: CuongCK
 * Date: 3/13/18
 */

public class AppRealmHelper implements IRealmHelper {
    private final Realm realm;
    private static AppRealmHelper instance;

    public AppRealmHelper(Application application) {
        Realm.init(application);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
    }

    public AppRealmHelper(Context application) {
        Realm.init(application);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
    }

    public static AppRealmHelper with(BaseFragment fragment) {

        if (instance == null) {
            instance = new AppRealmHelper(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static AppRealmHelper with(BaseActivity activity) {

        if (instance == null) {
            instance = new AppRealmHelper(activity.getApplication());
        }
        return instance;
    }

    public static AppRealmHelper with(Application application) {
        if (instance == null) {
            instance = new AppRealmHelper(application);
        }
        return instance;
    }

    public static AppRealmHelper with(Context context) {
        if (instance == null) {
            instance = new AppRealmHelper(context);
        }

        return instance;
    }

    public static AppRealmHelper getInstance() {
        return instance;
    }

    public static AppRealmHelper withService(Context context) {
        return new AppRealmHelper(context);
    }

    @Override
    public Realm getRealm() {
        return realm;
    }

    @Override
    public void insertTheme(ThemeModel model) {
        realm.executeTransaction(realm1 -> {
            Theme theme = new Theme(model);
            realm1.copyToRealmOrUpdate(theme);
        });
    }

    @Override
    public ThemeModel getDefaultTheme() {
        Theme theme = realm.where(Theme.class).equalTo("_default", true).findFirst();
        if (theme != null)
            return realm.where(Theme.class).equalTo("_default", true).findFirst().map2Model();
        return null;
    }

    @Override
    public ArrayList<ThemeModel> getAllTheme() {
        ArrayList<ThemeModel> themeModels = new ArrayList<>();
        RealmResults<Theme> realmResults = realm.where(Theme.class).findAll();
        for (Theme item : realmResults
                ) {
            themeModels.add(item.map2Model());
        }
        return themeModels;
    }

    @Override
    public void setThemeDefault(int id) {
        Theme theme = getThemeId(id);
        RealmResults<Theme> results = getAllThemeR();

        realm.executeTransaction(realm1 -> {
            for (Theme item : results
                    ) {
                item.setDefault(false);
                realm1.copyToRealmOrUpdate(item);
            }
            theme.setDefault(true);
            realm1.copyToRealmOrUpdate(theme);
        });
    }

    @Override
    public void setThemePayment(int id) {
        Theme theme = getThemeId(id);
        realm.executeTransaction(realm1 -> {
            theme.setPayment(true);
            realm1.copyToRealmOrUpdate(theme);
        });
    }

    @Override
    public void insertThemeList(ArrayList<ThemeModel> themeModels) {
        realm.executeTransaction(realm1 -> {
            for (ThemeModel item : themeModels
                    ) {
                Theme obj = new Theme(item);
                realm1.copyToRealmOrUpdate(obj);
            }
        });
    }

    @Override
    public ThemeModel getThemeById(int id) {
        return realm.where(Theme.class).equalTo("id", id).findFirst().map2Model();
    }

    private Theme getThemeId(int id) {
        return realm.where(Theme.class).equalTo("id", id).findFirst();
    }

    private RealmResults<Theme> getAllThemeR() {
        return realm.where(Theme.class).findAll();
    }
}
