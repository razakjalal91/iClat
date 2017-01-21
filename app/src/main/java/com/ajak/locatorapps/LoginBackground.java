package com.ajak.locatorapps;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Ajak on 1/6/2017.
 */

public class LoginBackground extends AsyncTask<Void,Void,String> {
    private Context context;
    public String ic_number;
    public SharedPreferences.Editor editor;

    public LoginBackground(Context context,String ic_number,SharedPreferences.Editor editor) {
        this.context = context;
        this.ic_number = ic_number;
        this.editor = editor;
    }

    @Override
    protected String doInBackground(Void... arg0) {

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
            data =  "?ic=" + URLEncoder.encode(ic_number, "UTF-8");

            link = "http://dev.16mb.com/login.php" + data;
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }


    protected void onPostExecute(String result){
        String jsonStr = result;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("query_result");

                if (query_result.equals("SUCCESS")) {
                    Toast.makeText(context, "Logged In!Welcome.", Toast.LENGTH_SHORT).show();
                    editor.putString("icnumber",ic_number);
                    editor.apply();
                    Intent intent = new Intent(context,MainActivity.class);
                    context.startActivity(intent);
                } else if (query_result.equals("FAILURE")) {
                    Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show();
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


}
