package com.core.compass.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Package: com.core.ssvapp.data.prefs
 * Created by: CuongCK
 * Date: 2/28/18
 */

public class CompassPreferences {
    public static float getLastSLPAccuracy(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat("pref_slp_accuracy", -9999.0f);
    }

    public static long getLastSLPCalibrationTime(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong("pref_last_alt_calebration_time", 0L);
    }

    public static float getSeaLevelPressure(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat("pref_last_sea_level_pressure", 1013.25f);
    }

    public static boolean isShowFirstUsingAlert(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_show_first_using_alert", true);
    }

    public static boolean isShowMobiledataAlert(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_show_mobiledata_alert", true);
    }

    public static void saveSeaLevelPressure(final Context context, final float n, final float n2) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        defaultSharedPreferences.edit().putFloat("pref_last_sea_level_pressure", n).apply();
        defaultSharedPreferences.edit().putLong("pref_last_alt_calebration_time", System.currentTimeMillis()).apply();
        defaultSharedPreferences.edit().putFloat("pref_slp_accuracy", n2).apply();
    }

    public static void saveShowFirtUsingAlert(final Context context, final boolean b) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("pref_show_first_using_alert", b).apply();
    }

    public static void saveShowMobiledataAlert(final Context context, final boolean b) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("pref_show_mobiledata_alert", b).apply();
    }
}
