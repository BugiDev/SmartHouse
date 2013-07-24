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

import android.os.AsyncTask;
import com.montequality.smarthouse.HomeActivity;
import com.montequality.smarthouse.MainDevicesListActivity;
import com.montequality.smarthouse.util.PropertyReader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class GetDevicesTask extends AsyncTask<Void, Void, String> {

	public HomeActivity activity;
	private int userID;

	public GetDevicesTask(HomeActivity activity) {
		this.activity = activity;
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPreExecute() {
		final boolean show = true;

		int shortAnimTime = activity.getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		activity.mHomeStatusView.setVisibility(View.VISIBLE);
		activity.mHomeStatusView.animate().setDuration(shortAnimTime)
				.alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						activity.mHomeStatusView
								.setVisibility(show ? View.VISIBLE : View.GONE);
					}
				});

		activity.mHomeView.setVisibility(View.VISIBLE);
		activity.mHomeView.animate().setDuration(shortAnimTime)
				.alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						activity.mHomeView.setVisibility(show ? View.GONE
								: View.VISIBLE);
					}
				});

	}

	@Override
	protected String doInBackground(Void... params) {

		try {
			InputStream inputStream = activity.getResources().getAssets()
					.open("config.properties");
			Properties properties = PropertyReader.getInstance()
					.readConfigProperties(inputStream);

			String hostURL = properties.getProperty("hostURL");
			String methodName = properties.getProperty("getDevicesMethod");

			userID = Integer.parseInt(activity.preferences.getString("userID",
					"0"));

			URI uri = new URI(hostURL.concat(methodName));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("userID", Integer
					.toString(userID)));

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
				Log.d("++++++++++++++++++++PROBA++++++++++++++++++", sb.toString());
				return sb.toString();
			}
		} // TODO: register the new account here.
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void onPostExecute(String params) {

		final boolean show = true;

		int shortAnimTime = activity.getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		activity.mHomeView.setVisibility(View.VISIBLE);
		activity.mHomeView.animate().setDuration(shortAnimTime)
				.alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						activity.mHomeView.setVisibility(show ? View.VISIBLE
								: View.GONE);
					}
				});

		activity.mHomeStatusView.setVisibility(View.VISIBLE);
		activity.mHomeStatusView.animate().setDuration(shortAnimTime)
				.alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						activity.mHomeStatusView.setVisibility(show ? View.GONE
								: View.VISIBLE);
					}
				});

		Intent intent = new Intent(activity, MainDevicesListActivity.class);
		intent.putExtra("jsonDevices", params);
		activity.startActivity(intent);
		

	}

}
