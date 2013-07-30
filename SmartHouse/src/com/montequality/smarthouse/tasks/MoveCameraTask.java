package com.montequality.smarthouse.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class MoveCameraTask extends AsyncTask<Void, Void, Void>{

    String path;
    String direction;
    String cameraHostURL;
    
    public MoveCameraTask(String direction, String cameraHostURL) {
        this.direction = direction;
        this.cameraHostURL = cameraHostURL;
    }
    
    @Override
    protected Void doInBackground(Void... params) {
        setPath(cameraHostURL);
        try {
            
            URI uri = new URI(path);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri);

            httpclient.execute(httpGet);


        } // TODO: register the new account here.
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; 
        
    }
    
    private void setPath(String cameraHostURL){
        if(direction.equalsIgnoreCase("up")){
            path =  cameraHostURL+ "decoder_control.cgi?command=0&degree=20&onestep=1&user=admin&pwd=";
        }else  if(direction.equalsIgnoreCase("down")){
            path =  cameraHostURL+ "decoder_control.cgi?command=2&degree=20&onestep=1&user=admin&pwd=";
        }else  if(direction.equalsIgnoreCase("left")){
            path =  cameraHostURL+ "decoder_control.cgi?command=6&degree=20&onestep=1&user=admin&pwd=";
        }else  if(direction.equalsIgnoreCase("right")){
            path =  cameraHostURL+ "decoder_control.cgi?command=4&degree=20&onestep=1&user=admin&pwd=";
        }
    }

}
