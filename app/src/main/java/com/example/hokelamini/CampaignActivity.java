package com.example.hokelamini;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hokelamini.Models.Adapters.SurveyAdapter;
import com.example.hokelamini.Models.Responses.StandardResponse;
import com.example.hokelamini.Models.Survey;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CampaignActivity extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    String token;

    Toolbar toolbar;
    Button sessionButton;
    Button reportsButton;

    ImageView image;
    Bitmap img;
    TextView msg;
    CardView beginlayout;
    View.OnClickListener startSessionListener;
    View.OnClickListener endSessionListener;
    JSONArray coords;

    long campaignId;
    String campaign;
    static String TAG = "tag_campaign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        context = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        token = preferences.getString("token",null);
        campaignId = getIntent().getLongExtra("campaign_id",-1);
        campaign = getIntent().getStringExtra("campaign_name");

        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title);
        title.setText(campaign);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        startSessionListener = view -> startSession();
        endSessionListener = view -> endSession();

        reportsButton = findViewById(R.id.reports);
        sessionButton = findViewById(R.id.session);
        sessionButton.setOnClickListener(startSessionListener);
        checkSession();
    }

    private void checkSession() {
        Call<StandardResponse> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).checkSessionStatus("Bearer " + token,campaignId);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if(response.code() == 200){
                    setSessionExistsState();
                }

                if(response.code() == 400){
                    setSessionDoesNotExistState();
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {

            }
        });
    }

    private void startSession() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.start_session_dialog);

        Button begin,photoButton;
        EditText area;

        photoButton = dialog.findViewById(R.id.photo);
        area = dialog.findViewById(R.id.area);
        image = dialog.findViewById(R.id.image);
        msg = dialog.findViewById(R.id.fetchingmessage);
        beginlayout = dialog.findViewById(R.id.beginlayout);
        begin = dialog.findViewById(R.id.begin);

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Getting Location");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        coords = new JSONArray();
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
            return;
        }

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                try {
                    if(location == null){
                        coords.put("0.0");
                        coords.put("0.0");
                        return;
                    }
                    coords.put(location.getLatitude());
                    coords.put(location.getLongitude());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "Location set: " + coords, Toast.LENGTH_SHORT).show();
                msg.setText("Location Fetched\n" + coords);
                progressDialog.dismiss();
            }
        });

        area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Check if there is text in the area field
                boolean isAreaFieldNotEmpty = charSequence.length() > 0;

                // Enable or disable the "take picture" button based on the condition
                photoButton.setEnabled(isAreaFieldNotEmpty);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage(999);
            }
        });

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sendSessionToServer();
            }
        });

        dialog.show();
    }

    private void endSession(){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Ending session");
        dialog.show();
        Call<StandardResponse> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).endSession("Bearer " + token,campaignId);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                Log.d(TAG, "onResponse: " + response.code());
                dialog.dismiss();
                if(response.code() == 204){
                    setSessionDoesNotExistState();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error")
                            .setMessage("Please try again.")
                            .setNegativeButton("OK",null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
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

    private void sendSessionToServer() {
        Checkin checkin = new Checkin(campaignId,coords.toString());
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Starting session");
        dialog.show();
        Call<StandardResponse> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).sendSession("Bearer " + token,checkin);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if(response.code() == 201){
                    setSessionExistsState();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
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

    private void setSessionExistsState() {
        sessionButton.setBackgroundColor(Color.RED);
        sessionButton.setText("Check Out");
        sessionButton.setOnClickListener(endSessionListener);
        reportsButton.setVisibility(View.VISIBLE);
        reportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ReportsActivity.class);
                i.putExtra("campaign_id",campaignId);
                i.putExtra("campaign_name",campaign);
                startActivity(i);
            }
        });
    }

    private void setSessionDoesNotExistState() {
        sessionButton.setBackgroundColor(Color.parseColor("#4CAF50"));
        sessionButton.setText("Check In");
        sessionButton.setOnClickListener(startSessionListener);
        reportsButton.setVisibility(View.GONE);
    }

    private void captureImage(int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,code);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // Get the captured image
            img = (Bitmap) data.getExtras().get("data");

            // Display the captured image in the ImageView
            beginlayout.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(img);

            // Convert the image to Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            String imageString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            Log.d("TAG", "onActivityResult: " + requestCode);
        }
    }
}