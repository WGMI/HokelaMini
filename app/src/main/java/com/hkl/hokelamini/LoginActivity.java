package com.hkl.hokelamini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }

    public void login(View v){
        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
    }

    public void register(View v){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
}