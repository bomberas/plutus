package buyme.hackzurich.buyme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import buyme.hackzurich.buyme.R;
import buyme.hackzurich.buyme.util.RestClient;

public class HomeActivity extends AppCompatActivity {

    public static String TAG = HomeActivity.class.getSimpleName();
    public static final int RESULT_GALLERY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Starting Camera Activity");
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadPhotoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });


        RestClient.getProductsInStores(null, "https://4.bp.blogspot.com/-hH-RzRv_5SU/WBiXkuofvvI/AAAAAAAAIAI/sDERp1wIzG0XNEGInAQr83o9970WVxX0wCLcB/s1600/01d11eb823d638967dcf11198521549f.jpg",null,0,0,0,this);

    }

}
