package com.example.hokelamini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = preferences.edit();
        token = preferences.getString("token",null);
        if(token == null){
            //startActivity(new Intent(this,Login.class));
            startActivity(new Intent(this,Register.class));
            finish();
            return;
        }


    }
}