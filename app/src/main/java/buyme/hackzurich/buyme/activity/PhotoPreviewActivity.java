package buyme.hackzurich.buyme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buyme.hackzurich.buyme.R;
import buyme.hackzurich.buyme.domain.Product;

public class PhotoPreviewActivity extends AppCompatActivity {

    private static String TAG = UploadPhotoActivity.class.getSimpleName();
    private  List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        String pictureFile = getIntent().getStringExtra("image");
        products = (ArrayList<Product>) getIntent().getSerializableExtra("products");

        ImageView photoPreview = (ImageView) findViewById(R.id.photo_preview);
        try {
            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(pictureFile));
            photoPreview.setImageBitmap(mImageBitmap);
            photoPreview.setRotation(90);
            createSubViews(mImageBitmap, photoPreview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }


    private void createSubViews(Bitmap bitmap, ImageView imageView) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlPreviewPhoto);
        int height = bitmap.getWidth();
        int width = bitmap.getHeight();
        int initialX = imageView.getLeft();
        int initialY = imageView.getTop();

        Log.i(TAG, "initialX  = " + width);
        Log.i(TAG, "initialY  = " + height);

        for ( Product product : products ) {
            int _left = initialX + (int)(Double.valueOf(product.getX()) * width);
            int _top = initialY + (int)(Double.valueOf(product.getY()) * height);

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
}
