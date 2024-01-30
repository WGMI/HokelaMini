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

import com.example.hokelamini.CampaignActivity;
import com.example.hokelamini.Models.Campaign;
import com.example.hokelamini.Models.Project;
import com.example.hokelamini.ProjectActivity;
import com.example.hokelamini.R;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.ViewHolder> {

    Context context;
    List<Campaign> campaignList;

    public CampaignAdapter(Context context, List<Campaign> campaignList) {
        this.context = context;
        this.campaignList = campaignList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.single_campaign,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Campaign campaign = campaignList.get(position);
        holder.title.setText(campaign.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(campaign.getCreated_at());
            holder.date.setText(new SimpleDateFormat("d MMM yyyy").format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CampaignActivity.class);
                i.putExtra("campaign_id",campaign.getId());
                i.putExtra("campaign_name",campaign.getName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return campaignList.size();
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
