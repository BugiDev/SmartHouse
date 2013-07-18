package com.montequality.smarthouse.util;

import java.util.List;

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
	private final List<String> values;
	private List<Integer> drawableIntLeft;
	private List<Integer> drawableIntRight;

	public CustomListAdapter(Context context, List<String> values,
			 List<Integer> drawableIntLeft,  List<Integer> drawableIntRight) {
		super(context, R.layout.custom_list_row, values);
		this.context = context;
		this.values = values;
		this.setDrawableIntLeft(drawableIntLeft);
		this.setDrawableIntRight(drawableIntRight);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater
				.inflate(R.layout.custom_list_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.list_text);
		ImageView imageViewLeft = (ImageView) rowView
				.findViewById(R.id.list_icon_left);
		ImageView imageViewRight = (ImageView) rowView
				.findViewById(R.id.list_icon_right);
		textView.setText(values.get(position));

		imageViewLeft.setImageResource(getDrawableIntLeft().get(position));
		imageViewRight.setImageResource(getDrawableIntRight().get(position));

		return rowView;
	}

	public  List<Integer> getDrawableIntLeft() {
		return drawableIntLeft;
	}

	public void setDrawableIntLeft( List<Integer> drawableIntLeft) {
		this.drawableIntLeft = drawableIntLeft;
	}

	public  List<Integer> getDrawableIntRight() {
		return drawableIntRight;
	}

	public void setDrawableIntRight( List<Integer> drawableIntRight) {
		this.drawableIntRight = drawableIntRight;
	}

}
