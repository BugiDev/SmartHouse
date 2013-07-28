package com.montequality.smarthouse.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.montequality.smarthouse.HomeActivity;
import com.montequality.smarthouse.MainDevicesListActivity;
import com.montequality.smarthouse.util.HTTPHelper;

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

        int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);

        activity.mHomeStatusView.setVisibility(View.VISIBLE);
        activity.mHomeStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activity.mHomeStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });

        activity.mHomeView.setVisibility(View.VISIBLE);
        activity.mHomeView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activity.mHomeView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

    }

    @Override
    protected String doInBackground(Void... params) {
        userID = Integer.parseInt(activity.getSharedPrefs().getPreferences().getString("userID", "0"));

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("userID", Integer.toString(userID)));
        
        HTTPHelper httpHelper = new HTTPHelper("hostURL", "getDevicesMethod", nameValuePairs, activity);
        return httpHelper.executeHelper();

    }

    @SuppressLint("NewApi")
    @Override
    protected void onPostExecute(String params) {

        final boolean show = true;

        int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);

        activity.mHomeView.setVisibility(View.VISIBLE);
        activity.mHomeView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activity.mHomeView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });

        activity.mHomeStatusView.setVisibility(View.VISIBLE);
        activity.mHomeStatusView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activity.mHomeStatusView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

        Intent intent = new Intent(activity, MainDevicesListActivity.class);
        intent.putExtra("jsonDevices", params);
        activity.startActivity(intent);
    }

}
