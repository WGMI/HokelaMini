package com.hkl.hokelamini;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hkl.hokelamini.Models.Answer;
import com.hkl.hokelamini.Models.Constants;
import com.hkl.hokelamini.Models.Question;
import com.hkl.hokelamini.Models.User;
import com.hkl.hokelamini.Network.ApiClient;
import com.hkl.hokelamini.Network.RetrofitClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;
    String token;
    User user;

    TextView questionText;
    EditText answer;
    RadioGroup options;
    LinearLayout multipleOptions;
    Button imageaction,locationaction/*,answercontrol*/;
    Button prev,next;

    long surveyId;
    List<Question> questions;
    int questionCounter;
    int questionCountTotal;
    Question currentQuestion;
    boolean answered;

    List<Answer> imageanswers;

    static String TAG = "tag_report";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        context = this;

        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        token = preferences.getString("token",null);
        user = new Gson().fromJson(preferences.getString("user",null), User.class);
        surveyId = getIntent().getLongExtra("survey_id",-1);

        questionText = findViewById(R.id.question);
        answer = findViewById(R.id.text_answer);
        options = findViewById(R.id.options_answer);
        multipleOptions = findViewById(R.id.multiple_options_answer);
        imageaction = findViewById(R.id.image_action);
        locationaction = findViewById(R.id.location_action);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered){
                    checkAnswer();
                }else{
                    showNext();
                }
            }
        });

        imageanswers = new ArrayList<>();
        fetchQuestions();
    }

    private void fetchQuestions() {
        Call<List<Question>> call = RetrofitClient.getRetrofitInstance().create(ApiClient.class).getQuestions("Bearer " + token,surveyId);
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if(response != null){
                    questions = response.body();
                    questionCountTotal = questions.size();
                    showNext();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void showNext() {
        if(questionCounter < questionCountTotal){
            currentQuestion = questions.get(questionCounter);
            currentQuestion.setAnswer(new Answer());
            questionText.setText(currentQuestion.getQuestion_text());
            showAnswerLayout();
            questionCounter++;
            answered = false;
        } else{
            endReport();
        }
    }

    private void showAnswerLayout(){
        if (currentQuestion.getOptions() != null && currentQuestion.getOptions().length() > 0) {
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 10, 0, 10);
            if (currentQuestion.getType().equals(Constants.OPTION)) {
                multipleOptions.setVisibility(View.GONE);
                options.setVisibility(View.VISIBLE);
                options.removeAllViews();
                try {
                    JSONArray opts = new JSONArray(currentQuestion.getOptions());
                    for (int i = 0; i < opts.length(); i++) {
                        RadioButton rb = new RadioButton(context);
                        rb.setLayoutParams(p);
                        rb.setText(opts.getString(i));
                        View.OnClickListener l = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                currentQuestion.getAnswer().setAnswer((String) rb.getText());
                            }
                        };
                        rb.setOnClickListener(l);
                        options.addView(rb);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (currentQuestion.getType().equals(Constants.MULTIPLE_OPTIONS)) {
                options.setVisibility(View.GONE);
                multipleOptions.setVisibility(View.VISIBLE);
                multipleOptions.removeAllViews();
                JSONArray checkedanswers = new JSONArray();
                try {
                    JSONArray opts = new JSONArray(currentQuestion.getOptions());
                    for (int i = 0; i < opts.length(); i++) {
                        CheckBox cb = new CheckBox(context);
                        cb.setLayoutParams(p);
                        cb.setText(opts.getString(i));

                        View.OnClickListener l = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkedanswers.put((String) cb.getText());
                                currentQuestion.getAnswer().setAnswer(checkedanswers.toString());
                            }
                        };
                        cb.setOnClickListener(l);
                        multipleOptions.addView(cb);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            options.setVisibility(View.GONE);
            multipleOptions.setVisibility(View.GONE);
        }

        answer.setVisibility(currentQuestion.getType().equals(Constants.TEXT) ? View.VISIBLE : View.GONE);
        imageaction.setVisibility(currentQuestion.getType().equals(Constants.IMAGE) ? View.VISIBLE : View.GONE);
        locationaction.setVisibility(currentQuestion.getType().equals(Constants.LOCATION) ? View.VISIBLE : View.GONE);

        imageaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage((int) currentQuestion.getId());
            }
        });
    }

    private void captureImage(int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,code);
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

    private void checkAnswer(){
        answered = true;
        if(currentQuestion.getType().equals(Constants.TEXT)){
            String answerString = answer.getText().toString();
            Log.d(TAG, "checkAnswer: " + answerString);
            //currentQuestion.getAnswer().setAnswer();
        }
    }

    private void endReport() {
        finish();
    }
}