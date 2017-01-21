package com.ajak.locatorapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.ajak.locatorapps.SaveSharedPreference.getSharedPreferences;

/**
 * Created by Ajak on 1/6/2017.
 */

public class LoginActivity extends Activity{
    public EditText ic;
    public Button login_btn,signup_btn;
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.login_layout);

        ic = (EditText) findViewById(R.id.icText);
        login_btn = (Button) findViewById(R.id.login_lgn_btn);
        signup_btn = (Button) findViewById(R.id.signup_lgn_btn);
    }

    public void login(View v){
        String ic_number = ic.getText().toString();
        Toast.makeText(getApplicationContext(),"Logging In",Toast.LENGTH_SHORT).show();
        SharedPreferences sp = getSharedPreferences("data_preference",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        new LoginBackground(this,ic_number,editor).execute();
    }

    public void to_signup(View v){
        Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
        finish();
    }

}
