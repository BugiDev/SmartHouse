package com.montequality.smarthouse;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HomeActivity extends Activity {

	private SoundPool soundPool;
	private int soundID;
	boolean loaded = false;
	Vibrator vibe;
	ImageButton devices;
	ImageButton premadeMods;
	ImageButton info;
	ImageButton settigs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

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
				fireSound();
				vibe.vibrate(80);

			}
		});

		premadeMods.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				fireSound();
				vibe.vibrate(80);

			}
		});

		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				fireSound();
				vibe.vibrate(80);

			}
		});

		settigs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				fireSound();
				vibe.vibrate(80);

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

}
