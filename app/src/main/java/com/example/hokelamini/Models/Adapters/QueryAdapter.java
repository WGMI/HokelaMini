package com.example.hokelamini.Models.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hokelamini.Models.Answer;
import com.example.hokelamini.Models.Constants;
import com.example.hokelamini.Models.Question;
import com.example.hokelamini.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ViewHolder> {

    Context context;
    List<Question> questionList;

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

        holder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG_", "onClick: " + new Gson().toJson(query));
                answer(query);
            }
        });
    }

    private void answer(Question query) {
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        if(query.getType().equals(Constants.TEXT)){
            dialog.setContentView(R.layout.text_dialog);
            EditText answerfield = dialog.findViewById(R.id.answerfield);
            Button done = dialog.findViewById(R.id.done);
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
                }
            });

        }
        Button close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public QueryAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
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
