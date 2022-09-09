package com.example.hokelamini;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hokelamini.Models.Adapters.QuestionAdapter;
import com.example.hokelamini.Models.Adapters.SurveyAdapter;
import com.example.hokelamini.Models.Answer;
import com.example.hokelamini.Models.Question;
import com.example.hokelamini.Models.Responses.StandardResponse;
import com.example.hokelamini.Models.User;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
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
    FloatingActionButton submit;

    long surveyId;
    List<Answer> answers;

    static String TAG = "tag_survey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        context = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        token = preferences.getString("token",null);
        User user = new Gson().fromJson(preferences.getString("user",null), User.class);
        surveyId = getIntent().getLongExtra("survey_id",-1);
        String surveyName = getIntent().getStringExtra("survey_name");

        toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.title);
        title.setText(surveyName);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        questionList = findViewById(R.id.question_list);
        submit = findViewById(R.id.submit);

        questions = new ArrayList<>();
        answers = new ArrayList<>();
        fetchQuestions();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Question q : questions){
                    //Redundancy
                    Answer a = new Answer(q.getAnswer().getAnswer(),q.getId(),user.getId());
                    if(a.getAnswer() == null){
                        a.setAnswer("NA");
                    }
                    answers.add(a);
                }
                sendAnswers(answers);
            }
        });
    }

    private void sendAnswers(List<Answer> answers) {
        Call<StandardResponse> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).storeAnswers("Bearer " + token,answers);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if(response.code() == 201){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Success")
                            .setMessage("Report submitted")
                            .setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
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
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}