package com.wenruixing.spacecolony;

import androidx.annotation.NonNull;

public interface MainNavigator {
    void openHome();
    void openRecruit();
    void openQuarters();
    void openSimulator();
    void openMissionControl();
    void openMedbay();
    void openStatistics();
    void updateTitle(@NonNull String title);
}
