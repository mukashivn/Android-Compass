package com.core.compass.utils;

import android.os.Build;
import android.text.TextUtils;

import com.core.compass.ApplicationImpl;

import java.util.HashMap;

/**
 * Package: com.core.ssvapp.utils
 * Created by: CuongCK
 * Date: 2/28/18
 */

public class CompassDeviceUtils {
    private static final String DEVICE;
    private static final HashMap<String, Boolean> sBooleanConfig;

    static {
        DEVICE = Build.DEVICE;
        sBooleanConfig = new HashMap<String, Boolean>();
    }

    private static boolean getBooleanValue(int n, final String s) {
        final Boolean b = CompassDeviceUtils.sBooleanConfig.get(s);
        if (b != null) {
            return b;
        }
        final boolean b2 = false;
        final String[] stringArray = ApplicationImpl.getAppContext().getResources().getStringArray(n);
        boolean b3 = b2;
        if (stringArray != null) {
            final int length = stringArray.length;
            n = 0;
            while (true) {
                b3 = b2;
                if (n >= length) {
                    break;
                }
                if (TextUtils.equals((CharSequence)CompassDeviceUtils.DEVICE, (CharSequence)stringArray[n])) {
                    CompassDeviceUtils.sBooleanConfig.put(s, true);
                    b3 = true;
                    break;
                }
                ++n;
            }
        }
        CompassDeviceUtils.sBooleanConfig.put(s, b3);
        return b3;
    }

    public static boolean isSupportCalibration() {
        return getBooleanValue(2131165185, "support_calibrate");
    }

    public static boolean isSupportNmeaAlt() {
        return getBooleanValue(2131165187, "support_mi_nmea");
    }

    public static boolean isSupportRotateCalibration() {
        return getBooleanValue(2131165186, "support_rotate_calibrate");
    }
}
