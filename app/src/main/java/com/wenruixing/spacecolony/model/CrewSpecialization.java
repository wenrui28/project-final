package com.wenruixing.spacecolony.model;

public enum CrewSpecialization {
    PILOT("Pilot", 5, 4, 20, "#3F51B5"),
    ENGINEER("Engineer", 6, 3, 19, "#F4B400"),
    MEDIC("Medic", 7, 2, 18, "#34A853"),
    SCIENTIST("Scientist", 8, 1, 17, "#8E24AA"),
    SOLDIER("Soldier", 9, 0, 16, "#EA4335");

    private final String displayName;
    private final int baseSkill;
    private final int resilience;
    private final int maxEnergy;
    private final String colorHex;

    CrewSpecialization(String displayName, int baseSkill, int resilience, int maxEnergy, String colorHex) {
        this.displayName = displayName;
        this.baseSkill = baseSkill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.colorHex = colorHex;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBaseSkill() {
        return baseSkill;
    }

    public int getResilience() {
        return resilience;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public String getColorHex() {
        return colorHex;
    }
}
