package com.example.hokelamini.Models.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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

import com.example.hokelamini.Models.Answer;
import com.example.hokelamini.Models.Constants;
import com.example.hokelamini.Models.Question;
import com.example.hokelamini.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ViewHolder> {

    Context context;
    Activity activity;
    List<Question> questionList;

    public QueryAdapter(Context context, Activity activity, List<Question> questionList) {
        this.context = context;
        this.activity = activity;
        this.questionList = questionList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_query,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Question query = questionList.get(position);
        holder.question.setText(query.getQuestion_text());

        if(query.getAnswer() != null){
            Log.d("TAG_", "onClick: " + new Gson().toJson(query));
            holder.info.setText(query.getAnswer().getAnswer());
            if(query.getType().equals(Constants.LOCATION)){
                holder.info.setText("Location Captured");
            }
            if(query.getType().equals(Constants.IMAGE)){
                holder.info.setText("Image Captured");
            }
        }

        holder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer(query,position);
            }
        });
    }

    private void answer(Question query,int position) {
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        if(query.getType().equals(Constants.TEXT)){
            dialog.setContentView(R.layout.text_dialog);
            TextView questionText = dialog.findViewById(R.id.question_text);
            EditText answerfield = dialog.findViewById(R.id.answerfield);
            Button done = dialog.findViewById(R.id.done);

            questionText.setText(query.getQuestion_text());
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TextUtils.isEmpty(answerfield.getText().toString())){
                        Toast.makeText(context, "Please type and answer or enter NA", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Answer a = new Answer();
                    a.setAnswer(answerfield.getText().toString());
                    query.setAnswer(a);
                    dialog.dismiss();
                    notifyItemChanged(position);
                }
            });
            Button close = dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 10, 0, 10);

        if(query.getType().equals(Constants.MULTIPLE_OPTIONS)){
            dialog.setContentView(R.layout.options_dialog);
            TextView questionText = dialog.findViewById(R.id.question_text);
            LinearLayout optionLayout = dialog.findViewById(R.id.multiple_options_answer);
            JSONArray checkedanswers = new JSONArray();
            query.setAnswer(new Answer());
            try {
                JSONArray opts = new JSONArray(query.getOptions());
                for (int i = 0; i < opts.length(); i++) {
                    CheckBox cb = new CheckBox(context);
                    cb.setLayoutParams(p);
                    cb.setText(opts.getString(i));

                    View.OnClickListener l = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkedanswers.put((String) cb.getText());
                            query.getAnswer().setAnswer(checkedanswers.toString());
                        }
                    };
                    cb.setOnClickListener(l);
                    optionLayout.addView(cb);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Button done = dialog.findViewById(R.id.done);

            questionText.setText(query.getQuestion_text());
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    notifyItemChanged(position);
                }
            });

            Button close = dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        if (questionList.get(position).getType().equals(Constants.OPTION)) {
            dialog.setContentView(R.layout.option_dialog);
            TextView questionText = dialog.findViewById(R.id.question_text);
            RadioGroup options = dialog.findViewById(R.id.options_answer);
            query.setAnswer(new Answer());
            try {
                JSONArray opts = new JSONArray(questionList.get(position).getOptions());
                for (int i = 0; i < opts.length(); i++) {
                    RadioButton rb = new RadioButton(context);
                    rb.setLayoutParams(p);
                    rb.setText(opts.getString(i));
                    View.OnClickListener l = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            query.getAnswer().setAnswer((String) rb.getText());
                        }
                    };
                    rb.setOnClickListener(l);
                    options.addView(rb);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Button done = dialog.findViewById(R.id.done);

            questionText.setText(query.getQuestion_text());
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    notifyItemChanged(position);
                }
            });

            Button close = dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        if (questionList.get(position).getType().equals(Constants.LOCATION)) {
            query.setAnswer(new Answer());
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Getting Location");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
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
                    query.getAnswer().setAnswer(coords.toString());
                    notifyItemChanged(position);
                    progressDialog.dismiss();
                }
            });
        }

        if (questionList.get(position).getType().equals(Constants.IMAGE)) {
            captureImage((int) questionList.get(position).getId());
            notifyItemChanged(position);
        }
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

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView question,info;
        LinearLayout answerLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            info = itemView.findViewById(R.id.info);
            answerLayout = itemView.findViewById(R.id.answer_area);
        }
    }
}
