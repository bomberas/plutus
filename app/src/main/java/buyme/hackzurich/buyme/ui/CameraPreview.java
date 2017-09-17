package buyme.hackzurich.buyme.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera.Size;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback  {

    public static String TAG = CameraPreview.class.getSimpleName();

    private Context context;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.context = context;
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
//        if ( mCamera != null ) {
//            mCamera.setPreviewCallback(null);
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = null;
//        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if ( mHolder.getSurface() == null ) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e){
            Log.e(TAG, e.getLocalizedMessage());
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            //mCamera.setPreviewCallback(this);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

//    /***
//     * Called by OS as preview frames are displayed (this callback is set by requestCalibration)
//     * When requested here comes the Camera Frame.
//     */
//    @Override
//    public void onPreviewFrame(final byte[] data, final Camera camera) {
//        Log.d(TAG, "bla bla");
//    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}