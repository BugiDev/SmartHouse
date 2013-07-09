package com.montequality.smarthouse;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private SoundPool soundPool;
	private int soundID;
	boolean loaded = false;
	Vibrator vibe;
	ImageButton devices;
	ImageButton premadeMods;
	ImageButton info;
	ImageButton settigs;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	boolean soundSettings;
	boolean vibraSettings;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		preferences = getSharedPreferences("smartHouse_auth",
				Context.MODE_PRIVATE);
		editor = preferences.edit();
		
		
		soundSettings = preferences.getBoolean("sound", true);
		vibraSettings = preferences.getBoolean("vibra", true);

		devices = (ImageButton) findViewById(R.id.devices_button);
		premadeMods = (ImageButton) findViewById(R.id.premade_mods_button);
		info = (ImageButton) findViewById(R.id.info_button);
		settigs = (ImageButton) findViewById(R.id.settings_button);

		vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});

		soundID = soundPool.load(this, R.raw.button_click, 1);

		devices.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(soundSettings){
					fireSound();
				}
				
				if(vibraSettings){
					vibe.vibrate(80);
				}
				
				Intent intent = new Intent(HomeActivity.this, MainDevicesListActivity.class);
				startActivity(intent);
			}
		});

		premadeMods.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(soundSettings){
					fireSound();
				}
				
				if(vibraSettings){
					vibe.vibrate(80);
				}
				
				showToastMessage();

			}
		});

		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(soundSettings){
					fireSound();
				}
				
				if(vibraSettings){
					vibe.vibrate(80);
				}
				Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
				startActivity(intent);

			}
		});

		settigs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(soundSettings){
					fireSound();
				}
				
				if(vibraSettings){
					vibe.vibrate(80);
				}
				Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
				startActivity(intent);
				Log.d("PROBA HOME", "Sound = " + preferences.getBoolean("sound", true));
				Log.d("PROBA HOME", "Vibra = " + preferences.getBoolean("vibra", true));
			}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void fireSound() {
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

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
		
	}
	
	@Override
	protected void onResume() {
		soundSettings = preferences.getBoolean("sound", true);
		vibraSettings = preferences.getBoolean("vibra", true);
		super.onResume();
	}

	private void showToastMessage(){
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) findViewById(R.id.toast_layout));
		((TextView) layout.findViewById(R.id.custom_toast_text))
				.setText(R.string.premade_mods_toast_message);
		Toast toast = new Toast(getBaseContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
	
}
