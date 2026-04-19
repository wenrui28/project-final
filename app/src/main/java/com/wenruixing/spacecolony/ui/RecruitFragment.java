package com.wenruixing.spacecolony.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.wenruixing.spacecolony.MainNavigator;
import com.wenruixing.spacecolony.R;
import com.wenruixing.spacecolony.TitleAware;
import com.wenruixing.spacecolony.data.ColonyStorage;
import com.wenruixing.spacecolony.model.CrewFactory;
import com.wenruixing.spacecolony.model.CrewMember;
import com.wenruixing.spacecolony.model.CrewSpecialization;
import com.wenruixing.spacecolony.util.UiUtils;

public class RecruitFragment extends Fragment implements TitleAware {

    private MainNavigator navigator;
    private EditText nameEditText;
    private Spinner specializationSpinner;
    private TextView statsText;

    public static RecruitFragment newInstance() {
        return new RecruitFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (MainNavigator) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruit, container, false);
        nameEditText = view.findViewById(R.id.recruit_name_input);
        specializationSpinner = view.findViewById(R.id.recruit_specialization_spinner);
        statsText = view.findViewById(R.id.recruit_stats_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (CrewSpecialization specialization : CrewSpecialization.values()) {
            adapter.add(specialization.getDisplayName());
        }
        specializationSpinner.setAdapter(adapter);
        specializationSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(this::updateStatsPreview));

        MaterialButton createButton = view.findViewById(R.id.button_create_crew);
        MaterialButton cancelButton = view.findViewById(R.id.button_cancel_recruit);
        createButton.setOnClickListener(v -> createCrewMember());
        cancelButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        updateStatsPreview(0);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        navigator.updateTitle(getScreenTitle());
    }

    private void updateStatsPreview(int index) {
        CrewSpecialization specialization = CrewSpecialization.values()[index];
        String preview = specialization.getDisplayName() + "\n"
                + "Base skill: " + specialization.getBaseSkill() + "\n"
                + "Resilience: " + specialization.getResilience() + "\n"
                + "Max energy: " + specialization.getMaxEnergy() + "\n"
                + "Specialization bonus: +2 on preferred missions";
        statsText.setText(preview);
    }

    private void createCrewMember() {
        String name = nameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            UiUtils.showToast(requireContext(), "Please enter a crew member name.");
            return;
        }
        CrewSpecialization specialization = CrewSpecialization.values()[specializationSpinner.getSelectedItemPosition()];
        ColonyStorage storage = ColonyStorage.getInstance();
        CrewMember crewMember = CrewFactory.create(storage.getNextIdAndAdvance(), name, specialization);
        storage.addCrewMember(crewMember);
        UiUtils.showToast(requireContext(), name + " recruited to Quarters.");
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public String getScreenTitle() {
        return getString(R.string.title_recruit);
    }
}
