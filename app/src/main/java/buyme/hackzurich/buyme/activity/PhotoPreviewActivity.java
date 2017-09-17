package buyme.hackzurich.buyme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buyme.hackzurich.buyme.R;
import buyme.hackzurich.buyme.domain.Product;
import buyme.hackzurich.buyme.util.CommonUtil;

public class PhotoPreviewActivity extends AppCompatActivity {

    private static String TAG = PhotoPreviewActivity.class.getSimpleName();
    private  List<Product> products;
    private ImageView photoPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        String pictureFile = getIntent().getStringExtra("image");
        products = (ArrayList<Product>) getIntent().getSerializableExtra("products");
        photoPreview = (ImageView) findViewById(R.id.photo_preview);
        try {
            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(pictureFile));
            photoPreview.setImageBitmap(mImageBitmap);
            photoPreview.setRotation(90);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.d(TAG, "heeeiii" +  photoPreview.getHeight());
        Log.d(TAG, "wiiidddd" +  photoPreview.getWidth());

        RectF bounds = new RectF();
        Drawable drawable = photoPreview.getDrawable();
        if (drawable != null) {
            photoPreview.getImageMatrix().mapRect(bounds, new RectF(drawable.getBounds()));
        }

        String xxxx = bounds.bottom + " - " + bounds.centerX() + " - " +bounds.centerY() + " - " + bounds.height() + " - " + bounds.left + " - " + bounds.right + " - " + bounds.top + " - " + bounds.width();
        Log.d(TAG, "total bounds" +  xxxx);

        createSubViews(85, (int)bounds.top, photoPreview.getHeight(), photoPreview.getWidth());

    }
    private void createSubViews(int offsetX, int offsetY, int height, int width) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlPreviewPhoto);
        for ( Product product : products ) {
            int _left = offsetX + (int)(Double.valueOf(product.getX()) * width);
            int _top = offsetY + (int)(Double.valueOf(product.getY()) * height);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); //WRAP_CONTENT param can be FILL_PARENT
            params.leftMargin = _left; //XCOORD
            params.topMargin = _top; //YCOORD

            ImageView priceTag = new ImageView(this);
            priceTag.setImageResource(R.drawable.pricetag);
            //priceTag.setX(_left);
            //priceTag.setY(_top);
            priceTag.setLayoutParams(params);
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
            price.setText(Html.fromHtml(product.getPrice()));
            price.setTextColor(Color.BLACK);
            price.setTextSize(17);
            price.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            price.setX(_left + 70);
            price.setY(_top - 25);
            layout.addView(price);


        }

    }
}
