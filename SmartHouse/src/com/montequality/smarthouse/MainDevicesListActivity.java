package com.montequality.smarthouse;

import android.app.ListActivity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.util.CustomListAdapter;

public class MainDevicesListActivity extends ListActivity {

	boolean svetlo = false;
	boolean boiler = false;
	boolean proba = false;
	CustomListAdapter adapter;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		String[] values = new String[] { "Lights", "TV", "Boiler",
				"Aircondition", "Window blinds", "Cameras" };
		int[] drawableLeft = new int[] { R.drawable.lightbuble_left,
				R.drawable.tv, R.drawable.boiler, R.drawable.aircondition,
				R.drawable.blinds, R.drawable.camera };
		int[] drawableRight = new int[] { R.drawable.lightbuble_right_off,
				R.drawable.remote, R.drawable.led_off, R.drawable.remote,
				R.drawable.list_more, R.drawable.watch };
		adapter = new CustomListAdapter(this, values,
				drawableLeft, drawableRight);
		setListAdapter(adapter);

		ListView list = getListView();
		ColorDrawable blue = new ColorDrawable(this.getResources().getColor(
				R.color.blue));
		list.setDivider(blue);
		list.setDividerHeight(1);
		list.setBackgroundColor(getResources().getColor(R.color.grey));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		String item = (String) getListAdapter().getItem(position);
		
		int[] tempDrawRight = adapter.getDrawableIntRight();

		switch (position) {
		case 0: {
			if (svetlo) {
				tempDrawRight[0] =  R.drawable.lightbuble_right_off;
				adapter.setDrawableIntRight(tempDrawRight);
				showToastMessage(item + " off");
				svetlo = false;
			} else {
				tempDrawRight[0] =  R.drawable.lightbuble_right_on;
				adapter.setDrawableIntRight(tempDrawRight);
				showToastMessage(item + " on");
				svetlo = true;
			}
			break;
		}
		case 2: {
			if (boiler) {
				tempDrawRight[2] =  R.drawable.led_off;
				adapter.setDrawableIntRight(tempDrawRight);
				showToastMessage(item + " off");
				boiler = false;
			} else {
				tempDrawRight[2] =  R.drawable.led_on;
				adapter.setDrawableIntRight(tempDrawRight);
				showToastMessage(item + " on");
				boiler = true;
			}
			break;
		}
		default:
			showToastMessage(item + " selected");
			break;
		}
		
		adapter.notifyDataSetChanged();

	}
	
	private void showToastMessage(String message){
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) findViewById(R.id.toast_layout));
		((TextView) layout.findViewById(R.id.custom_toast_text))
				.setText(message);
		Toast toast = new Toast(getBaseContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

}
