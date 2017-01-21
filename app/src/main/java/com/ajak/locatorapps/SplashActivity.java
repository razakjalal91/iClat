package com.ajak.locatorapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Ajak on 1/6/2017.
 */

public class SplashActivity extends Activity {
    public Context context;
    GifImageView splgif;
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.splash_screen);
        splgif = (GifImageView) findViewById(R.id.splashgifid);
        Thread timerThread = new Thread(){
            public void run()
            {
                try{
                    sleep(3000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    SharedPreferences sp = getSharedPreferences("data_preference",Context.MODE_PRIVATE);
                    String ic = sp.getString("icnumber","");

                    if(ic == ""){
                        Intent intent = new Intent(SplashActivity.this,SignupActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }
}
