package com.montequality.smarthouse;

import java.util.concurrent.ExecutionException;

import com.montequality.smarthouse.entity.TV;
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

public class RemoteTV extends Activity {

    private ImageButton powerBtn;

    private ImageButton channelUpBtn;
    private ImageButton channelDownBtn;

    private ImageButton volumeUpBtn;
    private ImageButton volumeDownBtn;

    private TextView roomText;
    private TextView channelText;
    private TextView volumeText;

    private TV tv = new TV();

    private SharedPrefs sharedPrefs;
    private SoundAndVibration soundAndVibra;
    
    private int channel;
    private int volume;
    
    OnOffTask onOffTask;
    RemoteControllTask remoteControllTask;
    GetRemoteParamsTask getRemoteParamsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_remote_tv);
	// Show the Up button in the action bar.
	setupActionBar();
	intialize();
	
	if(tv.isPower()){
		powerBtn.setImageDrawable(getResources().getDrawable(R.drawable.led_on));
	}else{
		powerBtn.setImageDrawable(getResources().getDrawable(R.drawable.led_off));
	}
	
	getRemoteParamsTask = new GetRemoteParamsTask(tv.getId(), "volume", this);
	try {
		volume = Integer.parseInt(getRemoteParamsTask.execute((Void)null).get());
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
	
	getRemoteParamsTask = new GetRemoteParamsTask(tv.getId(), "channel", this);
	try {
		channel = Integer.parseInt(getRemoteParamsTask.execute((Void)null).get());
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
	
	roomText.setText(getResources().getString(R.string.remote_tv_room) + " " + tv.getRoom());
	channelText.setText(getResources().getString(R.string.remote_tv_channel) + " " + channel);
	volumeText.setText(getResources().getString(R.string.remote_tv_volume) + " " + volume);

	sharedPrefs = new SharedPrefs(this);
	soundAndVibra = new SoundAndVibration(sharedPrefs, this);
	
	powerBtn.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		soundAndVibra.playSoundAndVibra();
		onOffTask = new OnOffTask(RemoteTV.this, tv.getId(), "TV", tv.getRoom(), powerBtn);
		onOffTask.execute((Void) null);
	    }
	});
	
	channelUpBtn.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		soundAndVibra.playSoundAndVibra();
		remoteControllTask = new RemoteControllTask(RemoteTV.this, tv.getId(), "channelUp", channelText);
		remoteControllTask.execute((Void)null);
		
	    }
	});
	
	channelDownBtn.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		soundAndVibra.playSoundAndVibra();
		remoteControllTask = new RemoteControllTask(RemoteTV.this, tv.getId(), "channelDown", channelText);
		remoteControllTask.execute((Void)null);
		
	    }
	});
	
	volumeUpBtn.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		soundAndVibra.playSoundAndVibra();
		remoteControllTask = new RemoteControllTask(RemoteTV.this, tv.getId(), "volumeUp", volumeText);
		remoteControllTask.execute((Void)null);
		
	    }
	});
	
	volumeDownBtn.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		soundAndVibra.playSoundAndVibra();
		remoteControllTask = new RemoteControllTask(RemoteTV.this, tv.getId(), "volumeDown", volumeText);
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
	getMenuInflater().inflate(R.menu.remote_tv, menu);
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
	powerBtn = (ImageButton) findViewById(R.id.tv_remote_power);
	
	channelUpBtn = (ImageButton) findViewById(R.id.tv_remote_ch_up);
	channelDownBtn = (ImageButton) findViewById(R.id.tv_remote_ch_down);

	volumeUpBtn = (ImageButton) findViewById(R.id.tv_remote_v_up);
	volumeDownBtn = (ImageButton) findViewById(R.id.tv_remote_v_down);

	roomText = (TextView) findViewById(R.id.tv_remote_text_room);
	channelText = (TextView) findViewById(R.id.tv_remote_text_channel);
	volumeText = (TextView) findViewById(R.id.tv_remote_text_volume);

	tv.setId(getIntent().getIntExtra("tvID", -1));
	tv.setRoom(getIntent().getStringExtra("room"));
	tv.setPower(getIntent().getBooleanExtra("power", false));
	tv.setChannel(getIntent().getIntExtra("channel", -1));
	tv.setVolume(getIntent().getIntExtra("volume", -1));

    }

}
