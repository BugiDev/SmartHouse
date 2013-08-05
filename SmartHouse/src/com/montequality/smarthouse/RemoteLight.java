package com.montequality.smarthouse;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.montequality.smarthouse.entity.PotentiometerLight;
import com.montequality.smarthouse.tasks.PotentiometerLightUpDownTask;
import com.montequality.smarthouse.util.SharedPrefs;
import com.montequality.smarthouse.util.SoundAndVibration;

public class RemoteLight extends Activity {

    private ImageButton potLightUpBtn;
    private ImageButton potLightDownBtn;

    private TextView percentText;
    private TextView roomText;

    private ImageView potLightImage;

    private PotentiometerLight potLight = new PotentiometerLight();

    private SharedPrefs sharedPrefs;
    private SoundAndVibration soundAndVibra;

    private String percent;

    PotentiometerLightUpDownTask potLightTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_light);
        // Show the Up button in the action bar.
        setupActionBar();
        intialize();

        if (potLight.getPowerMeter() == 0) {
            potLightImage.setImageDrawable(getResources().getDrawable(R.drawable.lightbuble_right_off));
        } else if (potLight.getPowerMeter() == 0.25) {
            potLightImage.setImageDrawable(getResources().getDrawable(R.drawable.lightbuble_25));
        } else if (potLight.getPowerMeter() == 0.50) {
            potLightImage.setImageDrawable(getResources().getDrawable(R.drawable.lightbuble_50));
        } else if (potLight.getPowerMeter() == 0.75) {
            potLightImage.setImageDrawable(getResources().getDrawable(R.drawable.lightbuble_75));
        } else {
            potLightImage.setImageDrawable(getResources().getDrawable(R.drawable.lightbuble_right_on));
        }
        
        roomText.setText(potLight.getRoom());
        
        percentText.setText(percent + "%");

        sharedPrefs = new SharedPrefs(this);
        soundAndVibra = new SoundAndVibration(sharedPrefs, this);
        
        potLightUpBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                soundAndVibra.playSoundAndVibra();
                potLightTask = new PotentiometerLightUpDownTask(RemoteLight.this, potLight.getId(), potLight.getRoom(), "up", percentText, potLightImage);
                potLightTask.execute((Void)null);
                
            }
        });
        
        potLightDownBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                soundAndVibra.playSoundAndVibra();
                potLightTask = new PotentiometerLightUpDownTask(RemoteLight.this, potLight.getId(), potLight.getRoom(), "down", percentText, potLightImage);
                potLightTask.execute((Void)null);
                
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

        potLightUpBtn = (ImageButton) findViewById(R.id.remote_light_btn_up);
        potLightDownBtn = (ImageButton) findViewById(R.id.remote_light_btn_down);

        percentText = (TextView) findViewById(R.id.remote_light_text);
        roomText = (TextView) findViewById(R.id.remote_light_room);

        potLightImage = (ImageView) findViewById(R.id.remote_light_image);

        potLight.setId(getIntent().getIntExtra("potLightID", -1));
        potLight.setRoom(getIntent().getStringExtra("room"));
        potLight.setPowerMeter(getIntent().getDoubleExtra("powerMeter", 0));

        percent = Double.toString(potLight.getPowerMeter());

    }

}
