package com.montequality.smarthouse;

import java.util.concurrent.ExecutionException;

import com.montequality.smarthouse.entity.Aircondition;
import com.montequality.smarthouse.tasks.GetRemoteParamsTask;
import com.montequality.smarthouse.tasks.OnOffTask;
import com.montequality.smarthouse.tasks.RemoteControllTask;
import com.montequality.smarthouse.util.SharedPrefs;
import com.montequality.smarthouse.util.SoundAndVibration;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View.OnClickListener;

public class RemoteAir extends Activity {

    private ImageButton powerBtn;
    private ImageButton temperatureUpBtn;
    private ImageButton temperatureDownBtn;

    private TextView roomText;
    private TextView modeText;
    private TextView temperatureText;

    private Aircondition aircondition = new Aircondition();

    private SharedPrefs sharedPrefs;
    private SoundAndVibration soundAndVibra;
    
    private String mode;
    private int temperature;

    OnOffTask onOffTask;
    
    RemoteControllTask remoteControllTask;
    GetRemoteParamsTask getRemoteParamsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_remote_air);
	// Show the Up button in the action bar.
	setupActionBar();
	intialize();
	
	if(aircondition.isPower()){
		powerBtn.setImageDrawable(getResources().getDrawable(R.drawable.led_on));
	}else{
		powerBtn.setImageDrawable(getResources().getDrawable(R.drawable.led_off));
	}
	
	getRemoteParamsTask = new GetRemoteParamsTask(aircondition.getId(), "mode", this);
	try {
		mode = getRemoteParamsTask.execute((Void)null).get();
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	getRemoteParamsTask = new GetRemoteParamsTask(aircondition.getId(), "temperature", this);
	try {
		temperature = Integer.parseInt(getRemoteParamsTask.execute((Void)null).get());
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	roomText.setText(getResources().getString(R.string.remote_air_room) + " " + aircondition.getRoom());
	modeText.setText(getResources().getString(R.string.remote_air_mode) + " " + mode);
	temperatureText.setText(getResources().getString(R.string.remote_air_temperature) + " " + temperature);

	sharedPrefs = new SharedPrefs(this);
	soundAndVibra = new SoundAndVibration(sharedPrefs, this);

	powerBtn.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		soundAndVibra.playSoundAndVibra();
		onOffTask = new OnOffTask(RemoteAir.this, aircondition.getId(), "Aircondition", aircondition.getRoom(), powerBtn);
		onOffTask.execute((Void) null);
	    }
	});

	temperatureUpBtn.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		soundAndVibra.playSoundAndVibra();
		remoteControllTask = new RemoteControllTask(RemoteAir.this, aircondition.getId(), "temperatureUp", temperatureText);
		remoteControllTask.execute((Void)null);
	    }
	});

	temperatureDownBtn.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		soundAndVibra.playSoundAndVibra();
		remoteControllTask = new RemoteControllTask(RemoteAir.this, aircondition.getId(), "temperatureDown", temperatureText);
		remoteControllTask.execute((Void)null);
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
	getMenuInflater().inflate(R.menu.remote_air, menu);
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
	    onBackPressed();
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    public void intialize() {
	powerBtn = (ImageButton) findViewById(R.id.remote_air_power);
	temperatureUpBtn = (ImageButton) findViewById(R.id.remote_air_t_up);
	temperatureDownBtn = (ImageButton) findViewById(R.id.remote_air_t_down);

	roomText = (TextView) findViewById(R.id.remote_air_text_room);
	modeText = (TextView) findViewById(R.id.remote_air_text_mode);
	temperatureText = (TextView) findViewById(R.id.remote_air_text_temperature);

	aircondition.setId(getIntent().getIntExtra("airID", -1));
	aircondition.setMode(getIntent().getStringExtra("mode"));
	aircondition.setRoom(getIntent().getStringExtra("room"));
	aircondition.setPower(getIntent().getBooleanExtra("power", false));
	aircondition.setTemperature(getIntent().getIntExtra("temperature", -1));

    }

}
