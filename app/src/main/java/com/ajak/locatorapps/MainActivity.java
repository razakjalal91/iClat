package com.ajak.locatorapps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import pl.droidsonroids.gif.GifImageView;

import static com.ajak.locatorapps.R.layout.main_layout;

/**
 * Created by Ajak on 12/28/2016.
 */
public class MainActivity extends Activity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Button LocBtn, stopBtn;
    public String appId, routeId;
    LocationRequest mLocationRequest;
    double initLong;
    double initLat;
    GifImageView trackGif;
    public Location location = null, mLastLocation;
    public TextView viewId, idTitle,longtext,lattext,longdisp,latdisp;
    boolean stopFlag;
    boolean toggleBtn;
    GoogleApiClient mGoogleApiClient;
    LinearLayout main;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(main_layout);
        buildGoogleApiClient();
        createLocationRequest();

        final Handler h = new Handler();
        viewId = (TextView) findViewById(R.id.userId);
        idTitle = (TextView) findViewById(R.id.idText);
        longtext = (TextView) findViewById(R.id.LongText);
        lattext = (TextView) findViewById(R.id.latText);
        longdisp = (TextView) findViewById(R.id.longDisp);
        latdisp = (TextView) findViewById(R.id.latDisp);
        trackGif = (GifImageView) findViewById(R.id.trackLoad);
        main = (LinearLayout) findViewById(R.id.linearLayout);
        SharedPreferences sp = getSharedPreferences("data_preference", Context.MODE_PRIVATE);
        trackGif.setVisibility(View.INVISIBLE);
        // Show IC No
        String unm = sp.getString("icnumber", null);
        viewId.setText(unm);
        // Show IC No END

        //Set Random App ID
        appId = UUID.randomUUID().toString();
        //Set Random App ID END

        // Create the widgets
        stopBtn = (Button) findViewById(R.id.stopTimer);
        LocBtn = (Button) findViewById(R.id.sendLoc);
        // Create the widgets END

        // Disable Stop Button By Default
        stopBtn.setEnabled(false);
        // Disable Stop Button By Default END

        // Stop Button OnClick Behaviour
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBtn = false;
                LocBtn.setEnabled(true);
                stopBtn.setEnabled(false);
                longdisp.setText("");
                latdisp.setText("");
                trackGif.setVisibility(View.INVISIBLE);
                stopLocationUpdates();
            }
        });
        // Stop Button OnClick Behaviour END

        // Tracking Button OnClick Behaviour
        LocBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(isGpsProviderOn() && isNetworkProviderOn() && internet()) {
                    LocBtn.setEnabled(false);
                    stopBtn.setEnabled(true);
                    routeId = UUID.randomUUID().toString();
                    trackGif.setVisibility(View.VISIBLE);
                    startLocationUpdates();
                }else{
                    Snackbar.make(main,"GPS And Internet Connection Unavailable!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        // Tracking Button OnClick Behaviour END
    }

    // Connect GoogleApiCLient When The App Starts
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            Snackbar.make(main,"Google API Connected!Welcome!",Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(main,"Google API Not Connected",Snackbar.LENGTH_SHORT).show();
        }
    }
    // Connect GoogleApiCLient When The App Starts END


    // Disconnect GoogleApiClient When Activity Not Visible
    protected void onStop() {
        super.onStop();
        final Handler h = new Handler();
        Toast.makeText(getApplicationContext(), "Closing Apps!", Toast.LENGTH_SHORT).show();
        h.removeCallbacksAndMessages(null);
        mGoogleApiClient.disconnect();
    }
    // Disconnect GoogleApiClient When Activity Not Visible END

    private void mainLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        SharedPreferences sp = getSharedPreferences("data_preference", Context.MODE_PRIVATE);
        String icNumber = sp.getString("icnumber", null);
        Geocoder geo = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        String a = Double.toString(latitude);
        String b = Double.toString(longitude);

        Float acc = mLastLocation.getAccuracy();
        String accStr = acc.toString();

        try {
            addresses = geo.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Set Latitude and Longitude Display When Changed
        latdisp.setText(a);
        longdisp.setText(b);
        // Set Latitude and Longitude Display When Changed END
        String cityName = addresses.get(0).getAddressLine(0);
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);
        String fullAdd = cityName + stateName + countryName;
        String newAdd = fullAdd.replaceAll("[,\\s]", "_");
        Snackbar.make(main,"Location Changed!Accuracy: "+accStr,Snackbar.LENGTH_SHORT).show();
        new LocationBackground(this, newAdd, icNumber, appId, routeId).execute(longitude, latitude);
    }

    //Main Location Method
    @Override
    public void onLocationChanged(Location location) {
            mLastLocation = location;
            mainLocation();
    }
    // Main Location Method END

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //Build The GoogleAPI
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    //Build The GoogleAPI END

    // Logout Method
    public void logout(View view) {
        SharedPreferences sp = getSharedPreferences("data_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Toast.makeText(getApplicationContext(), "Logging Out..", Toast.LENGTH_SHORT).show();
        editor.clear();
        editor.commit();
        finish();
    }
    // Logout Method END

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Snackbar.make(main,"Tracking Stop!",Snackbar.LENGTH_SHORT).show();
    }

    protected boolean internet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    protected boolean isGpsProviderOn(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean isGps = lm.isProviderEnabled(lm.GPS_PROVIDER);
        if(isGps){
            return true;
        }else{
            return false;
        }
    }

    protected boolean isNetworkProviderOn(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean isNetwork = lm.isProviderEnabled(lm.NETWORK_PROVIDER);
        if(isNetwork){
            return true;
        }else{
            return false;
        }
    }


    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Closing Application")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    public void onConnected(Bundle b){

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
