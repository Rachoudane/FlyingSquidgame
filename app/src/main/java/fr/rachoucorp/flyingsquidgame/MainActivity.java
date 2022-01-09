package fr.rachoucorp.flyingsquidgame;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity  extends Activity{


    private AdView mAdView;
    private String mAppUnitId = "ca-app-pub-8567266539414859~1858343782";
    public MediaPlayer mediaplayer;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        // vvv : pour les curved screens
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES; }

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //blocking screen view
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        GameView gV = new GameView(this);
        setContentView(gV);
        mAdView = findViewById(R.id.adView);
        initializeBannerAd(mAppUnitId);
        loadBannerAd();
    }

    private void initializeBannerAd(String appUnitId) {

        MobileAds.initialize(this );
    }

    private void loadBannerAd() {
    }

    @Override
    public void onPause() {
        super.onPause();

        mediaplayer.pause();

    }

    @Override
    protected void onStop() {

        super.onStop();
        mediaplayer.release();
// Always call the superclass method first
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Always call the superclass method first

    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaplayer.start();
        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaplayer = MediaPlayer.create(this,R.raw.flyingsquidgamemusicver2);
        mediaplayer.setLooping(true);
        // Always call the superclass method first
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // save your state data to the instance state bundle
    }

    }