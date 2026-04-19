package com.wenruixing.spacecolony.model;

public enum CrewLocation {
    QUARTERS("Quarters"),
    SIMULATOR("Simulator"),
    MISSION_CONTROL("Mission Control"),
    MEDBAY("Medbay");

    private final String displayName;

    CrewLocation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
