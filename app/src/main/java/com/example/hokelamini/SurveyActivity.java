package com.example.hokelamini;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hokelamini.Models.Adapters.QuestionAdapter;
import com.example.hokelamini.Models.Answer;
import com.example.hokelamini.Models.Constants;
import com.example.hokelamini.Models.Question;
import com.example.hokelamini.Models.Responses.StandardResponse;
import com.example.hokelamini.Models.User;
import com.example.hokelamini.Network.ApiClient;
import com.example.hokelamini.Network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyActivity extends AppCompatActivity {

    Context context;
    Activity activity;
    SharedPreferences preferences;
    String token;
    User user;

    Toolbar toolbar;
    RecyclerView questionList;
    List<Question> questions;
    QuestionAdapter adapter;
    Button submit;

    long surveyId;
    List<Answer> answers;
    List<Answer> imageanswers;

    static String TAG = "tag_survey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        context = this;
        activity = this;
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        token = preferences.getString("token",null);
        user = new Gson().fromJson(preferences.getString("user",null), User.class);
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
        imageanswers = new ArrayList<>();
        fetchQuestions();

        submit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for(Question q : questions){
                    //Redundancy
                    if(q.getType().equals(Constants.IMAGE)){
                        continue;
                    }
                    Answer a = new Answer(q.getAnswer().getAnswer(),q.getId(),user.getId());
                    if(a.getAnswer() == null || a.getAnswer().length() < 1){
                        a.setAnswer("NA");
                    }
                    Log.d(TAG, "onLongClick: " + new Gson().toJson(a));
                }
                return true;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Question q : questions){
                    //Redundancy
                    if(q.getType().equals(Constants.IMAGE)){
                        continue;
                    }
                    Answer a = new Answer(q.getAnswer().getAnswer(),q.getId(),user.getId());
                    if(a.getAnswer() == null || a.getAnswer().length() < 1){
                        a.setAnswer("NA");
                    }
                    answers.add(a);
                }

                answers.addAll(imageanswers);
                sendAnswers(answers);
            }
        });
    }

    private void sendAnswers(List<Answer> answers) {
        Call<StandardResponse> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).storeAnswers("Bearer " + token,answers);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                Log.d(TAG, "onResponse: " + response.code());
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
                    adapter = new QuestionAdapter(context,activity,questions);
                    questionList.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap img = (Bitmap) data.getExtras().get("data");
            img.compress(Bitmap.CompressFormat.JPEG,80,baos);
            String imageString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            imageanswers.add(new Answer(imageString,requestCode,user.getId()));
            Log.d(TAG, "onActivityResult: " + requestCode);
        }
    }
}