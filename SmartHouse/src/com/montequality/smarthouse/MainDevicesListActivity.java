package com.montequality.smarthouse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.entity.Boiler;
import com.montequality.smarthouse.entity.Light;
import com.montequality.smarthouse.entity.Temperature;
import com.montequality.smarthouse.tasks.OnOffTask;
import com.montequality.smarthouse.util.CustomListAdapter;

public class MainDevicesListActivity extends ListActivity {

	CustomListAdapter adapter;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	OnOffTask onOffTask;

	List<Light> lightList = new ArrayList<Light>();
	List<Boiler> boilerList = new ArrayList<Boiler>();
	List<Temperature> temperatureList = new ArrayList<Temperature>();

	List<String> allDevices = new ArrayList<String>();
	List<Integer> drawableLeft = new ArrayList<Integer>();
	List<Integer> drawableRight = new ArrayList<Integer>();

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		getDevicesFromJSON();

		initializeList();

		preferences = getSharedPreferences("smartHouse_auth",
				Context.MODE_PRIVATE);
		editor = preferences.edit();

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		String item = (String) getListAdapter().getItem(position);

		if(item.equalsIgnoreCase("Lights")){
			if (lightList.size() > 1) {
				Intent intent = new Intent(this, LightsList.class);
				intent.putExtra("jsonDevices",
						getIntent().getStringExtra("jsonDevices"));
				startActivity(intent);
			} else if (lightList.size() == 1) {
				onOffTask = new OnOffTask(this, lightList.get(0).getId(),
						"Light", lightList.get(0).getRoom(), adapter, position);
				onOffTask.execute((Void) null);
			}

		}else if(item.equalsIgnoreCase("Boiler")){
			if (boilerList.size() > 1) {
				// TODO Auto-generated catch block
			} else if (boilerList.size() == 1) {
				onOffTask = new OnOffTask(this, boilerList.get(0).getId(),
						"Boiler", boilerList.get(0).getRoom(), adapter, position);
				onOffTask.execute((Void) null);
			}
		}else{
			showToastMessage(item + " clicked");
		}
		
		adapter.notifyDataSetChanged();

	}

	private void showToastMessage(String message) {
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

	private void getDevicesFromJSON() {

		JSONObject jsonObj = null;
		JSONArray jArray = null;

		try {

			jsonObj = new JSONObject(getIntent().getStringExtra("jsonDevices"));
			jArray = jsonObj.getJSONArray("lightList");

			for (int i = 0; i < jArray.length(); i++) {

				JSONObject json_data;

				json_data = jArray.getJSONObject(i);
				Light light_single = new Light(json_data.getInt("id"),
						json_data.getBoolean("power"),
						json_data.getString("room"));

				lightList.add(light_single);
			}

			jArray = jsonObj.getJSONArray("boilerList");

			for (int i = 0; i < jArray.length(); i++) {

				JSONObject json_data;

				json_data = jArray.getJSONObject(i);
				Boiler boiler_single = new Boiler(json_data.getInt("id"),
						json_data.getBoolean("power"),
						json_data.getString("room"));

				boilerList.add(boiler_single);
			}

			jArray = jsonObj.getJSONArray("temperatureList");

			for (int i = 0; i < jArray.length(); i++) {

				JSONObject json_data;

				json_data = jArray.getJSONObject(i);
				Temperature light_single = new Temperature(
						json_data.getInt("id"),
						json_data.getInt("temperature"),
						json_data.getString("room"));

				temperatureList.add(light_single);
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
		
		drawableLeft.add(R.drawable.tv);
		allDevices.add("TV");
		drawableRight.add(R.drawable.remote);

		allDevices.add("Aircondition");
		drawableLeft.add(R.drawable.aircondition);
		drawableRight.add(R.drawable.remote);

		allDevices.add("Window blinds");
		drawableLeft.add(R.drawable.blinds);
		drawableRight.add(R.drawable.list_more);

		allDevices.add("Cameras");
		drawableLeft.add(R.drawable.camera);
		drawableRight.add(R.drawable.watch);

		adapter = new CustomListAdapter(this, allDevices, drawableLeft,
				drawableRight);
		setListAdapter(adapter);

		ListView list = getListView();
		ColorDrawable blue = new ColorDrawable(this.getResources().getColor(
				R.color.blue));
		list.setDivider(blue);
		list.setDividerHeight(1);
		list.setBackgroundColor(getResources().getColor(R.color.grey));

	}

}
