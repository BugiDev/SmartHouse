package com.montequality.smarthouse;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.montequality.smarthouse.tasks.AuthenticateFromPrefsTask;
import com.montequality.smarthouse.tasks.CheckInternetTask;
import com.montequality.smarthouse.util.SharedPrefs;
import com.montequality.smarthouse.util.SoundAndVibration;

public class SplashScreenActivity extends Activity {

	public Dialog dialog = null;
	private SharedPrefs sharedPrefs;
	private SoundAndVibration soundAndVibra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		setSharedPrefs(new SharedPrefs(this));
		setSoundAndVibra(new SoundAndVibration(getSharedPrefs(), this));

		dialog = new Dialog(this, R.style.custom_dialog);
		prepDialog();

	}

	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
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
				getSoundAndVibra().playSoundAndVibra();
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
				getSoundAndVibra().playSoundAndVibra();
				dialog.dismiss();
				finish();
			}
		});

	}

	public void showSplash() {

		CheckInternetTask checkInternet = new CheckInternetTask(this);
		
		checkInternet.execute((Void) null);
		
		AuthenticateFromPrefsTask authFromPrefs = new AuthenticateFromPrefsTask(this, getSharedPrefs().getPreferences().getString("username", null), getSharedPrefs().getPreferences().getString("password", null));
	
		authFromPrefs.execute((Void)null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

	@Override
	protected void onResume() {
		
		soundAndVibra = new SoundAndVibration(sharedPrefs, this);

		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		showSplash();
		super.onResume();
	}

	public SharedPrefs getSharedPrefs() {
		return sharedPrefs;
	}

	public void setSharedPrefs(SharedPrefs sharedPrefs) {
		this.sharedPrefs = sharedPrefs;
	}

	public SoundAndVibration getSoundAndVibra() {
		return soundAndVibra;
	}

	public void setSoundAndVibra(SoundAndVibration soundAndVibra) {
		this.soundAndVibra = soundAndVibra;
	}
}
