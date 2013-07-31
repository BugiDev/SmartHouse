package com.montequality.smarthouse.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.montequality.smarthouse.util.HTTPHelper;

import android.app.Activity;
import android.os.AsyncTask;

public class GetRemoteParamsTask  extends AsyncTask<Void, Void, String> {
	
    private int deviceID;
    private String actionType;
    private Activity activity;
    
    public GetRemoteParamsTask() {
		// TODO Auto-generated constructor stub
	}
	
    public GetRemoteParamsTask(int deviceID, String actionType, Activity activity) {
		this.deviceID = deviceID;
		this.actionType = actionType;
		this.activity = activity;
	}
    
	@Override
    protected String doInBackground(Void... arg0) {

	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("deviceID", Integer.toString(deviceID)));
	HTTPHelper httpHelper = null;

	if (actionType.equalsIgnoreCase("volume")) {
	    httpHelper = new HTTPHelper("hostURL", "getVolumeMethod", nameValuePairs, activity);
	} else if (actionType.equalsIgnoreCase("channel")) {
	    httpHelper = new HTTPHelper("hostURL", "getChannelMethod", nameValuePairs, activity);
	} else if (actionType.equalsIgnoreCase("mode")) {
	    httpHelper = new HTTPHelper("hostURL", "getModeMethod", nameValuePairs, activity);
	} else if (actionType.equalsIgnoreCase("temperature")) {
	    httpHelper = new HTTPHelper("hostURL", "getTemperatureMethod", nameValuePairs, activity);
	}

	return httpHelper.executeHelper();
    }

}
