package com.montequality.smarthouse.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
	
	private Activity activity;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	public SharedPrefs(Activity activity) {
		this.setActivity(activity);
		setPreferences(activity.getSharedPreferences("smartHouse_auth", Context.MODE_PRIVATE));
		setEditor(getPreferences().edit());
	}

	public SharedPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(SharedPreferences preferences) {
		this.preferences = preferences;
	}

	public SharedPreferences.Editor getEditor() {
		return editor;
	}

	public void setEditor(SharedPreferences.Editor editor) {
		this.editor = editor;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
