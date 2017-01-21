package com.ajak.locatorapps;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Ajak on 1/6/2017.
 */

public class SignupActivity extends Activity{
    private EditText uname,pword,fname,ic;
    private Button signup_btn;
    public Context context;

    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.signup_layout);

        uname = (EditText) findViewById(R.id.uname);
        pword = (EditText) findViewById(R.id.password);
        fname = (EditText) findViewById(R.id.fullname);
        ic = (EditText) findViewById(R.id.icNumber);
        signup_btn = (Button) findViewById(R.id.signup_btn);
    }

    public void signup(View v){
        String username = uname.getText().toString();
        String password = pword.getText().toString();
        String fullname = fname.getText().toString();
        String icNumber = ic.getText().toString();
        Toast.makeText(this, "Signing up...", Toast.LENGTH_SHORT).show();
        new SignupBackground(this).execute(username,password,fullname,icNumber);
    }

    public void to_login(View v){
        Toast.makeText(this, "To Login Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }




}
