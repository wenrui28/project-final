package com.wenruixing.spacecolony.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wenruixing.spacecolony.MainNavigator;
import com.wenruixing.spacecolony.R;
import com.wenruixing.spacecolony.TitleAware;
import com.wenruixing.spacecolony.adapter.StatisticsAdapter;
import com.wenruixing.spacecolony.data.ColonyStorage;
import com.wenruixing.spacecolony.model.CrewLocation;

public class StatisticsFragment extends Fragment implements TitleAware {

    private MainNavigator navigator;
    private TextView colonyStatsText;
    private ProgressBar winRateBar;
    private TextView winRateText;
    private ProgressBar readinessBar;
    private TextView readinessText;
    private ProgressBar moraleBar;
    private TextView moraleText;
    private RecyclerView recyclerView;

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (MainNavigator) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        colonyStatsText = view.findViewById(R.id.statistics_summary_text);
        winRateBar = view.findViewById(R.id.statistics_win_rate_bar);
        winRateText = view.findViewById(R.id.statistics_win_rate_text);
        readinessBar = view.findViewById(R.id.statistics_readiness_bar);
        readinessText = view.findViewById(R.id.statistics_readiness_text);
        moraleBar = view.findViewById(R.id.statistics_morale_bar);
        moraleText = view.findViewById(R.id.statistics_morale_text);
        recyclerView = view.findViewById(R.id.statistics_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setNestedScrollingEnabled(false);
        refresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        navigator.updateTitle(getScreenTitle());
        refresh();
    }

    private void refresh() {
        ColonyStorage storage = ColonyStorage.getInstance();
        String summary = "Colony-wide statistics\n\n"
                + "Total recruited: " + storage.getTotalRecruited() + "\n"
                + "Total missions: " + storage.getTotalMissions() + "\n"
                + "Successful missions: " + storage.getSuccessfulMissions() + "\n"
                + "Failed missions: " + storage.getFailedMissions() + "\n\n"
                + "Current crew in Quarters: " + storage.countByLocation(CrewLocation.QUARTERS) + "\n"
                + "Current crew in Simulator: " + storage.countByLocation(CrewLocation.SIMULATOR) + "\n"
                + "Current crew in Mission Control: " + storage.countByLocation(CrewLocation.MISSION_CONTROL) + "\n"
                + "Current crew in Medbay: " + storage.countByLocation(CrewLocation.MEDBAY);
        colonyStatsText.setText(summary);

        int winRate = storage.getWinRatePercent();
        int readiness = storage.getMissionReadinessPercent();
        int morale = storage.getAverageMorale();

        winRateBar.setMax(100);
        winRateBar.setProgress(winRate);
        winRateText.setText("Win rate visualization: " + winRate + "%");

        readinessBar.setMax(100);
        readinessBar.setProgress(readiness);
        readinessText.setText("Mission readiness visualization: " + readiness + "%");

        moraleBar.setMax(100);
        moraleBar.setProgress(morale);
        moraleText.setText("Average morale visualization: " + morale + "/100");

        recyclerView.setAdapter(new StatisticsAdapter(storage.getAllCrew()));
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.title_statistics);
    }
}
