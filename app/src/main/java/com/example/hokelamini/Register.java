package com.example.hokelamini;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hokelamini.Models.Responses.AuthResponse;
import com.example.hokelamini.Models.Responses.StandardResponse;
import com.example.hokelamini.Models.User;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;

import java.util.Calendar;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    EditText name,lname,idno,email,number,dob,country,password,confirmPass;
    Button submit;

    static String TAG = "tag_register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        name = findViewById(R.id.email);
        lname = findViewById(R.id.pass);
        idno = findViewById(R.id.idText);
        email = findViewById(R.id.user_email);
        number = findViewById(R.id.phone_number);
        dob = findViewById(R.id.dob);
        country = findViewById(R.id.country);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.password_confirm);
        submit = findViewById(R.id.submitButton);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(context,MainActivity.class));
                //Validate not empty

                String nameString = name.getText().toString();
                String lnameString = lname.getText().toString();
                String idnoString = idno.getText().toString();
                String emailString = email.getText().toString();
                String numberString = number.getText().toString();
                String dobString = dob.getText().toString();
                String countryString = country.getText().toString();
                String passwordString = password.getText().toString();
                String confirmPassString = confirmPass.getText().toString();

                if(
                        TextUtils.isEmpty(nameString) ||
                        TextUtils.isEmpty(lnameString) ||
                        TextUtils.isEmpty(idnoString) ||
                        TextUtils.isEmpty(emailString) ||
                        TextUtils.isEmpty(numberString) ||
                        TextUtils.isEmpty(dobString) ||
                        TextUtils.isEmpty(countryString) ||
                        TextUtils.isEmpty(passwordString) ||
                        TextUtils.isEmpty(confirmPassString)
                ){
                    Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!passwordString.equals(confirmPassString)){
                    Toast.makeText(context, "The passwords do not match.", Toast.LENGTH_LONG).show();
                    return;
                }

                ApiClient client = RetrofitClient.getRetrofitInstance().create(ApiClient.class);
                Call<AuthResponse> call = client.register(nameString + " " + lnameString,emailString,numberString,countryString,idnoString,dobString,passwordString);
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
    }
}