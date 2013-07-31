package com.montequality.smarthouse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.camera.MjpegActivity;
import com.montequality.smarthouse.entity.Aircondition;
import com.montequality.smarthouse.entity.Boiler;
import com.montequality.smarthouse.entity.Camera;
import com.montequality.smarthouse.entity.Light;
import com.montequality.smarthouse.entity.TV;
import com.montequality.smarthouse.entity.Temperature;
import com.montequality.smarthouse.entity.WindowBlinds;
import com.montequality.smarthouse.tasks.ChangeWindowBlindsDirectionTask;
import com.montequality.smarthouse.tasks.OnOffTask;
import com.montequality.smarthouse.util.CustomListAdapter;
import com.montequality.smarthouse.util.SharedPrefs;
import com.montequality.smarthouse.util.SoundAndVibration;

public class MainDevicesListActivity extends ListActivity {

    CustomListAdapter adapter;
    Dialog dialog = null;

    private SharedPrefs sharedPrefs;
    private SoundAndVibration soundAndVibra;

    OnOffTask onOffTask;
    ChangeWindowBlindsDirectionTask windowBlindsTask;

    List<Light> lightList = new ArrayList<Light>();
    List<Boiler> boilerList = new ArrayList<Boiler>();
    List<Temperature> temperatureList = new ArrayList<Temperature>();
    List<Camera> cameraList = new ArrayList<Camera>();
    List<Aircondition> airconditionList = new ArrayList<Aircondition>();
    List<TV> tvList = new ArrayList<TV>();
    List<WindowBlinds> windowBlindsList = new ArrayList<WindowBlinds>();

    List<String> allDevices = new ArrayList<String>();
    List<Integer> drawableLeft = new ArrayList<Integer>();
    List<Integer> drawableRight = new ArrayList<Integer>();

    public void onCreate(Bundle icicle) {
	super.onCreate(icicle);

	getDevicesFromJSON();

	initializeList();

	sharedPrefs = new SharedPrefs(this);
	soundAndVibra = new SoundAndVibration(sharedPrefs, this);

	dialog = new Dialog(this, R.style.custom_dialog);
	prepDialog();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

	soundAndVibra.playSoundAndVibra();

	String item = (String) getListAdapter().getItem(position);

	if (item.equalsIgnoreCase("Lights")) {

	    if (lightList.size() > 1) {
		Intent intent = new Intent(this, LightsList.class);
		intent.putExtra("jsonDevices", getIntent().getStringExtra("jsonDevices"));
		startActivity(intent);
	    } else if (lightList.size() == 1) {
		onOffTask = new OnOffTask(this, lightList.get(0).getId(), "Light", lightList.get(0).getRoom(), adapter, position);
		onOffTask.execute((Void) null);
	    }

	} else if (item.equalsIgnoreCase("Boiler")) {

	    if (boilerList.size() > 1) {
		Intent intent = new Intent(this, BoilerList.class);
		intent.putExtra("jsonDevices", getIntent().getStringExtra("jsonDevices"));
		startActivity(intent);
	    } else if (boilerList.size() == 1) {
		onOffTask = new OnOffTask(this, boilerList.get(0).getId(), "Boiler", boilerList.get(0).getRoom(), adapter, position);
		onOffTask.execute((Void) null);
	    }

	} else if (item.equalsIgnoreCase("Cameras")) {

	    if (cameraList.size() > 1) {
		Intent intent = new Intent(this, CameraList.class);
		intent.putExtra("jsonDevices", getIntent().getStringExtra("jsonDevices"));
		startActivity(intent);
	    } else if (cameraList.size() == 1) {
		Intent intent = new Intent(this, MjpegActivity.class);
		intent.putExtra("cameraURL", cameraList.get(0).getPath());
		startActivity(intent);
	    }

	} else if (item.contains("Temperature")) {

	    dialog.show();

	} else if (item.equalsIgnoreCase("TV")) {

	    if (tvList.size() > 1) {
		Intent intent = new Intent(this, TVList.class);
		intent.putExtra("jsonDevices", getIntent().getStringExtra("jsonDevices"));
		startActivity(intent);

	    } else if (tvList.size() == 1) {
		
		Intent intent = new Intent(this, RemoteTV.class);
		intent.putExtra("tvID", tvList.get(0).getId());
		intent.putExtra("room", tvList.get(0).getRoom());
		intent.putExtra("power", tvList.get(0).isPower());
		intent.putExtra("channel", tvList.get(0).getChannel());
		intent.putExtra("volume", tvList.get(0).getVolume());
		startActivity(intent);

	    }
	} else if (item.equalsIgnoreCase("Aircondition")) {

	    if (airconditionList.size() > 1) {
		Intent intent = new Intent(this, AirconditionList.class);
		intent.putExtra("jsonDevices", getIntent().getStringExtra("jsonDevices"));
		startActivity(intent);
	    } else if (airconditionList.size() == 1) {
		Intent intent = new Intent(this, RemoteAir.class);
		intent.putExtra("airID", airconditionList.get(0).getId());
		intent.putExtra("mode", airconditionList.get(0).getMode());
		intent.putExtra("room", airconditionList.get(0).getRoom());
		intent.putExtra("temperature", airconditionList.get(0).getTemperature());
		intent.putExtra("power", airconditionList.get(0).isPower());
		startActivity(intent);
	    }

	} else if (item.equalsIgnoreCase("Window blinds")) {

	    if (windowBlindsList.size() > 1) {
		Intent intent = new Intent(this, WindowBlindsList.class);
		intent.putExtra("jsonDevices", getIntent().getStringExtra("jsonDevices"));
		startActivity(intent);
	    } else if (windowBlindsList.size() == 1) {
		windowBlindsTask = new ChangeWindowBlindsDirectionTask(this, windowBlindsList.get(0).getId(), windowBlindsList.get(0).getRoom(),
			adapter, position);
		windowBlindsTask.execute((Void) null);
	    }

	} else {

	    showToastMessage(item + " clicked");

	}

	adapter.notifyDataSetChanged();

    }

