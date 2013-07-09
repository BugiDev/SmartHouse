package com.montequality.smarthouse;

import android.app.ListActivity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.montequality.smarthouse.util.CustomListAdapter;

public class MainDevicesListActivity extends ListActivity {

	boolean svetlo = false;
	
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    String[] values = new String[] { "Lights", "TV", "Boiler",
	            "Aircondition", "Window blinds", "Cameras"};
	    int[] drawableLeft = new int[] {R.drawable.lightbuble_left,R.drawable.tv, R.drawable.boiler, R.drawable.aircondition, R.drawable.blinds, R.drawable.camera};
	    int[] drawableRight = new int[] {R.drawable.lightbuble_right_off,R.drawable.remote, R.drawable.remote, R.drawable.remote, R.drawable.list_more, R.drawable.watch};
	        CustomListAdapter adapter = new CustomListAdapter(this, values, drawableLeft, drawableRight);
	        setListAdapter(adapter);
	        
	        ListView list = getListView();
	        ColorDrawable blue = new ColorDrawable(this.getResources().getColor(R.color.blue));
	        list.setDivider(blue);
	        list.setDividerHeight(1);
	        list.setBackgroundColor(getResources().getColor(R.color.grey));
	  }

	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	   
	    ImageView imageView2 = (ImageView) findViewById(R.id.list_icon_right);
	    if(item.equals("Lights")){
	    	if(svetlo){
	    		imageView2.setImageResource(R.drawable.lightbuble_right_off);
	    		Toast.makeText(this, item + " off", Toast.LENGTH_LONG).show();
	    		svetlo = false;
	    	}else {
	    		imageView2.setImageResource(R.drawable.lightbuble_right_on);
	    		Toast.makeText(this, item + " on", Toast.LENGTH_LONG).show();
	    		svetlo = true;
	    	}
			
		}else {
			 Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		}
	    
	  }

}
