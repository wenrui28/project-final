package com.wenruixing.spacecolony.model;

public enum ThreatType {
    ASTEROID_STORM("Asteroid Storm"),
    BROKEN_HEATING("Broken Heating"),
    MEDICAL_EMERGENCY("Medical Emergency"),
    SOLAR_FLARE("Solar Flare"),
    ALIEN_ATTACK("Alien Attack");

    private final String displayName;

    ThreatType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
