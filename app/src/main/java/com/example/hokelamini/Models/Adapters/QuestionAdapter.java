package com.example.hokelamini.Models.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hokelamini.Models.Question;
import com.example.hokelamini.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    Context context;
    List<Question> questionList;

    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_question,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.question.setText(question.getQuestion_text());

        boolean isExpanded = question.isExpanded();

        if(questionList.get(position).getOptions() != null && questionList.get(position).getOptions().length() > 0){
            holder.options.setVisibility(View.VISIBLE);
            holder.options.removeAllViews();
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            try {
                JSONArray opts = new JSONArray(questionList.get(position).getOptions());
                CharSequence[] cs = new CharSequence[opts.length()];
                for(int i=0;i<opts.length();i++){
                    RadioButton rb = new RadioButton(context);
                    rb.setLayoutParams(p);
                    rb.setText(opts.getString(i));
                    holder.options.addView(rb);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            holder.options.setVisibility(View.GONE);
        }

        holder.answer.setVisibility(question.getType().equals("text") ? View.VISIBLE : View.GONE);
        holder.imageaction.setVisibility(question.getType().equals("image") ? View.VISIBLE : View.GONE);
        holder.locationaction.setVisibility(question.getType().equals("location") ? View.VISIBLE : View.GONE);
        holder.answerLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView question;
        LinearLayout answerLayout;
        EditText answer;
        RadioGroup options;
        Button imageaction,locationaction;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answerLayout = itemView.findViewById(R.id.answer_area);
            answer = itemView.findViewById(R.id.text_answer);
            options = itemView.findViewById(R.id.options_answer);
            imageaction = itemView.findViewById(R.id.image_action);
            locationaction = itemView.findViewById(R.id.location_action);

            question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Question q = questionList.get(getAdapterPosition());
                    q.setExpanded(!q.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
