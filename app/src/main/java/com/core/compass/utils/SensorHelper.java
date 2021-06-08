package com.core.compass.utils;

/**
 * Package: com.core.ssvapp.utils
 * Created by: CuongCK
 * Date: 2/28/18
 */

public class SensorHelper {
    private static float MIN_FLT_TO_AVOID_SINGULARITY;

    static {
        SensorHelper.MIN_FLT_TO_AVOID_SINGULARITY = 1.0E-4f;
    }

    public static void quatToRotMat(final float[] array, final float[] array2) {
        final float n = array2[0];
        final float n2 = array2[1];
        final float n3 = array2[2];
        final float n4 = array2[3];
        final float n5 = n * n;
        final float n6 = n * n2;
        final float n7 = n * n3;
        final float n8 = n * n4;
        final float n9 = n2 * n2;
        final float n10 = n2 * n3;
        final float n11 = n2 * n4;
        final float n12 = n3 * n3;
        final float n13 = n3 * n4;
        array[0] = 1.0f - 2.0f * (n9 + n12);
        array[1] = 2.0f * (n6 - n13);
        array[2] = 2.0f * (n7 + n11);
        array[3] = 2.0f * (n6 + n13);
        array[4] = 1.0f - 2.0f * (n5 + n12);
        array[5] = 2.0f * (n10 - n8);
        array[6] = 2.0f * (n7 - n11);
        array[7] = 2.0f * (n10 + n8);
        array[8] = 1.0f - 2.0f * (n5 + n9);
    }

    public static void rotMatToOrient(final float[] array, final float[] array2) {
        final float[] array3 = { array2[0], array2[3], array2[6] };
        final float n = array2[1];
        final float n2 = array2[4];
        final float n3 = array2[7];
        final float[] array4 = { array2[2], array2[5], array2[8] };
        final float n4 = (float)Math.sqrt(array3[0] * array3[0] + array3[1] * array3[1]);
        if (Math.abs(array4[2]) < SensorHelper.MIN_FLT_TO_AVOID_SINGULARITY) {
            final float min_FLT_TO_AVOID_SINGULARITY = SensorHelper.MIN_FLT_TO_AVOID_SINGULARITY;
            int n5;
            if (array4[2] < 0.0f) {
                n5 = -1;
            }
            else {
                n5 = 1;
            }
            array4[2] = n5 * min_FLT_TO_AVOID_SINGULARITY;
        }
        if (Math.abs(array3[0]) < SensorHelper.MIN_FLT_TO_AVOID_SINGULARITY) {
            final float min_FLT_TO_AVOID_SINGULARITY2 = SensorHelper.MIN_FLT_TO_AVOID_SINGULARITY;
            int n6;
            if (array3[0] < 0.0f) {
                n6 = -1;
            }
            else {
                n6 = 1;
            }
            array3[0] = n6 * min_FLT_TO_AVOID_SINGULARITY2;
        }
        float n7 = n4;
        if (Math.abs(n4) < SensorHelper.MIN_FLT_TO_AVOID_SINGULARITY) {
            final float min_FLT_TO_AVOID_SINGULARITY3 = SensorHelper.MIN_FLT_TO_AVOID_SINGULARITY;
            int n8;
            if (n4 < 0.0f) {
                n8 = -1;
            }
            else {
                n8 = 1;
            }
            n7 = min_FLT_TO_AVOID_SINGULARITY3 * n8;
        }
        array[0] = (float)(57.295780181884766 * Math.atan2(array3[1], array3[0]));
        array[0] = 360.0f - array[0] % 360.0f;
        array[1] = (float)(-57.295780181884766 * Math.atan2((new float[] { n, n2, n3 })[2], array4[2]));
        array[2] = (float)(57.295780181884766 * Math.atan2(array3[2], n7));
    }
}
