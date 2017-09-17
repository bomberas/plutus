package buyme.hackzurich.buyme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import buyme.hackzurich.buyme.R;
import buyme.hackzurich.buyme.domain.MultiPartStack;
import buyme.hackzurich.buyme.domain.MultiPartStringRequest;
import buyme.hackzurich.buyme.domain.Product;
import buyme.hackzurich.buyme.util.Constant;

public class UploadPhotoActivity extends AppCompatActivity {

    public static String TAG = UploadPhotoActivity.class.getSimpleName();
    private HashMap<String, HashMap<String,Object>> items = new HashMap<>();

    private Bitmap bitmap;
    private ImageView imageView;

    private List<Product> products = new ArrayList<>();
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        findViewById(R.id.loadingPanelUploadPhoto).setVisibility(View.VISIBLE);

        Log.i(TAG, "Starting Upload Activity");
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            String path = uri.getPath().substring(uri.getPath().indexOf(":") + 1);
            makeCall(new File("/storage/emulated/0/" + path));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    private void createSubViews(Bitmap bitmap, ImageView imageView) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlUploadPhoto);
        int height = bitmap.getWidth();
        int width = bitmap.getHeight();
        int initialX = imageView.getLeft();
        int initialY = imageView.getTop();

        Log.i(TAG, "initialX  = " + width);
        Log.i(TAG, "initialY  = " + height);

        Log.d(TAG, "**************" + products.size());

        for (Product product : products) {
            int _left = (int) (Double.valueOf(product.getX()) * width);
            int _top = (int) (Double.valueOf(product.getY()) * height);

            ImageView priceTag = new ImageView(this);
            priceTag.setImageResource(R.drawable.pricetag);
            priceTag.setX(_left);
            priceTag.setY(_top);
            layout.addView(priceTag);

            Display display = getWindowManager().getDefaultDisplay();
            String displayName = display.getName();  // minSdkVersion=17+
            Log.i(TAG, "displayName  = " + displayName);

            // display size in pixels
            Point size = new Point();
            display.getSize(size);
            int width1 = size.x;
            int height1 = size.y;
            Log.d(TAG, "width        = " + width1);
            Log.d(TAG, "height       = " + height1);

            TextView price = new TextView(this);
            price.setText(product.getPrice());
            price.setTextColor(Color.BLACK);
            price.setTextSize(17);
            price.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            price.setX(_left + 70);
            price.setY(_top - 25);
            layout.addView(price);
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
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView = (ImageView) findViewById(R.id.photo_selected);
                            imageView.setImageBitmap(bitmap);
                            imageView.setRotation(90);
                            createSubViews(bitmap, imageView);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                findViewById(R.id.loadingPanelUploadPhoto).setVisibility(View.GONE);
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
