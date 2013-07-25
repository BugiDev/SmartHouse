package com.montequality.smarthouse.tasks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.montequality.smarthouse.LoginActivity;
import com.montequality.smarthouse.R;
import com.montequality.smarthouse.SplashScreenActivity;

public class CheckInternetTask  extends AsyncTask<Void, Void, Boolean> {

	public SplashScreenActivity activity;
	
	public CheckInternetTask(SplashScreenActivity activity) {
		this.activity = activity;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (hasActiveInternetConnection()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean params) {
		
		if(params){
			activity.finish();

			activity.overridePendingTransition(R.anim.fade_in,
					R.anim.fade_out);

			Intent intent = new Intent(activity,
					LoginActivity.class);
			activity.startActivity(intent);
		}else{
			activity.dialog.show();
		}
	}
	
	public boolean hasActiveInternetConnection() {
	    if (isNetworkAvailable()) {
	        try {
	            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	            urlc.setRequestProperty("User-Agent", "Test");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(1500); 
	            urlc.connect();
	            return (urlc.getResponseCode() == 200);
	        } catch (IOException e) {
	            //TODO exception handle
	        }
	    } else {
	    	 return false;
	    }
	    return false;
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
}
