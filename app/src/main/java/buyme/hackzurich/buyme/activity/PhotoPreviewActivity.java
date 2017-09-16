package buyme.hackzurich.buyme.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.IOException;

import buyme.hackzurich.buyme.R;

public class PhotoPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        String pictureFile = getIntent().getStringExtra("image");

        ImageView photoPreview = (ImageView) findViewById(R.id.photo_preview);
        try {
            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(pictureFile));
            photoPreview.setImageBitmap(mImageBitmap);
            photoPreview.setRotation(90);
            photoPreview.setAdjustViewBounds(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
