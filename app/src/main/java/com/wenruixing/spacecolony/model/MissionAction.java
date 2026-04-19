package com.wenruixing.spacecolony.model;

public enum MissionAction {
    ATTACK("Attack"),
    DEFEND("Defend"),
    SPECIAL("Special");

    private final String displayName;

    MissionAction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
