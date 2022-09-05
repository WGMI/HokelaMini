package com.example.hokelamini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.hokelamini.Models.Adapters.QuestionAdapter;
import com.example.hokelamini.Models.Adapters.SurveyAdapter;
import com.example.hokelamini.Models.Question;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyActivity extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    String token;

    Toolbar toolbar;
    RecyclerView questionList;
    List<Question> questions;
    QuestionAdapter adapter;

    long surveyId;
    static String TAG = "tag_survey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        context = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        token = preferences.getString("token",null);
        surveyId = getIntent().getLongExtra("survey_id",-1);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        questions = new ArrayList<>();
        questionList = findViewById(R.id.question_list);
        fetchQuestions();
    }

    private void fetchQuestions() {
        Call<List<Question>> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).getQuestions("Bearer " + token,surveyId);
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if(response != null){
                    questions = response.body();
                    LinearLayoutManager manager = new LinearLayoutManager(context);
                    questionList.setLayoutManager(manager);
                    adapter = new QuestionAdapter(context,questions);
                    questionList.setAdapter(adapter);
                    Log.d(TAG, "fetchProjects: " + questions.size());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}