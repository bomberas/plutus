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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import buyme.hackzurich.buyme.R;

public class UploadPhotoActivity extends AppCompatActivity {

    public static String TAG = UploadPhotoActivity.class.getSimpleName();
    private HashMap<String, HashMap<String,Object>> items = new HashMap<>();

    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        Log.i(TAG, "Starting Upload Activity");
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

        setData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView = (ImageView) findViewById(R.id.photo_selected);
                imageView.setImageBitmap(bitmap);
                imageView.setRotation(90);
                setViews();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setViews() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlUploadPhoto);
        int height = bitmap.getWidth();
        int width = bitmap.getHeight();

        Log.d(TAG, "height " + height);
        Log.d(TAG, "width " + width);
        Log.d(TAG, "height " + imageView.getHeight());
        Log.d(TAG, "height " + imageView.getMeasuredHeight());
        Log.d(TAG, "width " + imageView.getWidth());
        Log.d(TAG, "width " + imageView.getMeasuredWidth());

        for ( HashMap<String, Object> item : this.items.values() ) {
            int _left = (int)(Double.parseDouble(String.valueOf(item.get("x"))) * width);
            int _top = (int)(Double.parseDouble(String.valueOf(item.get("y"))) * height);

            ImageView priceTag = new ImageView(this);
            priceTag.setImageResource(R.drawable.pricetag);
            priceTag.setX(_left);
            priceTag.setY(_top);
            layout.addView(priceTag);

            TextView price = new TextView(this);
            price.setText(String.valueOf(item.get("price")));
            price.setTextColor(Color.BLACK);
            price.setTextSize(17);
            price.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            price.setX(_left + 70);
            price.setY(_top - 25);
            layout.addView(price);
        }

    }

    private void setData() {
        HashMap<String, Object> cardigan = new HashMap<String, Object>();
        cardigan.put("title", "Topshop Strickpullover blue");
        cardigan.put("price", "37.45 €");
        cardigan.put("y", 0.247);
        cardigan.put("x", 0.30101302460202606);
        cardigan.put("height", 0.289);
        cardigan.put("width", 0.3835021707670043);
        items.put("cardigan", cardigan);

        HashMap<String, Object> jeans = new HashMap<String, Object>();
        jeans.put("title", "Glamorous Tall Jeans Skinny Fit dark blue");
        jeans.put("price", "40.95 €");
        jeans.put("y", 0.499);
        jeans.put("x", 0.3473227206946454);
        jeans.put("height", 0.402);
        jeans.put("width", 0.24457308248914617);
        items.put("jeans", jeans);

        HashMap<String, Object> shoes = new HashMap<String, Object>();
        shoes.put("title", "Zign Sportlicher Schnürer brown");
        shoes.put("price", "64.95 €");
        shoes.put("y", 0.86);
        shoes.put("x", 0.3748191027496382);
        shoes.put("height", 0.117);
        shoes.put("width", 0.2272069464544139);
        items.put("shoes", shoes);

        HashMap<String, Object> glasses = new HashMap<String, Object>();
        glasses.put("title", "RayBan Sonnenbrille gold");
        glasses.put("price", "154.94 €");
        glasses.put("y", 0.146);
        glasses.put("x", 0.43270622286541244);
        glasses.put("height", 0.064);
        glasses.put("width", 0.12301013024602026);
        items.put("glasses", glasses);
    }

}
