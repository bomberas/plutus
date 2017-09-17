package buyme.hackzurich.buyme.activity;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import buyme.hackzurich.buyme.R;

public class Splashscreen extends AppCompatActivity {

    //Set waktu lama splashscreen
    private static int splashInterval = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splashscreen);

        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.pbSplashScreen);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        progressAnimator.setDuration(splashInterval);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splashscreen.this, HomeActivity.class);
                startActivity(i); // menghubungkan activity splashscren ke main activity dengan intent
            }
        }, splashInterval);

    };

}
