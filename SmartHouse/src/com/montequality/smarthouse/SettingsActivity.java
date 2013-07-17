package com.montequality.smarthouse;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	private SoundPool soundPool;
	private int soundID;
	boolean loaded = false;
	Vibrator vibe;
	
	boolean soundSettings;
	boolean vibraSettings;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	CheckBox soundCheck;
	CheckBox vibraCheck;
	Button logoutButton;
	
	Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();

		preferences = getSharedPreferences("smartHouse_auth",
				Context.MODE_PRIVATE);
		editor = preferences.edit();

		soundCheck = (CheckBox) findViewById(R.id.settings_sound_check);
		vibraCheck = (CheckBox) findViewById(R.id.settings_vibra_check);
		logoutButton = (Button) findViewById(R.id.settings_button_logout);
		
		dialog = new Dialog(this, R.style.custom_dialog);
		prepDialog();

		if (preferences.getBoolean("sound", true)) {
			soundCheck.setChecked(true);
		} else {
			soundCheck.setChecked(false);
		}

		if ((preferences.getBoolean("vibra", true))) {
			vibraCheck.setChecked(true);
		} else {
			vibraCheck.setChecked(false);
		}
		
		soundSettings = preferences.getBoolean("sound", true);
		vibraSettings = preferences.getBoolean("vibra", true);
		
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

		soundCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					editor.putBoolean("sound", true);
					editor.commit();
					showToastMessage(R.string.settings_sound_enabled_toast_message);
					
				} else {
					editor.putBoolean("sound", false);
					editor.commit();
					showToastMessage(R.string.settings_sound_disabled_toast_message);
				}
			}
		});

		vibraCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					editor.putBoolean("vibra", true);
					editor.commit();
					showToastMessage(R.string.settings_vibra_enabled_toast_message);
				} else {
					editor.putBoolean("vibra", false);
					editor.commit();
					showToastMessage(R.string.settings_vibra_disabled_toast_message);
				}
			}
		});
		
		logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.show();
				
			}
		});

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void prepDialog() {

		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle(R.string.settings_dialog_title);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
		text.setText(R.string.settings_dialog_text);

		ImageView image = (ImageView) dialog.findViewById(R.id.dialog_image);
		image.setImageResource(R.drawable.warning);

		Button dialogButtonOK = (Button) dialog
				.findViewById(R.id.dialog_button_ok);
		// if button is clicked, close the custom dialog
		dialogButtonOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editor.remove("username");
				editor.remove("password");
				editor.commit();
				dialog.dismiss();
				showToastMessage(R.string.settings_logout_toast_message);
			}
		});

		Button dialogButtonCancle = (Button) dialog
				.findViewById(R.id.dialog_button_cancle);
		// if button is clicked, close the custom dialog
		dialogButtonCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}
	
	private void showToastMessage(int message){
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) findViewById(R.id.toast_layout));
		((TextView) layout.findViewById(R.id.custom_toast_text))
				.setText(message);
		Toast toast = new Toast(getBaseContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
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

}
