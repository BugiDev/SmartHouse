package com.montequality.smarthouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.tasks.GetDevicesTask;
import com.montequality.smarthouse.util.SharedPrefs;
import com.montequality.smarthouse.util.SoundAndVibration;

public class HomeActivity extends Activity {

	ImageButton devices;
	ImageButton premadeMods;
	ImageButton info;
	ImageButton settigs;

	private SharedPrefs sharedPrefs;
	private SoundAndVibration soundAndVibra;

	public View mHomeView;
	public View mHomeStatusView;
	public TextView mHomeStatusMessageView;
	
	GetDevicesTask mTestTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		setSharedPrefs(new SharedPrefs(this));
		setSoundAndVibra(new SoundAndVibration(getSharedPrefs(), this));
		

		devices = (ImageButton) findViewById(R.id.devices_button);
		premadeMods = (ImageButton) findViewById(R.id.premade_mods_button);
		info = (ImageButton) findViewById(R.id.info_button);
		settigs = (ImageButton) findViewById(R.id.settings_button);

		mHomeView = findViewById(R.id.home_layout);
		mHomeStatusView = findViewById(R.id.home_status);
		mHomeStatusMessageView = (TextView) findViewById(R.id.home_status_message);
		
		devices.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				getSoundAndVibra().playSoundAndVibra();
				mTestTask = new GetDevicesTask(HomeActivity.this);
				mTestTask.execute((Void) null);

			}
		});

		premadeMods.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getSoundAndVibra().playSoundAndVibra();

				showToastMessage();

			}
		});

		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getSoundAndVibra().playSoundAndVibra();
				Intent intent = new Intent(HomeActivity.this,
						InfoActivity.class);
				startActivity(intent);

			}
		});

		settigs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getSoundAndVibra().playSoundAndVibra();
				Intent intent = new Intent(HomeActivity.this,
						SettingsActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	private void showToastMessage() {
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
	
	@Override
	protected void onResume() {
		
		soundAndVibra = new SoundAndVibration(sharedPrefs, this);
		super.onResume();
	}

}
