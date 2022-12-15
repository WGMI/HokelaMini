package com.hkl.hokelamini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hkl.hokelamini.Models.Responses.AuthResponse;
import com.hkl.hokelamini.Models.User;
import com.hkl.hokelamini.Network.ApiClient;
import com.hkl.hokelamini.Network.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    EditText email,password;
    Button submit,register;

    static String TAG = "tag_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        submit = findViewById(R.id.submitButton);
        register = findViewById(R.id.register);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(context,MainActivity.class));
                //Validate not empty

                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                if(
                    TextUtils.isEmpty(emailString) ||
                    TextUtils.isEmpty(passwordString)
                ){
                    Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                ApiClient client = RetrofitClient.getRetrofitInstance().create(ApiClient.class);
                Call<AuthResponse> call = client.login(emailString,passwordString);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        Gson gson = new Gson();
                        User user = response.body().getUser();
                        String token = response.body().getToken();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user",gson.toJson(user));
                        editor.putString("token",token);
                        editor.apply();
                        startActivity(new Intent(context,MainActivity.class));
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,Register.class));
            }
        });
    }
}