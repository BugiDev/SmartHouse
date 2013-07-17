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

import com.montequality.smarthouse.HomeActivity;
import com.montequality.smarthouse.LoginActivity;
import com.montequality.smarthouse.R;
import com.montequality.smarthouse.util.PropertyReader;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class AuthenticateTask extends AsyncTask<Void, Boolean, Boolean> {

	public LoginActivity activity;
	
	private String username = "";
	private String pass = ""; 

	public AuthenticateTask(LoginActivity activity) {
		this.activity = activity;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {
			InputStream inputStream = activity.getResources().getAssets().open("config.properties");
			Properties properties = PropertyReader.getInstance().readConfigProperties(inputStream);
			
			String hostURL = properties.getProperty("hostURL");
			String methodName = properties.getProperty("authenticateMethod");
			
			username = activity.mUsernameView.getText().toString();
			pass = activity.mPasswordView.getText().toString();
			
			URI uri = new URI(hostURL.concat(methodName));
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);
			// Log.d("response", "WORKING");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", pass));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			InputStream ips  = response.getEntity().getContent();
	        BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
	        
	        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND)
	        {
	            throw new Exception("Wrong username or password");
	        }else{
	        	StringBuilder sb = new StringBuilder();
		        String s;
		        while(true )
		        {
		            s = buf.readLine();
		            if(s==null || s.length()==0)
		                break;
		            sb.append(s);

		        }
		        buf.close();
		        ips.close();
		        activity.editor.putString("id", sb.toString());
		        Log.d("respones", "json response is:" + sb.toString());
	        }
	        
			
			return true;
		} // TODO: register the new account here.
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Override
	protected void onPostExecute(final Boolean success) {

		activity.showProgress(false);
		activity.mAuthTask = null;

		if (success) {

			if (activity.saveCheck.isChecked()) {
				activity.editor.putString("username", activity.mUsernameView.getText().toString());
				activity.editor.putString("password", activity.mPasswordView.getText().toString());
				activity.editor.commit();
			}

			Intent intent = new Intent(activity, HomeActivity.class);
			activity.startActivity(intent);
		} else {
			activity.mUsernameView
					.setError(activity.getString(R.string.error_username_or_password));
			activity.mUsernameView.requestFocus();
		}
	}

	@Override
	protected void onCancelled() {
		activity.mAuthTask = null;
		activity.showProgress(false);
	}

}
