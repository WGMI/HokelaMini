package com.example.hokelamini.Models.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hokelamini.Models.Survey;
import com.example.hokelamini.R;
import com.example.hokelamini.ReportActivity;
import com.example.hokelamini.SurveyActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

    Context context;
    List<Survey> surveyList;

    public SurveyAdapter(Context context, List<Survey> surveyList) {
        this.context = context;
        this.surveyList = surveyList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.single_survey,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Survey survey = surveyList.get(position);
        holder.title.setText(survey.getName());
        holder.date.setText(survey.getCreated_at());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SurveyActivity.class);
                //Intent i = new Intent(context, ReportActivity.class);
                i.putExtra("survey_id",survey.getId());
                i.putExtra("survey_name",survey.getName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView title,date;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.layout);
            this.title = itemView.findViewById(R.id.title);
            this.date = itemView.findViewById(R.id.date);
        }
    }
}
