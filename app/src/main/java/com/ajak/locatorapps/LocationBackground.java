package com.ajak.locatorapps;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Ajak on 12/28/2016.
 */

public class LocationBackground extends AsyncTask<Double,Void,String> {
    private Context context;
    double longitude;
    double latitude;
    String address,icNumber,appId,routeId;

    // Constructor to past parameters more easily
    public LocationBackground(Context context, String address, String icNumber, String appId,String routeId) {
        this.context = context;
        this.address = address;
        this.icNumber = icNumber;
        this.appId = appId;
        this.routeId = routeId;
    }
    // Constructor to past parameters more easily END

    protected void onPreExecute(){

    }

    protected String doInBackground(Double... arg0) {
        double longitude =  arg0[0];
        double latitude =   arg0[1];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;
        try {
            data = "?long=" + longitude;
            data += "&lat=" + latitude;
            data += "&addr=" + address;
            data += "&ic=" + icNumber;
            data += "&app=" + appId;
            data += "&route=" + routeId;

            link = "http://dev.16mb.com/locatorconn.php" + data;
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    protected void onPostExecute(String result) {
        String jsonStr = result;

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("query_result");
                if (query_result.equals("SUCCESS")) {

                } else if (query_result.equals("FAILURE")) {
                    Toast.makeText(context, "Failed To Update Location.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to Parse JSON!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void locationUpdate(){

    }


}
