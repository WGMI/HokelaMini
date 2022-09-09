package com.example.hokelamini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hokelamini.Models.Adapters.ProjectAdapter;
import com.example.hokelamini.Models.Responses.Project;
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
    RecyclerView projectList;
    List<Project> projects;
    ProjectAdapter adapter;

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

        projects = new ArrayList<>();
        projectList = findViewById(R.id.project_list);

        fetchProjects();
    }

    private void fetchProjects() {
        Call<List<Project>> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).getProjects("Bearer " + token);
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if(response != null){
                    projects = response.body();
                    LinearLayoutManager manager = new LinearLayoutManager(context);
                    projectList.setLayoutManager(manager);
                    adapter = new ProjectAdapter(context,projects);
                    projectList.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_project:
                newProject();
                break;
            case R.id.logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void newProject() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.project_dialog);

        EditText code = dialog.findViewById(R.id.code);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button join = dialog.findViewById(R.id.join);

        cancel.setOnClickListener(null);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeString = code.getText().toString();
                joinProject(codeString);
            }
        });

        dialog.show();
    }

    private void joinProject(String code) {
//        Call<SurveyResponse> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).joinSurvey(token,code);
//        call.enqueue(new Callback<SurveyResponse>() {
//            @Override
//            public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<SurveyResponse> call, Throwable t) {
//
//            }
//        });
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