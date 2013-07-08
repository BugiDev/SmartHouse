package com.montequality.smarthouse;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
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
	private SoundPool soundPool;
	private int soundID;
	boolean loaded = false;
	Vibrator vibe;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	boolean soundSettings;
	boolean vibraSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		preferences = getSharedPreferences("smartHouse_auth",
				Context.MODE_PRIVATE);
		editor = preferences.edit();
		
		if(!preferences.contains("sound")){
			editor.putBoolean("sound", true);
			editor.commit();
		}
		
		if(!preferences.contains("vibra")){
			editor.putBoolean("vibra", true);
			editor.commit();
		}

		soundSettings = preferences.getBoolean("sound", true);
		vibraSettings = preferences.getBoolean("vibra", true);

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                    int status) {
                loaded = true;
            }
        });
		
		soundID = soundPool.load(this, R.raw.button_click, 1);

		dialog = new Dialog(this, R.style.custom_dialog);
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
				if(soundSettings){
					fireSound();
				}
				
				if(vibraSettings){
					vibe.vibrate(80);
				}
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
				if(soundSettings){
					fireSound();
				}
				
				if(vibraSettings){
					vibe.vibrate(80);
				}
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
		soundSettings = preferences.getBoolean("sound", true);
		vibraSettings = preferences.getBoolean("vibra", true);
		super.onResume();
	}

	public void fireSound(){
		// Getting the user sound settings
	      AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
	      float actualVolume = (float) audioManager
	          .getStreamVolume(AudioManager.STREAM_MUSIC);
	      float maxVolume = (float) audioManager
	          .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	      float volume = actualVolume / maxVolume;
	      // Is the sound loaded already?
	      if (loaded) {
	        soundPool.play(soundID, volume, volume, 1, 0, 1f);
	      }
	}
	
}
