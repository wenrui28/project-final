package com.wenruixing.spacecolony.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wenruixing.spacecolony.R;
import com.wenruixing.spacecolony.model.CrewMember;
import com.wenruixing.spacecolony.util.UiUtils;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {

    private final List<CrewMember> crewMembers;

    public StatisticsAdapter(List<CrewMember> crewMembers) {
        this.crewMembers = crewMembers;
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistics, parent, false);
        return new StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position) {
        CrewMember crewMember = crewMembers.get(position);
        holder.nameText.setText("#" + crewMember.getId() + " " + crewMember.getName());
        holder.roleText.setText(crewMember.getSpecialization().getDisplayName());
        UiUtils.tintLabel(holder.roleText, crewMember.getSpecialization().getColorHex());
        holder.summaryText.setText("Missions: " + crewMember.getMissionsCompleted()
                + " | Wins: " + crewMember.getMissionsWon()
                + " | Training: " + crewMember.getTrainingSessions()
                + " | Defeats: " + crewMember.getDefeats()
                + " | Morale: " + crewMember.getMorale() + "/100");
    }

    @Override
    public int getItemCount() {
        return crewMembers.size();
    }

    static class StatisticsViewHolder extends RecyclerView.ViewHolder {
        final TextView nameText;
        final TextView roleText;
        final TextView summaryText;

        StatisticsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.stats_item_name);
            roleText = itemView.findViewById(R.id.stats_item_role);
            summaryText = itemView.findViewById(R.id.stats_item_summary);
        }
    }
}
