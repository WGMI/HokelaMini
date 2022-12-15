package com.hkl.hokelamini.Models.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hkl.hokelamini.Models.Answer;
import com.hkl.hokelamini.Models.Constants;
import com.hkl.hokelamini.Models.Question;
import com.hkl.hokelamini.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    Context context;
    Activity activity;
    List<Question> questionList;

    public QuestionAdapter(Context context, Activity activity, List<Question> questionList) {
        this.context = context;
        this.activity = activity;
        this.questionList = questionList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.question.setText(question.getQuestion_text());

        //holder.setIsRecyclable(false);

        if (questionList.get(position).getOptions() != null && questionList.get(position).getOptions().length() > 0) {
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 10, 0, 10);
            if (questionList.get(position).getType().equals(Constants.OPTION)) {
                holder.multipleOptions.setVisibility(View.GONE);
                holder.options.setVisibility(View.VISIBLE);
                holder.options.removeAllViews();
                try {
                    JSONArray opts = new JSONArray(questionList.get(position).getOptions());
                    for (int i = 0; i < opts.length(); i++) {
                        RadioButton rb = new RadioButton(context);
                        rb.setLayoutParams(p);
                        rb.setText(opts.getString(i));
                        View.OnClickListener l = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                question.getAnswer().setAnswer((String) rb.getText());
                            }
                        };
                        rb.setOnClickListener(l);
                        holder.options.addView(rb);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (questionList.get(position).getType().equals(Constants.MULTIPLE_OPTIONS)) {
                holder.options.setVisibility(View.GONE);
                holder.multipleOptions.setVisibility(View.VISIBLE);
                holder.multipleOptions.removeAllViews();
                JSONArray checkedanswers = new JSONArray();
                try {
                    JSONArray opts = new JSONArray(questionList.get(position).getOptions());
                    for (int i = 0; i < opts.length(); i++) {
                        CheckBox cb = new CheckBox(context);
                        cb.setLayoutParams(p);
                        cb.setText(opts.getString(i));

                        View.OnClickListener l = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkedanswers.put((String) cb.getText());
                                question.getAnswer().setAnswer(checkedanswers.toString());
                            }
                        };
                        cb.setOnClickListener(l);
                        holder.multipleOptions.addView(cb);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            holder.options.setVisibility(View.GONE);
            holder.multipleOptions.setVisibility(View.GONE);
        }

        holder.answer.setVisibility(question.getType().equals(Constants.TEXT) ? View.VISIBLE : View.GONE);
        holder.imageaction.setVisibility(question.getType().equals(Constants.IMAGE) ? View.VISIBLE : View.GONE);
        holder.locationaction.setVisibility(question.getType().equals(Constants.LOCATION) ? View.VISIBLE : View.GONE);

        holder.imageaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage((int) questionList.get(position).getId());
            }
        });

        Answer a = new Answer();

        holder.locationaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = new ProgressDialog(context);
                dialog.setTitle("Getting Location");
                dialog.setMessage("Please wait...");
                dialog.show();
                JSONArray coords = new JSONArray();
                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(activity);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        try {
                            coords.put(location.getLatitude());
                            coords.put(location.getLongitude());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        a.setAnswer(coords.toString());
                        holder.info.setVisibility(View.VISIBLE);
                        holder.info.setText("Location Captured");
                        dialog.dismiss();
                    }
                });
            }
        });

        if (question.getType().equals(Constants.TEXT)) {
            holder.answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    a.setAnswer(editable.toString());
                }
            });
        }

        question.setAnswer(a);
    }

    private void captureImage(int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent,code);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView question,info;
        LinearLayout answerLayout;
        EditText answer;
        RadioGroup options;
        LinearLayout multipleOptions;
        Button imageaction,locationaction/*,answercontrol*/;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            info = itemView.findViewById(R.id.info);
            answerLayout = itemView.findViewById(R.id.answer_area);
            answer = itemView.findViewById(R.id.text_answer);
            options = itemView.findViewById(R.id.options_answer);
            multipleOptions = itemView.findViewById(R.id.multiple_options_answer);
            imageaction = itemView.findViewById(R.id.image_action);
            locationaction = itemView.findViewById(R.id.location_action);
        }
    }
}