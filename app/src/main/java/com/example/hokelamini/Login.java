package com.example.hokelamini;

import android.app.ProgressDialog;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hokelamini.Models.Responses.AuthResponse;
import com.example.hokelamini.Models.User;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;
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
                ProgressDialog dialog = new ProgressDialog(context);
                dialog.setMessage("Signing In...");
                dialog.show();

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
                        dialog.dismiss();
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
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Error")
                            .setMessage("Please try again.")
                            .setNegativeButton("OK",null)
                            .show();
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