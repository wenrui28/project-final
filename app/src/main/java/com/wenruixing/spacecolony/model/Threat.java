package com.wenruixing.spacecolony.model;

import java.io.Serializable;
import java.util.Random;

public class Threat implements Serializable {

    private final ThreatType type;
    private final String name;
    private final int skill;
    private final int resilience;
    private final int maxEnergy;
    private int currentEnergy;

    public Threat(ThreatType type, int skill, int resilience, int maxEnergy) {
        this.type = type;
        this.name = type.getDisplayName();
        this.skill = skill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
    }

    public static Threat generate(int completedMissions, Random random) {
        ThreatType[] values = ThreatType.values();
        ThreatType type = values[random.nextInt(values.length)];
        int skill = 4 + completedMissions;
        int resilience = 1 + (completedMissions / 2);
        int maxEnergy = 18 + (completedMissions * 3);
        return new Threat(type, skill, resilience, maxEnergy);
    }

    public int takeDamage(int incomingPower) {
        int damage = Math.max(1, incomingPower - resilience);
        currentEnergy = Math.max(0, currentEnergy - damage);
        return damage;
    }

    public int rollAttack(Random random) {
        return skill + random.nextInt(3);
    }

    public boolean isAlive() {
        return currentEnergy > 0;
    }

    public ThreatType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getSkill() {
        return skill;
    }

    public int getResilience() {
        return resilience;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }
}