    private void showToastMessage(String message) {
	LayoutInflater inflater = getLayoutInflater();
	View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
	((TextView) layout.findViewById(R.id.custom_toast_text)).setText(message);
	Toast toast = new Toast(getBaseContext());
	toast.setDuration(Toast.LENGTH_SHORT);
	toast.setView(layout);
	toast.show();
    }

    private void getDevicesFromJSON() {

	JSONObject jsonObj = null;
	JSONArray jArray = null;

	try {

	    jsonObj = new JSONObject(getIntent().getStringExtra("jsonDevices"));
	    jArray = jsonObj.getJSONArray("lightList");

	    for (int i = 0; i < jArray.length(); i++) {

		JSONObject json_data;

		json_data = jArray.getJSONObject(i);
		Light light_single = new Light(json_data.getInt("id"), json_data.getBoolean("power"), json_data.getString("room"));

		lightList.add(light_single);
	    }

	    jArray = jsonObj.getJSONArray("boilerList");

	    for (int i = 0; i < jArray.length(); i++) {

		JSONObject json_data;

		json_data = jArray.getJSONObject(i);
		Boiler boiler_single = new Boiler(json_data.getInt("id"), json_data.getBoolean("power"), json_data.getString("room"));

		boilerList.add(boiler_single);
	    }

	    jArray = jsonObj.getJSONArray("temperatureList");

	    for (int i = 0; i < jArray.length(); i++) {

		JSONObject json_data;

		json_data = jArray.getJSONObject(i);
		Temperature temperature_single = new Temperature(json_data.getInt("id"), json_data.getInt("temperature"), json_data.getString("room"));

		temperatureList.add(temperature_single);
	    }

	    jArray = jsonObj.getJSONArray("airconditionList");

	    for (int i = 0; i < jArray.length(); i++) {

		JSONObject json_data;

		json_data = jArray.getJSONObject(i);
		Aircondition air_single = new Aircondition(json_data.getInt("id"), json_data.getBoolean("power"), json_data.getString("room"),
			json_data.getString("mode"), json_data.getInt("temperature"));

		airconditionList.add(air_single);
	    }

	    jArray = jsonObj.getJSONArray("cameraList");

	    for (int i = 0; i < jArray.length(); i++) {

		JSONObject json_data;

		json_data = jArray.getJSONObject(i);

		Camera camera_single = new Camera(json_data.getInt("id"), json_data.getString("path"), json_data.getString("room"));

		cameraList.add(camera_single);
	    }

	    jArray = jsonObj.getJSONArray("tvList");

	    for (int i = 0; i < jArray.length(); i++) {

		JSONObject json_data;

		json_data = jArray.getJSONObject(i);
		TV tv_single = new TV(json_data.getInt("id"), json_data.getBoolean("power"), json_data.getString("room"),
			json_data.getInt("channel"), json_data.getInt("volume"));

		tvList.add(tv_single);
	    }

	    jArray = jsonObj.getJSONArray("windowsList");

	    for (int i = 0; i < jArray.length(); i++) {

		JSONObject json_data;

		json_data = jArray.getJSONObject(i);
		WindowBlinds window_single = new WindowBlinds(json_data.getInt("id"), json_data.getString("direction"), json_data.getString("room"));

		windowBlindsList.add(window_single);
	    }

	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void initializeList() {

	if (lightList.size() > 1) {
	    drawableRight.add(R.drawable.list_more);
	    allDevices.add("Lights");
	    drawableLeft.add(R.drawable.lightbuble_left);
	} else if (lightList.size() == 1) {
	    allDevices.add("Lights");
	    drawableLeft.add(R.drawable.lightbuble_left);
	    if (lightList.get(0).isPower()) {
		drawableRight.add(R.drawable.lightbuble_right_on);
	    } else {
		drawableRight.add(R.drawable.lightbuble_right_off);
	    }
	}

	if (boilerList.size() > 1) {
	    drawableRight.add(R.drawable.list_more);
	    allDevices.add("Boiler");
	    drawableLeft.add(R.drawable.boiler);
	} else if (boilerList.size() == 1) {
	    allDevices.add("Boiler");
	    drawableLeft.add(R.drawable.boiler);
	    if (boilerList.get(0).isPower()) {
		drawableRight.add(R.drawable.led_on);
	    } else {
		drawableRight.add(R.drawable.led_off);
	    }
	}

	if (airconditionList.size() > 1) {
	    drawableRight.add(R.drawable.list_more);
	    allDevices.add("Aircondition");
	    drawableLeft.add(R.drawable.aircondition);
	} else if (airconditionList.size() == 1) {
	    allDevices.add("Aircondition");
	    drawableLeft.add(R.drawable.aircondition);
	    drawableRight.add(R.drawable.remote);
	}

	if (tvList.size() > 1) {
	    drawableRight.add(R.drawable.list_more);
	    allDevices.add("TV");
	    drawableLeft.add(R.drawable.tv);
	} else if (tvList.size() == 1) {
	    allDevices.add("TV");
	    drawableLeft.add(R.drawable.tv);
	    drawableRight.add(R.drawable.remote);
	}

	if (cameraList.size() > 1) {
	    drawableRight.add(R.drawable.list_more);
	    allDevices.add("Cameras");
	    drawableLeft.add(R.drawable.camera);
	} else if (cameraList.size() == 1) {
	    allDevices.add("Cameras");
	    drawableLeft.add(R.drawable.camera);
	    drawableRight.add(R.drawable.watch);
	}

	if (windowBlindsList.size() > 1) {
	    drawableRight.add(R.drawable.list_more);
	    allDevices.add("Window blinds");
	    drawableLeft.add(R.drawable.blinds);
	} else if (windowBlindsList.size() == 1) {
	    allDevices.add("Window blinds");
	    drawableLeft.add(R.drawable.blinds);

	    if (windowBlindsList.get(0).getDirection().equalsIgnoreCase("up")) {
		drawableRight.add(R.drawable.arrow_down_camera);
	    } else {
		drawableRight.add(R.drawable.arrow_up_camera);
	    }

	}

	allDevices.add("Temperature: ".concat(Integer.toString(temperatureList.get(0).getTemperature())).concat("°C"));
	drawableLeft.add(R.drawable.temperature);
	drawableRight.add(R.drawable.temperature);

	adapter = new CustomListAdapter(this, allDevices, drawableLeft, drawableRight);
	setListAdapter(adapter);

	ListView list = getListView();
	ColorDrawable blue = new ColorDrawable(this.getResources().getColor(R.color.blue));
	list.setDivider(blue);
	list.setDividerHeight(1);
	list.setBackgroundColor(getResources().getColor(R.color.grey));

    }

    public void prepDialog() {
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setContentView(R.layout.custom_dialog_temperature);
	dialog.setTitle(R.string.temperature_title);

	// set the custom dialog components - text, image and button
	TextView text = (TextView) dialog.findViewById(R.id.dialog_temperature_text);
	text.setText("Temperature: ".concat(Integer.toString(temperatureList.get(0).getTemperature())).concat("°C"));

	ImageView image = (ImageView) dialog.findViewById(R.id.dialog_temperature_image);
	image.setImageResource(R.drawable.temperature);

	Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialog_temperature_button_ok);
	// if button is clicked, close the custom dialog
	dialogButtonOK.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		dialog.dismiss();
	    }
	});

    }

    @Override
    protected void onResume() {

	soundAndVibra = new SoundAndVibration(sharedPrefs, this);
	super.onResume();
    }

}
