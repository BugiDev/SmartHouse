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
import com.montequality.smarthouse.util.CustomListAdapter;

public class MainDevicesListActivity extends ListActivity {

	boolean svetlo = false;
	boolean boiler = false;

	CustomListAdapter adapter;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	List<Light> lightList = new ArrayList<Light>();
	List<Boiler> boilerList = new ArrayList<Boiler>();
	List<Temperature> temperatureList = new ArrayList<Temperature>();
	
	List<String> allDevices = new ArrayList<String>();
	List<Integer> drawableLeft = new ArrayList<Integer>();
	List<Integer> drawableRight = new ArrayList<Integer>();

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		getDevicesFromJSON();
		
		allDevices.add("Lights");
		allDevices.add("TV");
		allDevices.add("Boiler");
		allDevices.add("Aircondition");
		allDevices.add("Window blinds");
		allDevices.add("Cameras");
		
		drawableLeft.add(R.drawable.lightbuble_left);
		drawableLeft.add(R.drawable.tv);
		drawableLeft.add(R.drawable.boiler);
		drawableLeft.add(R.drawable.aircondition);
		drawableLeft.add(R.drawable.blinds);
		drawableLeft.add(R.drawable.camera);
		
		drawableRight.add(R.drawable.list_more);
		drawableRight.add(R.drawable.remote);
		drawableRight.add(R.drawable.led_off);
		drawableRight.add(R.drawable.remote);
		drawableRight.add(R.drawable.list_more);
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

		preferences = getSharedPreferences("smartHouse_auth",
				Context.MODE_PRIVATE);
		editor = preferences.edit();

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		String item = (String) getListAdapter().getItem(position);

		List<Integer> tempDrawRight = adapter.getDrawableIntRight();

		switch (position) {
		case 0: {
			Intent intent = new Intent(this, LightsList.class);
			intent.putExtra("jsonDevices", getIntent().getStringExtra("jsonDevices"));
			startActivity(intent);
			break;
		}
		case 2: {
			if (boiler) {
				tempDrawRight.set(2, R.drawable.led_off);
				adapter.setDrawableIntRight(tempDrawRight);
				showToastMessage(item + " off");
				boiler = false;
			} else {
				tempDrawRight.set(2, R.drawable.led_on);
				adapter.setDrawableIntRight(tempDrawRight);
				showToastMessage(item + " on");
				boiler = true;
			}
			break;
		}
		default:
			showToastMessage(item + " selected");
			break;
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
				Light light_single = new Light(
						json_data.getInt("id"),
						json_data.getBoolean("power"),
						json_data.getString("room"));
				
				lightList.add(light_single);
			}
			
			jArray = jsonObj.getJSONArray("boilerList");

			for (int i = 0; i < jArray.length(); i++) {

				JSONObject json_data;

				json_data = jArray.getJSONObject(i);
				Boiler boiler_single = new Boiler(
						json_data.getInt("id"),
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

}
