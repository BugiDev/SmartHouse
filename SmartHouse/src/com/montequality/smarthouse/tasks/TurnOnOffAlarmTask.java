package com.montequality.smarthouse.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.R;
import com.montequality.smarthouse.util.CustomListAdapter;

public class TurnOnOffAlarmTask extends AsyncTask<Void, Void, String> {

	String path;
	boolean onOff;
	String cameraHostURL;
	
	public Activity activity;
    private CustomListAdapter adapter;
    private int positionInList;

    Dialog dialog = null;

	public TurnOnOffAlarmTask(boolean onOff, String cameraHostURL, int positionInList, CustomListAdapter adapter, Activity activity) {
		this.activity = activity;
		this.onOff = onOff;
		this.cameraHostURL = cameraHostURL;
		this.adapter = adapter;
		this.positionInList = positionInList;
	}
	
	@Override
    protected void onPreExecute() {
	dialog = new Dialog(activity, R.style.custom_dialog);
	prepDialog();
	dialog.show();
    }

	@Override
	protected String doInBackground(Void... params) {
		setPath(cameraHostURL);
		try {

			URI uri = new URI(path);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(uri);

			httpclient.execute(httpGet);
			return "true";
		} // TODO: register the new account here.
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "sranje";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "sranje";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "sranje";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "sranje";
		}

	}

	@Override
	protected void onPostExecute(String params) {

		String message;
		List<Integer> tempDrawRight = adapter.getDrawableIntRight();

		if (params.equalsIgnoreCase("true")) {
			if(onOff){
				message = "Alarm ON";
			}else{
				message = "Alarm OFF";
			}
		} else {
			message = "Error with alarm";
		}

		if (tempDrawRight.get(positionInList).equals(R.drawable.led_on)) {

			tempDrawRight.set(positionInList, R.drawable.led_off);
			adapter.setDrawableIntRight(tempDrawRight);

		} else {

			tempDrawRight.set(positionInList, R.drawable.led_on);
			adapter.setDrawableIntRight(tempDrawRight);

		}

		showToastMessage(message);

		dialog.dismiss();

	}

	private void showToastMessage(String message) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) activity.findViewById(R.id.toast_layout));
		((TextView) layout.findViewById(R.id.custom_toast_text))
				.setText(message);
		Toast toast = new Toast(activity.getBaseContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	private void setPath(String cameraHostURL) {
		if (onOff) {
			path = cameraHostURL + "set_alarm.cgi?motion_armed=1";
		} else {
			path = cameraHostURL + "set_alarm.cgi?motion_armed=0";
		}
	}
	
	public void prepDialog() {

		dialog.setContentView(R.layout.custom_dialog_action);
		dialog.setTitle(R.string.performing_action_title);

	    }

}
