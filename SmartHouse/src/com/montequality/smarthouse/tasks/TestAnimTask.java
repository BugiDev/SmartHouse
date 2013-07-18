package com.montequality.smarthouse.tasks;

import com.montequality.smarthouse.HomeActivity;
import com.montequality.smarthouse.MainDevicesListActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

public class TestAnimTask extends AsyncTask<Void, Void, Boolean> {
	
	public HomeActivity activity;
	
	public TestAnimTask(HomeActivity activity) {
		this.activity = activity;
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onPreExecute(){
		final boolean show = true;
		
		int shortAnimTime = activity.getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		activity.mHomeStatusView.setVisibility(View.VISIBLE);
		activity.mHomeStatusView.animate().setDuration(shortAnimTime)
				.alpha(show ? 1 : 0)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						activity.mHomeStatusView.setVisibility(show ? View.VISIBLE
								: View.GONE);
					}
				});

		activity.mHomeView.setVisibility(View.VISIBLE);
		activity.mHomeView.animate().setDuration(shortAnimTime)
				.alpha(show ? 0 : 1)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						activity.mHomeView.setVisibility(show ? View.GONE
								: View.VISIBLE);
					}
				});
		
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onPostExecute(Boolean params){
		
		final boolean show = true;
		
		int shortAnimTime = activity.getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		activity.mHomeView.setVisibility(View.VISIBLE);
		activity.mHomeView.animate().setDuration(shortAnimTime)
				.alpha(show ? 1 : 0)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						activity.mHomeView.setVisibility(show ? View.VISIBLE
								: View.GONE);
					}
				});

		activity.mHomeStatusView.setVisibility(View.VISIBLE);
		activity.mHomeStatusView.animate().setDuration(shortAnimTime)
				.alpha(show ? 0 : 1)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						activity.mHomeStatusView.setVisibility(show ? View.GONE
								: View.VISIBLE);
					}
				});
		
		Intent intent = new Intent(activity,
				MainDevicesListActivity.class);
		activity.startActivity(intent);
		
	}

}
