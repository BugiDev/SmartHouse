package com.montequality.smarthouse.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.R;
import com.montequality.smarthouse.util.CustomListAdapter;
import com.montequality.smarthouse.util.HTTPHelper;

public class OnOffTask extends AsyncTask<Void, Void, String> {

    public Activity activity;
    private int deviceID;
    private String deviceType;
    private String room;
    private CustomListAdapter adapter;
    private int positionInList;
    private ImageButton powerBtn;

    Dialog dialog = null;

    public OnOffTask() {
	// TODO Auto-generated constructor stub
    }

    public OnOffTask(Activity activity, int deviceID, String deviceType, String room, CustomListAdapter adapter, int positionInList) {
	this.activity = activity;
	this.deviceID = deviceID;
	this.deviceType = deviceType;
	this.room = room;
	this.adapter = adapter;
	this.positionInList = positionInList;
    }

    public OnOffTask(Activity activity, int deviceID, String deviceType, String room, ImageButton powerBtn) {
	this.activity = activity;
	this.deviceID = deviceID;
	this.deviceType = deviceType;
	this.room = room;
	this.powerBtn = powerBtn;
    }

    @Override
    protected void onPreExecute() {
	dialog = new Dialog(activity, R.style.custom_dialog);
	prepDialog();
	dialog.show();
    }

    @Override
    protected String doInBackground(Void... arg0) {

	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("deviceID", Integer.toString(deviceID)));
	nameValuePairs.add(new BasicNameValuePair("deviceType", deviceType));

	HTTPHelper httpHelper = new HTTPHelper("hostURL", "turnOnOffMethode", nameValuePairs, activity);
	return httpHelper.executeHelper();
    }

    @Override
    protected void onPostExecute(String params) {

	if (deviceType.equalsIgnoreCase("Light")) {
	    changeLight(params);
	    adapter.notifyDataSetChanged();
	} else if (deviceType.equalsIgnoreCase("Boiler")) {
	    changeBoiler(params);
	    adapter.notifyDataSetChanged();
	} else if (deviceType.equalsIgnoreCase("Aircondition")) {
	    changeAircondition(params);
	}else if (deviceType.equalsIgnoreCase("TV")) {
	    changeTV(params);
	}
	
	dialog.dismiss();

    }

    private void showToastMessage(String message) {
	LayoutInflater inflater = activity.getLayoutInflater();
	View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout));
	((TextView) layout.findViewById(R.id.custom_toast_text)).setText(message);
	Toast toast = new Toast(activity.getBaseContext());
	toast.setDuration(Toast.LENGTH_SHORT);
	toast.setView(layout);
	toast.show();
    }

    private void changeLight(String params) {

	String message;
	List<Integer> tempDrawRight = adapter.getDrawableIntRight();

	if (params.equalsIgnoreCase("true")) {
	    message = activity.getResources().getString(R.string.light_on).concat(": " + room);

	} else if (params.equalsIgnoreCase("false")) {
	    message = activity.getResources().getString(R.string.light_off).concat(": " + room);
	} else {
	    message = activity.getResources().getString(R.string.light_error).concat(": " + room);
	}

	if (tempDrawRight.get(positionInList).equals(R.drawable.lightbuble_right_on)) {

	    tempDrawRight.set(positionInList, R.drawable.lightbuble_right_off);
	    adapter.setDrawableIntRight(tempDrawRight);

	} else {

	    tempDrawRight.set(positionInList, R.drawable.lightbuble_right_on);
	    adapter.setDrawableIntRight(tempDrawRight);

	}

	showToastMessage(message);

    }

    private void changeBoiler(String params) {

	String message;
	List<Integer> tempDrawRight = adapter.getDrawableIntRight();

	if (params.equalsIgnoreCase("true")) {
	    message = activity.getResources().getString(R.string.light_on).concat(": " + room);

	} else if (params.equalsIgnoreCase("false")) {
	    message = activity.getResources().getString(R.string.light_off).concat(": " + room);
	} else {
	    message = activity.getResources().getString(R.string.light_error).concat(": " + room);
	}

	if (tempDrawRight.get(positionInList).equals(R.drawable.led_on)) {

	    tempDrawRight.set(positionInList, R.drawable.led_off);
	    adapter.setDrawableIntRight(tempDrawRight);

	} else {

	    tempDrawRight.set(positionInList, R.drawable.led_on);
	    adapter.setDrawableIntRight(tempDrawRight);

	}

	showToastMessage(message);
    }

    private void changeAircondition(String params) {

	String message;

	if (params.equalsIgnoreCase("true")) {
	    message = activity.getResources().getString(R.string.remote_air_power_on).concat(": " + room);
	    powerBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.led_on));

	} else if (params.equalsIgnoreCase("false")) {
	    message = activity.getResources().getString(R.string.remote_air_power_off).concat(": " + room);
	    powerBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.led_off));
	} else {
	    message = activity.getResources().getString(R.string.remote_air_power_error).concat(": " + room);
	}

	showToastMessage(message);
    }
    
    private void changeTV(String params) {

	String message;

	if (params.equalsIgnoreCase("true")) {
	    message = activity.getResources().getString(R.string.remote_tv_power_on).concat(": " + room);
	    powerBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.led_on));

	} else if (params.equalsIgnoreCase("false")) {
	    message = activity.getResources().getString(R.string.remote_tv_power_off).concat(": " + room);
	    powerBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.led_off));
	} else {
	    message = activity.getResources().getString(R.string.remote_tv_power_error).concat(": " + room);
	}

	showToastMessage(message);
    }

    public void prepDialog() {

	dialog.setContentView(R.layout.custom_dialog_action);
	dialog.setTitle(R.string.performing_action_title);

    }

}
