package com.core.compass.ui.custom;

import android.content.DialogInterface;

import java.io.IOException;

import android.os.AsyncTask;
import android.hardware.Camera.Size;
import java.util.Iterator;
import android.util.Log;
import java.util.List;
import android.util.AttributeSet;
import android.content.Context;
import android.view.SurfaceHolder;
import android.app.AlertDialog;
import android.hardware.Camera;
import android.view.SurfaceView;

import com.core.ssvapp.R;

/**
 * Package: com.core.ssvapp.ui.custom
 * Created by: CuongCK
 * Date: 2/28/18
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera mCamera;
    private AlertDialog mCameraUnAvailableDialog;
    private SurfaceHolder mHolder;
    private boolean mIsRunningPreview;

    public CameraView(final Context context) {
        this(context, null);
    }

    public CameraView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public CameraView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        (this.mHolder = this.getHolder()).addCallback((SurfaceHolder.Callback)this);
        this.mHolder.setType(3);
    }

    private boolean checkRateRange(final int n, final int n2) {
        return n <= 30000 && n2 >= 30000;
    }

    private int[] getOptimalPreviewFpsRange(final List<int[]> list) {
        if (list == null || list.isEmpty()) {
            Log.e("Compass:CameraView", "No supported frame rates returned!");
            return null;
        }
        int n = 400000;
        int n2 = 0;
        int n3 = -1;
        int n4 = 0;
        for (final int[] array : list) {
            final int n5 = array[0];
            final int n6 = array[1];
            int n7 = n2;
            int n8 = n;
            int n9 = n3;
            Label_0152: {
                if (this.checkRateRange(n5, n6)) {
                    if (n5 >= n) {
                        n7 = n2;
                        n8 = n;
                        n9 = n3;
                        if (n5 != n) {
                            break Label_0152;
                        }
                        n7 = n2;
                        n8 = n;
                        n9 = n3;
                        if (n6 <= n2) {
                            break Label_0152;
                        }
                    }
                    n8 = n5;
                    n9 = n4;
                    n7 = n6;
                }
            }
            ++n4;
            n2 = n7;
            n = n8;
            n3 = n9;
        }
        if (n3 >= 0) {
            return list.get(n3);
        }
        Log.e("Compass:CameraView", "Can't find an appropriate frame rate range!");
        return null;
    }

    private Camera.Size getOptimalPreviewSize(final List<Camera.Size> list, final int n, final int n2) {
        Camera.Size camera$Size;
        if (n2 == 0 || list == null) {
            camera$Size = null;
        }
        else {
            final double n3 = n / n2;
            final Camera.Size camera$Size2 = null;
            double abs = Double.MAX_VALUE;
            final Iterator<Camera.Size> iterator = list.iterator();
            Camera.Size camera$Size3 = camera$Size2;
            while (true) {
                camera$Size = camera$Size3;
                if (!iterator.hasNext()) {
                    break;
                }
                final Camera.Size camera$Size4 = iterator.next();
                double n4;
                if ((n > n2 && camera$Size4.width > camera$Size4.height) || (n < n2 && camera$Size4.width < camera$Size4.height)) {
                    n4 = camera$Size4.width / camera$Size4.height;
                }
                else {
                    n4 = camera$Size4.height / camera$Size4.width;
                }
                final double abs2 = Math.abs(n3 - n4);
                if (camera$Size3 == null || abs - abs2 > 1.0E-6) {
                    camera$Size3 = camera$Size4;
                    abs = Math.abs(n3 - n4);
                }
                else {
                    if (abs2 - abs >= 1.0E-6 || abs - abs2 >= 1.0E-6 || (camera$Size3.width >= camera$Size4.width && camera$Size3.height >= camera$Size4.height)) {
                        continue;
                    }
                    camera$Size3 = camera$Size4;
                }
            }
        }
        return camera$Size;
    }

    private void initParameters(final int n, final int n2) {
        while (true) {
            while (true) {
                Camera.Size camera$Size2 = null;
                Label_0360: {
                    try {
                        final Camera.Parameters parameters = this.mCamera.getParameters();
                        final List supportedPictureSizes = parameters.getSupportedPictureSizes();
                        if (supportedPictureSizes != null && !supportedPictureSizes.isEmpty()) {
                            final Camera.Size camera$Size = (Size) supportedPictureSizes.get(0);
                            final Iterator<Camera.Size> iterator = supportedPictureSizes.iterator();
                            while (iterator.hasNext()) {
                                camera$Size2 = iterator.next();
                                if (camera$Size.width > camera$Size2.width) {
                                    break Label_0360;
                                }
                                if (camera$Size.width == camera$Size2.width && camera$Size.height > camera$Size2.height) {
                                    break Label_0360;
                                }
                            }
                            parameters.setPictureSize(camera$Size.width, camera$Size.height);
                            Log.i("Compass:CameraView", "set parameter picture size width:" + camera$Size.width + ",height:" + camera$Size.height);
                        }
                        final List supportedPreviewFpsRange = parameters.getSupportedPreviewFpsRange();
                        if (supportedPreviewFpsRange != null && supportedPreviewFpsRange.size() > 1) {
                            final int[] optimalPreviewFpsRange = this.getOptimalPreviewFpsRange(supportedPreviewFpsRange);
                            if (optimalPreviewFpsRange != null && optimalPreviewFpsRange.length > 1) {
                                parameters.setPreviewFpsRange(optimalPreviewFpsRange[0], optimalPreviewFpsRange[1]);
                                Log.v("Compass:CameraView", "minFpsRange:" + optimalPreviewFpsRange[0] + ", maxFpsRange:" + optimalPreviewFpsRange[1]);
                            }
                        }
                        final Camera.Size optimalPreviewSize = this.getOptimalPreviewSize(this.mCamera.getParameters().getSupportedPreviewSizes(), n, n2);
                        if (optimalPreviewSize != null) {
                            parameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
                            Log.i("Compass:CameraView", "set parameter preview size width:" + optimalPreviewSize.width + ",height:" + optimalPreviewSize.height);
                        }
                        parameters.setFocusMode("continuous-picture");
                        parameters.setAntibanding("50hz");
                        this.mCamera.setParameters(parameters);
                        return;
                    }
                    catch (Exception ex) {
                        Log.e("Compass:CameraView", "init parameters of camera failed", (Throwable)ex);
                        return;
                    }
                }
                final Camera.Size camera$Size = camera$Size2;
                continue;
            }
        }
    }

    private void setCameraDisplayOrientation(int displayOrientation) {
        final int n = 0;
        final Camera.CameraInfo camera$CameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(0, camera$CameraInfo);
        switch (displayOrientation) {
            default: {
                displayOrientation = n;
                break;
            }
            case 0: {
                displayOrientation = 0;
                break;
            }
            case 1: {
                displayOrientation = 90;
                break;
            }
            case 2: {
                displayOrientation = 180;
                break;
            }
            case 3: {
                displayOrientation = 270;
                break;
            }
        }
        displayOrientation = (camera$CameraInfo.orientation - displayOrientation + 360) % 360;
        Log.i("Compass:CameraView", "set camera display orientation:" + displayOrientation);
        this.mCamera.setDisplayOrientation(displayOrientation);
    }

    private void startPreview(final int n, final int n2) {
        if (this.mCamera == null || this.mIsRunningPreview) {
            return;
        }
        this.initParameters(n, n2);
        try {
            this.mCamera.startPreview();
            this.mIsRunningPreview = true;
            Log.i("Compass:CameraView", "startPreview");
        }
        catch (Exception ex) {
            Log.e("Compass:CameraView", "fail to start preview", (Throwable)ex);
        }
    }

    private void stopPreview() {
        this.shouldShowCameraUnavailableDialog(false);
        if (this.mIsRunningPreview) {
            synchronized (this) {
                if (this.mCamera != null) {
                    this.mCamera.stopPreview();
                    this.mCamera.release();
                    this.mHolder.removeCallback(this);
                    Log.i("Compass:CameraView", "stopPreview");
                }
                this.mCamera = null;
                this.mIsRunningPreview = false;
            }
        }
    }

    public void closeCamera() {
        this.stopPreview();
    }

    public void openCamera() {
        new AsyncTask<Void, Void, Boolean>() {
            protected Boolean doInBackground(Void... this$0) {
               return false;
            }

            protected void onPostExecute(final Boolean b) {
                CameraView.this.startPreview(CameraView.this.getWidth(), CameraView.this.getHeight());
                CameraView.this.requestLayout();
                if (!b) {
                    CameraView.this.shouldShowCameraUnavailableDialog(true);
                }
            }
        }.execute((Void[]) new Void[0]);
    }

    public void shouldShowCameraUnavailableDialog(final boolean b) {
        if (b) {
            if (this.mIsRunningPreview && (this.mCameraUnAvailableDialog == null || !this.mCameraUnAvailableDialog.isShowing())) {
                final AlertDialog.Builder alertDialog$Builder = new AlertDialog.Builder(this.getContext());
                alertDialog$Builder.setTitle(R.string.error_camera_unavailable_title);
                alertDialog$Builder.setMessage(R.string.error_camera_unavailable);
                alertDialog$Builder.setPositiveButton(R.string.using_camera_confirm_cancel, (DialogInterface.OnClickListener)null);
                (this.mCameraUnAvailableDialog = alertDialog$Builder.create()).show();
            }
            return;
        }
        if (this.mCameraUnAvailableDialog != null && this.mCameraUnAvailableDialog.isShowing()) {
            this.mCameraUnAvailableDialog.dismiss();
        }
        this.mCameraUnAvailableDialog = null;
    }

    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
        Log.i("Compass:CameraView", "surfaceChanged width:" + n2 + ",height:" + n3);
        if (this.mCamera != null) {
            this.startPreview(n2, n3);
        }
    }

    public void surfaceCreated(final SurfaceHolder previewDisplay) {
        Log.i("Compass:CameraView", "surfaceCreated");
        try {
            if (this.mCamera != null) {
                this.mCamera.setPreviewDisplay(previewDisplay);
            }
        }
        catch (IOException ex) {
            Log.e("Compass:CameraView", "IOException caused by setPreviewDisplay()", (Throwable)ex);
        }
    }

    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        Log.i("Compass:CameraView", "surfaceDestroyed");
        if (this.mCamera != null) {
            this.stopPreview();
        }
    }
}
