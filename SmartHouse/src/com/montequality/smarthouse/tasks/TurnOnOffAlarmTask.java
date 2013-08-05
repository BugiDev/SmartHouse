package com.montequality.smarthouse.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.montequality.smarthouse.R;
import com.montequality.smarthouse.util.CustomListAdapter;
import com.montequality.smarthouse.util.HTTPHelper;

public class TurnOnOffAlarmTask extends AsyncTask<Void, Void, String> {

    String path;
    String cameraHostURL;
    int deviceID;
    String alarmOnOff;

    public Activity activity;
    private CustomListAdapter adapter;
    private int positionInList;

    Dialog dialog = null;

    public TurnOnOffAlarmTask(int deviceID, String cameraHostURL, int positionInList, CustomListAdapter adapter, Activity activity) {
        this.activity = activity;
        this.cameraHostURL = cameraHostURL;
        this.adapter = adapter;
        this.positionInList = positionInList;
        this.deviceID = deviceID;
    }

    @Override
    protected void onPreExecute() {
        dialog = new Dialog(activity, R.style.custom_dialog);
        prepDialog();
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("deviceID", Integer.toString(deviceID)));

        try {

            HTTPHelper httpHelper = new HTTPHelper("hostURL", "cameraAlarmOnOffMethod", nameValuePairs, activity);
            alarmOnOff = httpHelper.executeHelper();

            setPath(cameraHostURL);
            
            URI uri = new URI(path);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri);

            httpclient.execute(httpGet);

            if (alarmOnOff.equalsIgnoreCase("true")) {
                return "true";
            } else {
                return "false";
            }

        } // TODO: register the new account here.
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "sranje";
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "sranje";
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "sranje";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "sranje";
        }

    }

    @Override
    protected void onPostExecute(String params) {

        String message;
        List<Integer> tempDrawRight = adapter.getDrawableIntRight();

        if (params.equalsIgnoreCase("true")) {
            message = "Alarm ON";
            tempDrawRight.set(positionInList, R.drawable.led_on);
            adapter.setDrawableIntRight(tempDrawRight);
        } else if (params.equalsIgnoreCase("false")) {
            message = "Alarm OFF";
            tempDrawRight.set(positionInList, R.drawable.led_off);
            adapter.setDrawableIntRight(tempDrawRight);
        } else {
            message = "Error with alarm";
        }
        adapter.notifyDataSetChanged();
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

    private void setPath(String cameraHostURL) {
        if (alarmOnOff.equalsIgnoreCase("true")) {
            path = cameraHostURL + "set_alarm.cgi?motion_armed=1";
        } else {
            path = cameraHostURL + "set_alarm.cgi?motion_armed=0";
        }
    }

    public void prepDialog() {

        dialog.setContentView(R.layout.custom_dialog_action);
        dialog.setTitle(R.string.performing_action_title);

    }

}
