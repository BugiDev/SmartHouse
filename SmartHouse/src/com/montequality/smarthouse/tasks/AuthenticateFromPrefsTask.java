package com.montequality.smarthouse.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.AsyncTask;

import com.montequality.smarthouse.HomeActivity;
import com.montequality.smarthouse.LoginActivity;
import com.montequality.smarthouse.R;
import com.montequality.smarthouse.SplashScreenActivity;
import com.montequality.smarthouse.util.HTTPHelper;

public class AuthenticateFromPrefsTask extends AsyncTask<Void, Boolean, Boolean> {

    public SplashScreenActivity activity;

    private String username = "";
    private String pass = "";

    public AuthenticateFromPrefsTask(SplashScreenActivity activity, String username, String password) {
        this.activity = activity;
        this.username = username;
        this.pass = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", pass));

        HTTPHelper httpHelper = new HTTPHelper("hostURL", "authenticateMethod", nameValuePairs, activity);

        if (!httpHelper.executeHelper().equalsIgnoreCase("-1")) {
            activity.getSharedPrefs().getEditor().putString("userID", httpHelper.executeHelper());
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onPostExecute(final Boolean success) {

        activity.finish();

        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        if (success) {

            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCancelled() {
        activity.finish();
    }

}
