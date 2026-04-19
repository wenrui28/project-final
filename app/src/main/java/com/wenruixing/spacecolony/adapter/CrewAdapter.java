package com.wenruixing.spacecolony.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wenruixing.spacecolony.R;
import com.wenruixing.spacecolony.model.CrewMember;
import com.wenruixing.spacecolony.model.MissionAction;
import com.wenruixing.spacecolony.util.UiUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    public interface SelectionChangedListener {
        void onSelectionChanged(Set<Integer> selectedIds);
    }

    private final List<CrewMember> crewMembers;
    private final Set<Integer> selectedIds = new HashSet<>();
    private final Map<Integer, MissionAction> actionMap = new HashMap<>();
    private final int maxSelectable;
    private final boolean missionMode;
    private final SelectionChangedListener listener;

    public CrewAdapter(List<CrewMember> crewMembers,
                       int maxSelectable,
                       boolean missionMode,
                       SelectionChangedListener listener) {
        this.crewMembers = crewMembers;
        this.maxSelectable = maxSelectable;
        this.missionMode = missionMode;
        this.listener = listener;
        for (CrewMember crewMember : crewMembers) {
            actionMap.put(crewMember.getId(), MissionAction.ATTACK);
        }
    }

    public Set<Integer> getSelectedIds() {
        return new HashSet<>(selectedIds);
    }

    public Map<Integer, MissionAction> getActionMap() {
        return new HashMap<>(actionMap);
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crew, parent, false);
        return new CrewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        CrewMember crewMember = crewMembers.get(position);
        holder.nameText.setText("#" + crewMember.getId() + " " + crewMember.getName());
        holder.specializationText.setText(crewMember.getSpecialization().getDisplayName());
        UiUtils.tintLabel(holder.specializationText, crewMember.getSpecialization().getColorHex());
        holder.locationText.setText("Location: " + crewMember.getLocation().getDisplayName());
        holder.statsText.setText("Skill " + crewMember.getTotalSkill()
                + "  |  Resilience " + crewMember.getResilience()
                + "  |  XP " + crewMember.getExperience()
                + "  |  Training " + crewMember.getTrainingSessions());
        holder.energyText.setText("Energy: " + crewMember.getCurrentEnergy() + "/" + crewMember.getMaxEnergy());
        holder.energyBar.setMax(crewMember.getMaxEnergy());
        holder.energyBar.setProgress(crewMember.getCurrentEnergy());
        holder.moraleText.setText("Morale: " + crewMember.getMorale() + "/100 (+" + crewMember.getMoraleBonus() + " skill bonus)");
        holder.moraleBar.setMax(100);
        holder.moraleBar.setProgress(crewMember.getMorale());
        holder.avatarView.setImageResource(resolveIconRes(crewMember));

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedIds.contains(crewMember.getId()));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (maxSelectable > 0 && selectedIds.size() >= maxSelectable) {
                    buttonView.setChecked(false);
                    return;
                }
                selectedIds.add(crewMember.getId());
            } else {
                selectedIds.remove(crewMember.getId());
            }
            listener.onSelectionChanged(getSelectedIds());
        });

        if (missionMode) {
            holder.tacticalPanel.setVisibility(View.VISIBLE);
            MissionAction currentAction = actionMap.containsKey(crewMember.getId())
                    ? actionMap.get(crewMember.getId()) : MissionAction.ATTACK;
            holder.attackButton.setChecked(currentAction == MissionAction.ATTACK);
            holder.defendButton.setChecked(currentAction == MissionAction.DEFEND);
            holder.specialButton.setChecked(currentAction == MissionAction.SPECIAL);

            View.OnClickListener clickListener = v -> {
                if (v == holder.attackButton) {
                    actionMap.put(crewMember.getId(), MissionAction.ATTACK);
                    holder.attackButton.setChecked(true);
                    holder.defendButton.setChecked(false);
                    holder.specialButton.setChecked(false);
                } else if (v == holder.defendButton) {
                    actionMap.put(crewMember.getId(), MissionAction.DEFEND);
                    holder.attackButton.setChecked(false);
                    holder.defendButton.setChecked(true);
                    holder.specialButton.setChecked(false);
                } else {
                    actionMap.put(crewMember.getId(), MissionAction.SPECIAL);
                    holder.attackButton.setChecked(false);
                    holder.defendButton.setChecked(false);
                    holder.specialButton.setChecked(true);
                }
            };
            holder.attackButton.setOnClickListener(clickListener);
            holder.defendButton.setOnClickListener(clickListener);
            holder.specialButton.setOnClickListener(clickListener);
        } else {
            holder.tacticalPanel.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> holder.checkBox.performClick());
    }

    @Override
    public int getItemCount() {
        return crewMembers.size();
    }

    private int resolveIconRes(CrewMember crewMember) {
        switch (crewMember.getSpecialization()) {
            case PILOT:
                return android.R.drawable.ic_menu_compass;
            case ENGINEER:
                return android.R.drawable.ic_menu_manage;
            case MEDIC:
                return android.R.drawable.ic_menu_info_details;
            case SCIENTIST:
                return android.R.drawable.ic_menu_search;
            case SOLDIER:
            default:
                return android.R.drawable.ic_lock_lock;
        }
    }

    static class CrewViewHolder extends RecyclerView.ViewHolder {
        final CheckBox checkBox;
        final ImageView avatarView;
        final TextView nameText;
        final TextView specializationText;
        final TextView locationText;
        final TextView statsText;
        final TextView energyText;
        final ProgressBar energyBar;
        final TextView moraleText;
        final ProgressBar moraleBar;
        final View tacticalPanel;
        final RadioButton attackButton;
        final RadioButton defendButton;
        final RadioButton specialButton;

        CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.item_select_box);
            avatarView = itemView.findViewById(R.id.item_avatar);
            nameText = itemView.findViewById(R.id.item_name_text);
            specializationText = itemView.findViewById(R.id.item_specialization_text);
            locationText = itemView.findViewById(R.id.item_location_text);
            statsText = itemView.findViewById(R.id.item_stats_text);
            energyText = itemView.findViewById(R.id.item_energy_text);
            energyBar = itemView.findViewById(R.id.item_energy_bar);
            moraleText = itemView.findViewById(R.id.item_morale_text);
            moraleBar = itemView.findViewById(R.id.item_morale_bar);
            tacticalPanel = itemView.findViewById(R.id.item_tactical_panel);
            attackButton = itemView.findViewById(R.id.item_action_attack);
            defendButton = itemView.findViewById(R.id.item_action_defend);
            specialButton = itemView.findViewById(R.id.item_action_special);
        }
    }
}
