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

import com.google.android.material.button.MaterialButton;
import com.wenruixing.spacecolony.MainNavigator;
import com.wenruixing.spacecolony.R;
import com.wenruixing.spacecolony.TitleAware;
import com.wenruixing.spacecolony.adapter.CrewAdapter;
import com.wenruixing.spacecolony.data.ColonyStorage;
import com.wenruixing.spacecolony.data.MissionEngine;
import com.wenruixing.spacecolony.model.CrewLocation;
import com.wenruixing.spacecolony.model.CrewMember;
import com.wenruixing.spacecolony.model.MissionAction;
import com.wenruixing.spacecolony.model.MissionResult;
import com.wenruixing.spacecolony.model.Threat;
import com.wenruixing.spacecolony.util.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrewListFragment extends Fragment implements TitleAware {

    public static final String ARG_MODE = "mode";
    public static final String MODE_QUARTERS = "quarters";
    public static final String MODE_SIMULATOR = "simulator";
    public static final String MODE_MISSION_CONTROL = "mission_control";
    public static final String MODE_MEDBAY = "medbay";

    private MainNavigator navigator;
    private String mode;
    private TextView headerText;
    private RecyclerView recyclerView;
    private MaterialButton primaryButton;
    private MaterialButton secondaryButton;
    private MaterialButton tertiaryButton;
    private TextView selectionHint;
    private TextView missionIntelText;
    private View missionResultCard;
    private TextView missionStatusText;
    private TextView missionThreatText;
    private ProgressBar missionThreatBar;
    private TextView missionOutcomeSummary;
    private TextView missionCrewOne;
    private ProgressBar missionCrewOneBar;
    private TextView missionCrewTwo;
    private ProgressBar missionCrewTwoBar;
    private TextView missionCrewThree;
    private ProgressBar missionCrewThreeBar;
    private TextView missionLogText;
    private CrewAdapter crewAdapter;

    public static CrewListFragment newInstance(String mode) {
        CrewListFragment fragment = new CrewListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_MODE, mode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (MainNavigator) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crew_list, container, false);
        mode = requireArguments().getString(ARG_MODE, MODE_QUARTERS);
        headerText = view.findViewById(R.id.crew_list_header_text);
        recyclerView = view.findViewById(R.id.crew_recycler_view);
        primaryButton = view.findViewById(R.id.crew_primary_button);
        secondaryButton = view.findViewById(R.id.crew_secondary_button);
        tertiaryButton = view.findViewById(R.id.crew_tertiary_button);
        selectionHint = view.findViewById(R.id.crew_selection_hint);
        missionIntelText = view.findViewById(R.id.mission_intel_text);
        missionResultCard = view.findViewById(R.id.mission_result_card);
        missionStatusText = view.findViewById(R.id.mission_status_text);
        missionThreatText = view.findViewById(R.id.mission_threat_text);
        missionThreatBar = view.findViewById(R.id.mission_threat_bar);
        missionOutcomeSummary = view.findViewById(R.id.mission_outcome_summary);
        missionCrewOne = view.findViewById(R.id.mission_crew_one);
        missionCrewOneBar = view.findViewById(R.id.mission_crew_one_bar);
        missionCrewTwo = view.findViewById(R.id.mission_crew_two);
        missionCrewTwoBar = view.findViewById(R.id.mission_crew_two_bar);
        missionCrewThree = view.findViewById(R.id.mission_crew_three);
        missionCrewThreeBar = view.findViewById(R.id.mission_crew_three_bar);
        missionLogText = view.findViewById(R.id.mission_log_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bindButtons();
        refreshList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        navigator.updateTitle(getScreenTitle());
        refreshList();
    }

    private void bindButtons() {
        missionResultCard.setVisibility(View.GONE);
        missionIntelText.setVisibility(View.GONE);
        tertiaryButton.setVisibility(View.VISIBLE);
        if (MODE_QUARTERS.equals(mode)) {
            headerText.setText("Crew members currently resting in Quarters.");
            selectionHint.setText("Select one or more crew members.");
            primaryButton.setText("Move to Simulator");
            secondaryButton.setText("Move to Mission Control");
            tertiaryButton.setText("Back Home");
            primaryButton.setOnClickListener(v -> moveSelected(CrewLocation.SIMULATOR));
            secondaryButton.setOnClickListener(v -> moveSelected(CrewLocation.MISSION_CONTROL));
            tertiaryButton.setOnClickListener(v -> navigator.openHome());
        } else if (MODE_SIMULATOR.equals(mode)) {
            headerText.setText("Train selected crew members to increase their experience and morale.");
            selectionHint.setText("Select one or more crew members.");
            primaryButton.setText("Train Selected");
            secondaryButton.setText("Return to Quarters");
            tertiaryButton.setText("Back Home");
            primaryButton.setOnClickListener(v -> trainSelected());
            secondaryButton.setOnClickListener(v -> moveSelected(CrewLocation.QUARTERS));
            tertiaryButton.setOnClickListener(v -> navigator.openHome());
        } else if (MODE_MEDBAY.equals(mode)) {
            headerText.setText("Defeated crew members rest in Medbay. Send them back to Quarters when you are ready.");
            selectionHint.setText("Select one or more crew members.");
            primaryButton.setText("Release to Quarters");
            secondaryButton.setText("Back Home");
            tertiaryButton.setVisibility(View.GONE);
            primaryButton.setOnClickListener(v -> moveSelected(CrewLocation.QUARTERS));
            secondaryButton.setOnClickListener(v -> navigator.openHome());
        } else {
            headerText.setText("Select two or three crew members, assign tactical actions, and launch a cooperative mission.");
            selectionHint.setText("Choose two or three crew members. Set each member to Attack, Defend, or Special.");
            missionIntelText.setVisibility(View.VISIBLE);
            primaryButton.setText("Launch Tactical Mission");
            secondaryButton.setText("Return to Quarters");
            tertiaryButton.setText("Back Home");
            primaryButton.setOnClickListener(v -> launchMission());
            secondaryButton.setOnClickListener(v -> moveSelected(CrewLocation.QUARTERS));
            tertiaryButton.setOnClickListener(v -> navigator.openHome());
        }
    }

    private void refreshList() {
        ColonyStorage storage = ColonyStorage.getInstance();
        CrewLocation location = resolveLocation();
        List<CrewMember> crewMembers = storage.getCrewByLocation(location);
        int maxSelectable = MODE_MISSION_CONTROL.equals(mode) ? 3 : 0;
        boolean missionMode = MODE_MISSION_CONTROL.equals(mode);
        crewAdapter = new CrewAdapter(crewMembers, maxSelectable, missionMode, selected -> { });
        recyclerView.setAdapter(crewAdapter);
        if (missionMode) {
            missionIntelText.setText(buildMissionIntel(crewMembers));
        }
    }

    private String buildMissionIntel(List<CrewMember> crewMembers) {
        if (crewMembers.isEmpty()) {
            return "Mission Intel\n\nNo crew are currently waiting in Mission Control.";
        }
        CrewMember recommended = crewMembers.get(0);
        for (CrewMember crewMember : crewMembers) {
            if (crewMember.getTotalSkill() + crewMember.getMoraleBonus() > recommended.getTotalSkill() + recommended.getMoraleBonus()) {
                recommended = crewMember;
            }
        }
        return "Mission Intel\n\n"
                + "Custom feature: the Commander Recommendation System scans the crew in Mission Control and highlights the strongest current option.\n"
                + "Recommended leader: " + recommended.getName() + " (" + recommended.getSpecialization().getDisplayName() + ")\n"
                + "Current morale average: " + ColonyStorage.getInstance().getAverageMorale() + "/100\n"
                + "Tip: combine at least one Special action with one Defend action for safer missions.";
    }

    private CrewLocation resolveLocation() {
        if (MODE_SIMULATOR.equals(mode)) {
            return CrewLocation.SIMULATOR;
        }
        if (MODE_MISSION_CONTROL.equals(mode)) {
            return CrewLocation.MISSION_CONTROL;
        }
        if (MODE_MEDBAY.equals(mode)) {
            return CrewLocation.MEDBAY;
        }
        return CrewLocation.QUARTERS;
    }

    private void moveSelected(CrewLocation destination) {
        Set<Integer> selectedIds = crewAdapter.getSelectedIds();
        if (selectedIds.isEmpty()) {
            UiUtils.showToast(requireContext(), "Please select at least one crew member.");
            return;
        }
        ColonyStorage.getInstance().moveCrew(selectedIds, destination);
        UiUtils.showToast(requireContext(), "Crew moved to " + destination.getDisplayName() + ".");
        refreshList();
    }

    private void trainSelected() {
        Set<Integer> selectedIds = crewAdapter.getSelectedIds();
        if (selectedIds.isEmpty()) {
            UiUtils.showToast(requireContext(), "Please select at least one crew member.");
            return;
        }
        ColonyStorage.getInstance().trainCrew(selectedIds);
        UiUtils.showToast(requireContext(), "Selected crew members trained successfully.");
        refreshList();
    }

    private void launchMission() {
        Set<Integer> selectedIds = crewAdapter.getSelectedIds();
        if (selectedIds.size() < 2 || selectedIds.size() > 3) {
            UiUtils.showToast(requireContext(), "Please select two or three crew members.");
            return;
        }

        List<CrewMember> participants = new ArrayList<>();
        for (Integer id : selectedIds) {
            CrewMember crewMember = ColonyStorage.getInstance().getCrew(id);
            if (crewMember != null) {
                participants.add(crewMember);
            }
        }
        if (participants.size() < 2 || participants.size() > 3) {
            UiUtils.showToast(requireContext(), "Selected crew members could not be loaded.");
            return;
        }

        Map<Integer, MissionAction> actionPlan = crewAdapter.getActionMap();
        MissionResult result = MissionEngine.runMission(participants,
                ColonyStorage.getInstance().getTotalMissions(),
                actionPlan);
        ColonyStorage.getInstance().applyMissionResult(participants, result);
        showMissionResult(result);
        refreshList();
    }

    private void showMissionResult(MissionResult result) {
        Threat threat = result.getThreat();
        missionResultCard.setVisibility(View.VISIBLE);
        missionStatusText.setText(result.isSuccess() ? "Mission success" : "Mission failed");
        missionThreatText.setText(threat.getName() + " | final energy " + threat.getCurrentEnergy() + "/" + threat.getMaxEnergy());
        missionThreatBar.setMax(threat.getMaxEnergy());
        missionThreatBar.setProgress(threat.getCurrentEnergy());
        missionOutcomeSummary.setText(result.isSuccess()
                ? "Mission visualization: threat bar, final crew energy bars, tactical log, and summary card updated dynamically."
                : "No Death bonus active: downed crew members move to Medbay instead of being deleted from the colony.");
        bindCrewOutcome(missionCrewOne, missionCrewOneBar, result, 0, true);
        bindCrewOutcome(missionCrewTwo, missionCrewTwoBar, result, 1, true);
        bindCrewOutcome(missionCrewThree, missionCrewThreeBar, result, 2, result.getParticipantSummaryLines().size() > 2);
        missionLogText.setText(result.getFullLog());
    }

    private void bindCrewOutcome(TextView textView,
                                 ProgressBar progressBar,
                                 MissionResult result,
                                 int index,
                                 boolean visible) {
        if (!visible || index >= result.getParticipantSummaryLines().size()) {
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText(result.getParticipantSummaryLines().get(index));
        progressBar.setMax(result.getParticipantMaxEnergy().get(index));
        progressBar.setProgress(result.getParticipantCurrentEnergy().get(index));
    }

    @Override
    public String getScreenTitle() {
        if (MODE_SIMULATOR.equals(mode)) {
            return getString(R.string.title_simulator);
        }
        if (MODE_MISSION_CONTROL.equals(mode)) {
            return getString(R.string.title_mission_control);
        }
        if (MODE_MEDBAY.equals(mode)) {
            return getString(R.string.title_medbay);
        }
        return getString(R.string.title_quarters);
    }
}
