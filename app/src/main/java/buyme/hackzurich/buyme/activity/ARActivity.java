package buyme.hackzurich.buyme.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import buyme.hackzurich.buyme.R;
import buyme.hackzurich.buyme.domain.MultiPartStack;
import buyme.hackzurich.buyme.domain.MultiPartStringRequest;
import buyme.hackzurich.buyme.domain.Product;
import buyme.hackzurich.buyme.ui.CameraPreview;
import buyme.hackzurich.buyme.util.CommonUtil;
import buyme.hackzurich.buyme.util.Constant;

public class ARActivity extends AppCompatActivity implements Camera.PreviewCallback {

    public static String TAG = ARActivity.class.getSimpleName();
    private CameraPreview mPreview;
    private Camera mCamera;

    private List<Product> products = new ArrayList<>();

    private boolean isInProgress = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview_ar);
        preview.addView(mPreview);

        findViewById(R.id.loadingPanelAR).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
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
                Log.d(TAG, pictureFile.getAbsolutePath());
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
                try {
                    JSONObject result = null;
                    result = new JSONObject(response);
                    JSONArray items = result.getJSONArray("products");

                    if (items != null && items.length() > 0) {
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = (JSONObject) items.get(i);
                            JSONObject bounding_box = (JSONObject) item.getJSONObject("bounding_box");
                            JSONArray instances = item.getJSONArray("instances");

                            Product product = new Product();
                            product.setCategory(item.getString("category"));
                            product.setX(bounding_box.getString("x"));
                            product.setY(bounding_box.getString("y"));
                            product.setHeight(bounding_box.getString("height"));
                            product.setWidth(bounding_box.getString("width"));

                            if (instances != null && instances.length() > 0) {
                                JSONObject temp = (JSONObject) instances.get(0);
                                product.setSku(temp.getString("sku"));
                                product.setTitle(temp.getString("title"));
                                product.setPrice(temp.getString("price"));
                                product.setBrand_name(temp.getString("brand_name"));
                                product.setShop_name(temp.getString("shop_name"));
                                product.setProduct_url(temp.getString("product_url"));
                                product.setImage_id(temp.getString("image_id"));
                                product.setImg_url(temp.getString("img_url"));
                            }
                            products.add(product);
                            Log.d(TAG, product.toString());
                        }
                        isInProgress = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                findViewById(R.id.loadingPanelAR).setVisibility(View.GONE);
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

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if ( !isInProgress ) {
            camera.takePicture(null, null, mPicture);
            isInProgress = true;
        }
    }
}
