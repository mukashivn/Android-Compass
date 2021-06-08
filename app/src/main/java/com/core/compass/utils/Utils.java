package com.core.compass.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.core.compass.ApplicationImpl;
import com.core.ssvapp.R;

import java.util.HashMap;
import java.util.Locale;

/**
 * Package: com.core.ssvapp.utils
 * Created by: CuongCK
 * Date: 2/27/18
 */

public class Utils {
    private static final String[] LANGUAGE_LIST;
    private static final HashMap<String, Typeface> sTypefaceMap;

    static {
        sTypefaceMap = new HashMap<String, Typeface>();
        LANGUAGE_LIST = ApplicationImpl.getAppContext().getResources().getStringArray(R.array.compass_locale_language);
    }

    public static double calculateSLP(final double n, final double n2) {
        return n2 / Math.pow(1.0 - n / 44330.0, 5.255);
    }

    public static void dismissDialog(final AlertDialog alertDialog) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public static String formatToArabicNums(final Context context, final int n, final float n2) {
        if (Utils.LANGUAGE_LIST != null && Utils.LANGUAGE_LIST.length > 0) {
            final String language = Locale.getDefault().getLanguage();
            final String[] language_LIST = Utils.LANGUAGE_LIST;
            for (int length = language_LIST.length, i = 0; i < length; ++i) {
                if (TextUtils.equals((CharSequence)language_LIST[i], (CharSequence)language)) {
                    return context.getString(n, new Object[] { n2 });
                }
            }
        }
        return String.format(Locale.US, context.getString(n), n2);
    }

    public static String getLocationString(final double n) {
        final int n2 = (int)n;
        final int n3 = (int)((n - n2) * 3600.0) / 60;
        final int n4 = (int)((n - n2) * 3600.0) % 60;
        if (Utils.LANGUAGE_LIST != null && Utils.LANGUAGE_LIST.length > 0) {
            final String language = Locale.getDefault().getLanguage();
            final String[] language_LIST = Utils.LANGUAGE_LIST;
            for (int length = language_LIST.length, i = 0; i < length; ++i) {
                if (TextUtils.equals((CharSequence)language_LIST[i], (CharSequence)language)) {
                    return ApplicationImpl.getAppContext().getString(R.string.altitude_longitude_Degree_format, new Object[] { n2, n3, n4 });
                }
            }
        }
        return String.format(Locale.US, ApplicationImpl.getAppContext().getString(R.string.altitude_longitude_Degree_format), n2, n3, n4);
    }

    public static final Typeface getTypeface(final AssetManager assetManager, final String s) {
        Typeface fromAsset;
        if ((fromAsset = Utils.sTypefaceMap.get(s)) == null) {
            fromAsset = Typeface.createFromAsset(assetManager, s);
            Utils.sTypefaceMap.put(s, fromAsset);
        }
        return fromAsset;
    }

    public static boolean isAndroidO() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean isNetworkAvailable(final ConnectivityManager connectivityManager) {
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }

    public static boolean isRTL() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    public static boolean isWifi(final Context context) {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)context.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }

    public static float normalizeDegree(final float n) {
        return (720.0f + n) % 360.0f;
    }
}
