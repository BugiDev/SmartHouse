package com.montequality.smarthouse;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {

	private static final int SPLASH_TIME = 2 * 1000;// 3 seconds
	Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		dialog = new Dialog(this);
		prepDialog();

	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void prepDialog() {

		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle(R.string.dialog_title_network);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
		text.setText(R.string.dialog_text_network);

		ImageView image = (ImageView) dialog.findViewById(R.id.dialog_image);
		image.setImageResource(R.drawable.warning);

		Button dialogButtonOK = (Button) dialog
				.findViewById(R.id.dialog_button_ok);
		// if button is clicked, close the custom dialog
		dialogButtonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				startActivity(intent);
			}
		});

		Button dialogButtonCancle = (Button) dialog
				.findViewById(R.id.dialog_button_cancle);
		// if button is clicked, close the custom dialog
		dialogButtonCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});

	}

	public void showSplash() {

		try {
			new Handler().postDelayed(new Runnable() {

				public void run() {

					if (isNetworkAvailable()) {
						SplashScreenActivity.this.finish();

						overridePendingTransition(R.anim.fade_in,
								R.anim.fade_out);

						Intent intent = new Intent(SplashScreenActivity.this,
								LoginActivity.class);
						startActivity(intent);
					} else {
						dialog.show();
					}

				}

			}, SPLASH_TIME);

			new Handler().postDelayed(new Runnable() {
				public void run() {
				}
			}, SPLASH_TIME);
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

	@Override
	protected void onResume() {

		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		showSplash();
		super.onResume();
	}
	

}
