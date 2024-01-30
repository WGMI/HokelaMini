package com.example.hokelamini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hokelamini.Models.Adapters.CampaignAdapter;
import com.example.hokelamini.Models.Adapters.ProjectAdapter;
import com.example.hokelamini.Models.Campaign;
import com.example.hokelamini.Models.Project;
import com.example.hokelamini.Models.Responses.StandardResponse;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String token;

    Toolbar toolbar;
    RecyclerView campaignList;
    List<Campaign> campaigns;
    CampaignAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    static String TAG = "tag_main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        context = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = preferences.edit();
        token = preferences.getString("token",null);
        if(token == null){
            startActivity(new Intent(this,Login.class));
            finish();
            return;
        }

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);

        campaigns = new ArrayList<>();
        campaignList = findViewById(R.id.campaign_list);

        fetchCampaigns();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCampaigns();
            }
        });
    }

    private void fetchCampaigns() {
        Call<List<Campaign>> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).getCampaigns("Bearer " + token);
        call.enqueue(new Callback<List<Campaign>>() {
            @Override
            public void onResponse(Call<List<Campaign>> call, Response<List<Campaign>> response) {
                campaigns = response.body();
                LinearLayoutManager manager = new LinearLayoutManager(context);
                campaignList.setLayoutManager(manager);
                adapter = new CampaignAdapter(context,campaigns);
                campaignList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Campaign>> call, Throwable t) {

            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_campaign:
                newCampaign();
                break;
            case R.id.logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void newCampaign() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.project_dialog);

        EditText code = dialog.findViewById(R.id.code);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button join = dialog.findViewById(R.id.join);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeString = code.getText().toString();
                joinCampaign(codeString);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void joinCampaign(String code) {
        Call<StandardResponse> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).joinCampaign("Bearer " + token,code);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if(response.code() == 201){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Success")
                            .setMessage("Refresh the page")
                            .setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(context,MainActivity.class));
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void logout(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(context,Login.class));
        Call<StandardResponse> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).logout("Bearer " + token);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                startActivity(new Intent(context,Login.class));
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {

            }
        });
    }
}