package com.montequality.smarthouse.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.montequality.smarthouse.R;

public class CustomListAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;
	private final int[] drawableIntLeft;
	private final int[] drawableIntRight;

	public CustomListAdapter(Context context, String[] values, int[] drawableIntLeft, int[] drawableIntRight) {
		super(context, R.layout.custom_list_row, values);
		this.context = context;
		this.values = values;
		this.drawableIntLeft = drawableIntLeft;
		this.drawableIntRight = drawableIntRight;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.custom_list_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.list_text);
		ImageView imageViewLeft = (ImageView) rowView.findViewById(R.id.list_icon_left);
		ImageView imageViewRight = (ImageView) rowView.findViewById(R.id.list_icon_right);
		textView.setText(values[position]);
		
		if(values[position].equals("Lights")){
			imageViewLeft.setImageResource(R.drawable.lightbuble_left);
			imageViewRight.setImageResource(R.drawable.lightbuble_right_off);
		}else{
			imageViewLeft.setImageResource(drawableIntLeft[position]);
			imageViewRight.setImageResource(drawableIntRight[position]);
		}
			
		return rowView;
	}

}
