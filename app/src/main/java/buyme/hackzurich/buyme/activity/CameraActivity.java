package buyme.hackzurich.buyme.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import buyme.hackzurich.buyme.R;
import buyme.hackzurich.buyme.domain.MultiPartStack;
import buyme.hackzurich.buyme.domain.MultiPartStringRequest;
import buyme.hackzurich.buyme.ui.CameraPreview;
import buyme.hackzurich.buyme.util.CommonUtil;
import buyme.hackzurich.buyme.util.Constant;

public class CameraActivity extends AppCompatActivity {

    public static String TAG = CameraActivity.class.getSimpleName();
    private Camera mCamera;
    private CameraPreview mPreview;
    private boolean isButtonAvailable;
    private ImageView button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        isButtonAvailable = true;
        System.out.println("adding button");

        // Add a listener to the Capture button
        button = (ImageView) findViewById(R.id.button_capture);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        // get an image from the camera

                        if (isButtonAvailable) {
                            CommonUtil.setColor(button, 0);
                            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                            mCamera.takePicture(null, null, mPicture);
                            isButtonAvailable = false;
                            Log.d(TAG, "TAKING PICTURE");
                        }
                    }
                }
        );
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                makeCall(pictureFile);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            Log.e(TAG, e.getLocalizedMessage());
        }
        return c;
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BuyMe");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("BuyMe", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if ( mCamera != null ) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void makeCall(final File file){

        RequestQueue mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, "http://www.fashwell.com/api/hackzurich/v1/posts/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                isButtonAvailable = true;
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                CommonUtil.setColor(button, 1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", Constant.FASHWELL_TOKEN);
                return params;
            }

            @Override
            public Map<String, File> getFileUploads() {
                Map<String,File> params = new HashMap<String, File>();
                params.put("image",file);
                return params;
            }

        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multiPartRequest.setRetryPolicy(policy);
        mSingleQueue.add(multiPartRequest);
    }
}
