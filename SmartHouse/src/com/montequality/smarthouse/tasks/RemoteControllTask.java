package com.montequality.smarthouse.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.widget.TextView;

import com.montequality.smarthouse.R;
import com.montequality.smarthouse.util.HTTPHelper;

public class RemoteControllTask extends AsyncTask<Void, Void, String> {

    public Activity activity;
    private int deviceID;
    private String actionType;
    private TextView textView;
    private int changingValue;

    Dialog dialog = null;

    public RemoteControllTask() {
	// TODO Auto-generated constructor stub
    }

    public RemoteControllTask(Activity activity, int deviceID, String actionType, TextView textView, int changingValue) {
	this.activity = activity;
	this.deviceID = deviceID;
	this.actionType = actionType;
	this.textView = textView;
	this.changingValue = changingValue;
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
	HTTPHelper httpHelper = null;

	if (actionType.equalsIgnoreCase("volumeUp")) {
	    httpHelper = new HTTPHelper("hostURL", "volumeUpMethod", nameValuePairs, activity);
	} else if (actionType.equalsIgnoreCase("volumeDown")) {
	    httpHelper = new HTTPHelper("hostURL", "volumeDownMethod", nameValuePairs, activity);
	} else if (actionType.equalsIgnoreCase("channelUp")) {
	    httpHelper = new HTTPHelper("hostURL", "channelUpMethod", nameValuePairs, activity);
	} else if (actionType.equalsIgnoreCase("channelDown")) {
	    httpHelper = new HTTPHelper("hostURL", "channelDownMethod", nameValuePairs, activity);
	} else if (actionType.equalsIgnoreCase("temperatureUp")) {
	    httpHelper = new HTTPHelper("hostURL", "temperatureUpMethod", nameValuePairs, activity);
	} else if (actionType.equalsIgnoreCase("temperatureDown")) {
	    httpHelper = new HTTPHelper("hostURL", "temperatureDownMethod", nameValuePairs, activity);
	}

	return httpHelper.executeHelper();
    }

    @Override
    protected void onPostExecute(String params) {
	
	if (!params.equalsIgnoreCase("sranje")) {
	    if (actionType.equalsIgnoreCase("volumeUp")) {
		turnVolumeUp();
	    } else if (actionType.equalsIgnoreCase("volumeDown")) {
		turnVolumeDown();
	    } else if (actionType.equalsIgnoreCase("channelUp")) {
		turnChannelUp();
	    } else if (actionType.equalsIgnoreCase("channelDown")) {
		turnChannelDown();
	    } else if (actionType.equalsIgnoreCase("temperatureUp")) {
		turnTemperatureUp();
	    } else if (actionType.equalsIgnoreCase("temperatureDown")) {
		turnTemperatureDown();
	    }
	}
	
	dialog.dismiss();

    }

    private void turnVolumeUp() {
	changingValue++;
	textView.setText(activity.getResources().getString(R.string.remote_tv_volume) + " " + changingValue);
    }

    private void turnVolumeDown() {
	changingValue--;
	textView.setText(activity.getResources().getString(R.string.remote_tv_volume) + " " + changingValue);
    }

    private void turnChannelUp() {
	changingValue++;
	textView.setText(activity.getResources().getString(R.string.remote_tv_channel) + " " + changingValue);
    }

    private void turnChannelDown() {
	changingValue--;
	textView.setText(activity.getResources().getString(R.string.remote_tv_channel) + " " + changingValue);
    }

    private void turnTemperatureUp() {
	changingValue++;
	textView.setText(activity.getResources().getString(R.string.remote_air_temperature) + " " + changingValue);
    }

    private void turnTemperatureDown() {
	changingValue--;
	textView.setText(activity.getResources().getString(R.string.remote_air_temperature) + " " + changingValue);
    }

    public void prepDialog() {

	dialog.setContentView(R.layout.custom_dialog_action);
	dialog.setTitle(R.string.performing_action_title);

    }

}
