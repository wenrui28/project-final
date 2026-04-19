package com.wenruixing.spacecolony;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.wenruixing.spacecolony.data.ColonyStorage;
import com.wenruixing.spacecolony.ui.CrewListFragment;
import com.wenruixing.spacecolony.ui.HomeFragment;
import com.wenruixing.spacecolony.ui.RecruitFragment;
import com.wenruixing.spacecolony.ui.StatisticsFragment;

public class MainActivity extends AppCompatActivity implements MainNavigator {

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.top_app_bar);
        setSupportActionBar(toolbar);

        ColonyStorage.getInstance().initialize(getApplicationContext());

        if (savedInstanceState == null) {
            showRootFragment(HomeFragment.newInstance(), getString(R.string.title_home));
        }
    }

    @Override
    public void openHome() {
        showRootFragment(HomeFragment.newInstance(), getString(R.string.title_home));
    }

    @Override
    public void openRecruit() {
        showDetailFragment(RecruitFragment.newInstance(), getString(R.string.title_recruit));
    }

    @Override
    public void openQuarters() {
        showDetailFragment(CrewListFragment.newInstance(CrewListFragment.MODE_QUARTERS), getString(R.string.title_quarters));
    }

    @Override
    public void openSimulator() {
        showDetailFragment(CrewListFragment.newInstance(CrewListFragment.MODE_SIMULATOR), getString(R.string.title_simulator));
    }

    @Override
    public void openMissionControl() {
        showDetailFragment(CrewListFragment.newInstance(CrewListFragment.MODE_MISSION_CONTROL), getString(R.string.title_mission_control));
    }

    @Override
    public void openMedbay() {
        showDetailFragment(CrewListFragment.newInstance(CrewListFragment.MODE_MEDBAY), getString(R.string.title_medbay));
    }

    @Override
    public void openStatistics() {
        showDetailFragment(StatisticsFragment.newInstance(), getString(R.string.title_statistics));
    }

    @Override
    public void updateTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    private void showRootFragment(Fragment fragment, String title) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        updateTitle(title);
    }

    private void showDetailFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        updateTitle(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current instanceof TitleAware) {
            updateTitle(((TitleAware) current).getScreenTitle());
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            updateTitle(getString(R.string.title_home));
        }
    }
}
