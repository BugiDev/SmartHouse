package com.montequality.smarthouse.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.util.Log;

public class HTTPHelper {

    private String hostURLPrefs;
    private String methodNamePrefs;
    private List<NameValuePair> nameValuePairs;
    private Activity activity;

    public HTTPHelper(String hostURLPrefs, String methodNamePrefs, List<NameValuePair> nameValuePairs, Activity activity) {
        this.setHostURLPrefs(hostURLPrefs);
        this.setMethodNamePrefs(methodNamePrefs);
        this.setNameValuePairs(nameValuePairs);
        this.setActivity(activity);
    }

    public String executeHelper() {

        try {
            InputStream inputStream = getActivity().getResources().getAssets().open("config.properties");
            Properties properties = PropertyReader.getInstance().readConfigProperties(inputStream);

            String hostURL = properties.getProperty(getHostURLPrefs());
            String methodName = properties.getProperty(getMethodNamePrefs());

            URI uri = new URI(hostURL.concat(methodName));

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uri);

            httppost.setEntity(new UrlEncodedFormEntity(getNameValuePairs()));
            HttpResponse response = httpclient.execute(httppost);

            InputStream ips = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                throw new Exception("-1");
            } else {
                StringBuilder sb = new StringBuilder();
                String s;
                while (true) {
                    s = buf.readLine();
                    if (s == null || s.length() == 0)
                        break;
                    sb.append(s);
                    Log.d("HTTPHelper", sb.toString());
                }
                buf.close();
                ips.close();
                return sb.toString();
            }
        } // TODO: register the new account here.
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-1";
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-1";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-1";
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-1";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "-1";
        }

    }

    public String getHostURLPrefs() {
        return hostURLPrefs;
    }

    public void setHostURLPrefs(String hostURLPrefs) {
        this.hostURLPrefs = hostURLPrefs;
    }

    public String getMethodNamePrefs() {
        return methodNamePrefs;
    }

    public void setMethodNamePrefs(String methodNamePrefs) {
        this.methodNamePrefs = methodNamePrefs;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    public void setNameValuePairs(List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
