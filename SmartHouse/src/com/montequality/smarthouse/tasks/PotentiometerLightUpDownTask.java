package com.montequality.smarthouse.tasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.R;
import com.montequality.smarthouse.util.HTTPHelper;

public class PotentiometerLightUpDownTask extends AsyncTask<Void, Void, String> {

    public Activity activity;
    private int deviceID;
    private String room;
    private String direction;
    private TextView percentText;
    private ImageView potLightImage;

    Dialog dialog = null;

    public PotentiometerLightUpDownTask() {
        // TODO Auto-generated constructor stub
    }

    public PotentiometerLightUpDownTask(Activity activity, int deviceID, String room, String direction, TextView percentText, ImageView potLightImage) {
        this.activity = activity;
        this.deviceID = deviceID;
        this.room = room;
        this.direction = direction;
        this.percentText = percentText;
        this.potLightImage = potLightImage;
    }

    @Override
    protected void onPreExecute() {
        dialog = new Dialog(activity, R.style.custom_dialog);
        prepDialog();
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... arg0) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("deviceID", Integer.toString(deviceID)));

        HTTPHelper httpHelper;

        if (direction.equalsIgnoreCase("up")) {
            httpHelper = new HTTPHelper("hostURL", "potLightUpMethod", nameValuePairs, activity);
        } else {
            httpHelper = new HTTPHelper("hostURL", "potLightDownMethod", nameValuePairs, activity);
        }

        return httpHelper.executeHelper();
    }

    @Override
    protected void onPostExecute(String params) {

        if (params.equalsIgnoreCase("0.0")) {
            potLightImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.lightbuble_right_off));
        } else if (params.equalsIgnoreCase("0.25")) {
            potLightImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.lightbuble_25));
        } else if (params.equalsIgnoreCase("0.5")) {
            potLightImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.lightbuble_50));
        } else if (params.equalsIgnoreCase("0.75")) {
            potLightImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.lightbuble_75));
        } else {
            potLightImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.lightbuble_right_off));
        }
        
        if(!params.equalsIgnoreCase("sranje")){
            percentText.setText(params + "%");  
        }
        
        String message = "Light in room".concat(": " + room).concat(" to ").concat(params);
        showToastMessage(message);
        dialog.dismiss();

    }

    private void showToastMessage(String message) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout));
        ((TextView) layout.findViewById(R.id.custom_toast_text)).setText(message);
        Toast toast = new Toast(activity.getBaseContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void prepDialog() {

        dialog.setContentView(R.layout.custom_dialog_action);
        dialog.setTitle(R.string.performing_action_title);

    }

}
