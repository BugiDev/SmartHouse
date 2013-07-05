package com.montequality.smarthouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class SplashScreenActivity extends Activity {

	private static final int SPLASH_TIME = 2 * 1000;// 3 seconds

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		try {
		new Handler().postDelayed(new Runnable() {

			public void run() {
				if(isNetworkAvailable()){
					Intent intent = new Intent(SplashScreenActivity.this,
							LoginActivity.class);
						startActivity(intent);
				}else {
					
				}
				

				SplashScreenActivity.this.finish();

				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

			}
			
			
		}, SPLASH_TIME);
		
		new Handler().postDelayed(new Runnable() {
			  public void run() {
			         } 
			    }, SPLASH_TIME);
		} catch(Exception e){}
	}

	
	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
