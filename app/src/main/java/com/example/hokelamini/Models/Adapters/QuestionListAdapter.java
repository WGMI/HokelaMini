package com.example.hokelamini.Models.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.hokelamini.Models.Answer;
import com.example.hokelamini.Models.Constants;
import com.example.hokelamini.Models.Question;
import com.example.hokelamini.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class QuestionListAdapter extends ArrayAdapter<Question> {

    Context context;
    Activity activity;
    List<Question> questionList;

    public QuestionListAdapter(Context context, List<Question> questionList){
        super(context, R.layout.single_question, questionList);
        this.context = context;
        this.questionList = questionList;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Question question = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_question, parent, false);
            viewHolder.question = (TextView) convertView.findViewById(R.id.question);
            viewHolder.info = convertView.findViewById(R.id.info);
            viewHolder.answerLayout = convertView.findViewById(R.id.answer_area);
            viewHolder.answer = convertView.findViewById(R.id.text_answer);
            viewHolder.options = convertView.findViewById(R.id.options_answer);
            viewHolder.multipleOptions = convertView.findViewById(R.id.multiple_options_answer);
            viewHolder.imageaction = convertView.findViewById(R.id.image_action);
            viewHolder.locationaction = convertView.findViewById(R.id.location_action);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        if (questionList.get(position).getOptions() != null && questionList.get(position).getOptions().length() > 0) {
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 10, 0, 10);
            if (questionList.get(position).getType().equals(Constants.OPTION)) {
                viewHolder.multipleOptions.setVisibility(View.GONE);
                viewHolder.options.setVisibility(View.VISIBLE);
                viewHolder.options.removeAllViews();
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
                        viewHolder.options.addView(rb);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (questionList.get(position).getType().equals(Constants.MULTIPLE_OPTIONS)) {
                viewHolder.options.setVisibility(View.GONE);
                viewHolder.multipleOptions.setVisibility(View.VISIBLE);
                viewHolder.multipleOptions.removeAllViews();
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
                        viewHolder.multipleOptions.addView(cb);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            viewHolder.options.setVisibility(View.GONE);
            viewHolder.multipleOptions.setVisibility(View.GONE);
        }

        viewHolder.answer.setVisibility(question.getType().equals(Constants.TEXT) ? View.VISIBLE : View.GONE);
        viewHolder.imageaction.setVisibility(question.getType().equals(Constants.IMAGE) ? View.VISIBLE : View.GONE);
        viewHolder.locationaction.setVisibility(question.getType().equals(Constants.LOCATION) ? View.VISIBLE : View.GONE);

        /*viewHolder.imageaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage((int) questionList.get(position).getId());
            }
        });*/

        Answer a = new Answer();

        viewHolder.locationaction.setOnClickListener(new View.OnClickListener() {
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
                        viewHolder.info.setVisibility(View.VISIBLE);
                        viewHolder.info.setText("Location Captured");
                        dialog.dismiss();
                    }
                });
            }
        });

        if (question.getType().equals(Constants.TEXT)) {
            viewHolder.answer.addTextChangedListener(new TextWatcher() {
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

        return convertView;
    }

    class ViewHolder {
        TextView question,info;
        LinearLayout answerLayout;
        EditText answer;
        RadioGroup options;
        LinearLayout multipleOptions;
        Button imageaction,locationaction/*,answercontrol*/;
    }
}
