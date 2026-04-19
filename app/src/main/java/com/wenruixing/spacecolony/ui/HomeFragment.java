package com.wenruixing.spacecolony.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.wenruixing.spacecolony.MainNavigator;
import com.wenruixing.spacecolony.R;
import com.wenruixing.spacecolony.TitleAware;
import com.wenruixing.spacecolony.data.ColonyStorage;
import com.wenruixing.spacecolony.model.CrewLocation;
import com.wenruixing.spacecolony.util.UiUtils;

public class HomeFragment extends Fragment implements TitleAware {

    private MainNavigator navigator;
    private TextView summaryText;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (MainNavigator) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        summaryText = view.findViewById(R.id.home_summary_text);

        MaterialButton recruitButton = view.findViewById(R.id.button_open_recruit);
        MaterialButton quartersButton = view.findViewById(R.id.button_open_quarters);
        MaterialButton simulatorButton = view.findViewById(R.id.button_open_simulator);
        MaterialButton missionControlButton = view.findViewById(R.id.button_open_mission_control);
        MaterialButton medbayButton = view.findViewById(R.id.button_open_medbay);
        MaterialButton statisticsButton = view.findViewById(R.id.button_open_statistics);
        MaterialButton saveButton = view.findViewById(R.id.button_save_data);
        MaterialButton loadButton = view.findViewById(R.id.button_load_data);
        MaterialButton resetButton = view.findViewById(R.id.button_reset_data);

        recruitButton.setOnClickListener(v -> navigator.openRecruit());
        quartersButton.setOnClickListener(v -> navigator.openQuarters());
        simulatorButton.setOnClickListener(v -> navigator.openSimulator());
        missionControlButton.setOnClickListener(v -> navigator.openMissionControl());
        medbayButton.setOnClickListener(v -> navigator.openMedbay());
        statisticsButton.setOnClickListener(v -> navigator.openStatistics());

        saveButton.setOnClickListener(v -> {
            boolean saved = ColonyStorage.getInstance().saveManualFile();
            UiUtils.showToast(requireContext(),
                    saved ? "Manual save file created: manual_save.json"
                            : "Failed to save manual file.");
            refreshSummary();
        });

        loadButton.setOnClickListener(v -> {
            boolean loaded = ColonyStorage.getInstance().loadManualFile();
            UiUtils.showToast(requireContext(),
                    loaded ? "Manual save file loaded: manual_save.json"
                            : "No manual save file found.");
            refreshSummary();
        });

        resetButton.setOnClickListener(v -> {
            ColonyStorage.getInstance().resetAllData();
            UiUtils.showToast(requireContext(), "All colony data cleared.");
            refreshSummary();
        });

        refreshSummary();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        navigator.updateTitle(getScreenTitle());
        refreshSummary();
    }

    private void refreshSummary() {
        if (summaryText == null) {
            return;
        }
        ColonyStorage storage = ColonyStorage.getInstance();
        String summary = "Colony overview\n\n"
                + "Quarters: " + storage.countByLocation(CrewLocation.QUARTERS) + "\n"
                + "Simulator: " + storage.countByLocation(CrewLocation.SIMULATOR) + "\n"
                + "Mission Control: " + storage.countByLocation(CrewLocation.MISSION_CONTROL) + "\n"
                + "Medbay: " + storage.countByLocation(CrewLocation.MEDBAY) + "\n\n"
                + "Total recruited: " + storage.getTotalRecruited() + "\n"
                + "Total missions: " + storage.getTotalMissions() + "\n"
                + "Successful missions: " + storage.getSuccessfulMissions() + "\n"
                + "Failed missions: " + storage.getFailedMissions() + "\n"
                + "Average morale: " + storage.getAverageMorale() + "/100\n"
                + "Mission readiness: " + storage.getMissionReadinessPercent() + "%\n\n"
                + "Manual save file: manual_save.json\n"
                + "Automatic backup file: autosave.json";
        summaryText.setText(summary);
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.title_home);
    }
}
