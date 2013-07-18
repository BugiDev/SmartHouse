package com.montequality.smarthouse.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.R;
import com.montequality.smarthouse.util.PropertyReader;

public class OnOffTask extends AsyncTask<Void, Void, String> {

	public Activity activity;
	private int deviceID;
	private String deviceType;
	private String room;
	
	public OnOffTask() {
		// TODO Auto-generated constructor stub
	}
	
	public OnOffTask(Activity activity, int deviceID, String deviceType, String room) {
		this.activity = activity;
		this.deviceID = deviceID;
		this.deviceType = deviceType;
		this.room = room;
	}
	
	@Override
	protected String doInBackground(Void... arg0) {
		try {
			InputStream inputStream = activity.getResources().getAssets()
					.open("config.properties");
			Properties properties = PropertyReader.getInstance()
					.readConfigProperties(inputStream);

			String hostURL = properties.getProperty("hostURL");
			String methodName = properties.getProperty("turnOnOffMethode");

			URI uri = new URI(hostURL.concat(methodName));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("deviceID", Integer
					.toString(deviceID)));
			nameValuePairs.add(new BasicNameValuePair("deviceType", deviceType));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			InputStream ips = response.getEntity().getContent();
			BufferedReader buf = new BufferedReader(new InputStreamReader(ips,
					"UTF-8"));

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				throw new Exception("Sranje");
			} else {
				StringBuilder sb = new StringBuilder();
				String s;
				while (true) {
					s = buf.readLine();
					if (s == null || s.length() == 0)
						break;
					sb.append(s);

				}
				buf.close();
				ips.close();
				Log.d("++++++++++++++++++++ONOFF++++++++++++++++++", sb.toString());
				return sb.toString();
			}
		} // TODO: register the new account here.
		catch (UnsupportedEncodingException e) {
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
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "sranje";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "sranje";
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String params) {
		
		String message;
		
		if(params.equalsIgnoreCase("true")){
			message = activity.getResources().getString(R.string.light_on).concat(room);
		}else if(params.equalsIgnoreCase("false")){
			message = activity.getResources().getString(R.string.light_off).concat(room);
		}else{
			message = activity.getResources().getString(R.string.light_error).concat(room);
		}

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

}
