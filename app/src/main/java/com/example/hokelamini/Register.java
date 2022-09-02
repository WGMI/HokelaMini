package com.example.hokelamini;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hokelamini.Models.Responses.RegisterResponse;
import com.example.hokelamini.Models.User;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    Context context;

    EditText name,lname,idno,email,number,dob,country,password,confirmPass;
    Button submit;

    static String TAG = "tag_register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        name = findViewById(R.id.name);
        lname = findViewById(R.id.lname);
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
                startActivity(new Intent(context,MainActivity.class));
                //Validate not empty
                /*User user = new User("name","0700","country","12345678","0","2000-01-01","email@e.e","password");
                ApiClient client = RetrofitClient.getRetrofitInstance().create(ApiClient.class);
                Call<RegisterResponse> call = client.register("name","a@b.b","0700","KEN","12345678","2000-01-01","password");
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });*/
            }
        });
    }
}