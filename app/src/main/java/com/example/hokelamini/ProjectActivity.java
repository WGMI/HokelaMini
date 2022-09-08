package com.example.hokelamini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.hokelamini.Models.Survey;
import com.example.hokelamini.Models.Adapters.SurveyAdapter;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectActivity extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    String token;

    Toolbar toolbar;
    RecyclerView surveyList;
    List<Survey> surveys;
    SurveyAdapter adapter;

    long projectId;
    static String TAG = "tag_project";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        context = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        token = preferences.getString("token",null);
        projectId = getIntent().getLongExtra("project_id",-1);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        surveys = new ArrayList<>();
        surveyList = findViewById(R.id.survey_list);

        fetchSurveys();
    }

    private void fetchSurveys() {
        Call<List<Survey>> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).getSurveys("Bearer " + token, projectId);
        call.enqueue(new Callback<List<Survey>>() {
            @Override
            public void onResponse(Call<List<Survey>> call, Response<List<Survey>> response) {
                if(response != null){
                    surveys = response.body();
                    LinearLayoutManager manager = new LinearLayoutManager(context);
                    surveyList.setLayoutManager(manager);
                    adapter = new SurveyAdapter(context,surveys);
                    surveyList.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Survey>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}