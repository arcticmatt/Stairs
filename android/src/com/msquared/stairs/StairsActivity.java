package com.msquared.stairs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.utils.IActivityRequestHandler;

public class StairsActivity extends AndroidApplication implements IActivityRequestHandler {
	public static final String AD_UNIT_ID = "ca-app-pub-5734601135747352/4365890823";
	public AdView adView;
	public RelativeLayout.LayoutParams adParams;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	RelativeLayout layout;
	public static final String TEST_ADS_LG = "95CFF973B1B840806C43234A952D1F07";
	private static final boolean USE_TEST_DEVICES = false;

    protected Handler handler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		switch(msg.what) {
    		case SHOW_ADS:
    			if (adView.getVisibility() == View.GONE) {
    				layout.addView(adView, adParams);
    				adView.setVisibility(View.VISIBLE);
    			}
    			break;
    		case HIDE_ADS:
    			if (adView.getVisibility() == View.VISIBLE) {
    				adView.setVisibility(View.GONE);
    				layout.removeView(adView);
    			}
    			break;
    		}
    	}
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;

		/*** Admob view stuff ***/
		// Create the layout
		layout = new RelativeLayout(this);

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// Create the libgdx View
		View gameView = initializeForView(new Stairs(false, false, this), config);

		// Create and setup the AdMob view
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		AdRequest adRequest;
		if (USE_TEST_DEVICES) {
			// Test ads
			adRequest = new AdRequest.Builder()
	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	        .addTestDevice("D52FF7CF5E8C1D04CD5A8584F0737B22")
	        .addTestDevice(TEST_ADS_LG)
	        .build();
		} else {
			// Real ads
			adRequest = new AdRequest.Builder().build();
		}
		adView.loadAd(adRequest);

		// Add the libgdx view
		layout.addView(gameView);;

		// Add the AdMob view
		adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		if (!Stairs.PAID_VERSION)
			layout.addView(adView, adParams);
		adView.setVisibility(View.VISIBLE);
		showAds(false);

		// Hook it all up
		setContentView(layout);
    }


	@Override
	// This is the callback that posts a message for the handler
	public void showAds(boolean show) {
		if (!Stairs.PAID_VERSION)
			handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}
