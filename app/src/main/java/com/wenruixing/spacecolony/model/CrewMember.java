package com.wenruixing.spacecolony.model;

import java.io.Serializable;
import java.util.Random;

public abstract class CrewMember implements Serializable {

    private static final int DEFAULT_MORALE = 50;

    private final int id;
    private final String name;
    private final CrewSpecialization specialization;
    private final int baseSkill;
    private final int resilience;
    private final int maxEnergy;

    private int currentEnergy;
    private int experience;
    private CrewLocation location;
    private int missionsCompleted;
    private int missionsWon;
    private int trainingSessions;
    private int defeats;
    private int morale;

    protected CrewMember(int id, String name, CrewSpecialization specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.baseSkill = specialization.getBaseSkill();
        this.resilience = specialization.getResilience();
        this.maxEnergy = specialization.getMaxEnergy();
        this.currentEnergy = maxEnergy;
        this.experience = 0;
        this.location = CrewLocation.QUARTERS;
        this.morale = DEFAULT_MORALE;
    }

    public abstract int getThreatBonus(Threat threat);

    public int rollActionPower(Random random, Threat threat) {
        int randomBonus = random.nextInt(3);
        return getBaseSkill() + experience + getMoraleBonus() + getThreatBonus(threat) + randomBonus;
    }

    public int takeDamage(int incomingPower) {
        int damage = Math.max(1, incomingPower - resilience);
        currentEnergy = Math.max(0, currentEnergy - damage);
        return damage;
    }

    public void train() {
        experience += 1;
        trainingSessions += 1;
        adjustMorale(4);
    }

    public void healToFull() {
        currentEnergy = maxEnergy;
        adjustMorale(2);
    }

    public void recoverEnergy(int amount) {
        currentEnergy = Math.min(maxEnergy, currentEnergy + Math.max(0, amount));
    }

    public void adjustMorale(int change) {
        morale = Math.max(0, Math.min(100, morale + change));
    }

    public void loseExperience(int amount) {
        experience = Math.max(0, experience - Math.max(0, amount));
    }

    public void resetAfterDefeat() {
        loseExperience(1);
        currentEnergy = Math.max(1, maxEnergy / 2);
        adjustMorale(-15);
    }

    public boolean isAlive() {
        return currentEnergy > 0;
    }

    public void markMissionWin() {
        missionsCompleted += 1;
        missionsWon += 1;
        adjustMorale(6);
    }

    public void markMissionLoss() {
        defeats += 1;
        adjustMorale(-8);
    }

    public void gainMissionExperience() {
        experience += 1;
        adjustMorale(2);
    }

    public int getMoraleBonus() {
        if (morale >= 80) {
            return 2;
        }
        if (morale >= 60) {
            return 1;
        }
        return 0;
    }

    public CrewMemberRecord toRecord() {
        CrewMemberRecord record = new CrewMemberRecord();
        record.id = id;
        record.name = name;
        record.specialization = specialization.name();
        record.currentEnergy = currentEnergy;
        record.experience = experience;
        record.location = location.name();
        record.missionsCompleted = missionsCompleted;
        record.missionsWon = missionsWon;
        record.trainingSessions = trainingSessions;
        record.defeats = defeats;
        record.morale = morale;
        return record;
    }

    public void restoreFromRecord(CrewMemberRecord record) {
        this.currentEnergy = record.currentEnergy;
        this.experience = record.experience;
        this.location = CrewLocation.valueOf(record.location);
        this.missionsCompleted = record.missionsCompleted;
        this.missionsWon = record.missionsWon;
        this.trainingSessions = record.trainingSessions;
        this.defeats = record.defeats;
        this.morale = record.morale <= 0 ? DEFAULT_MORALE : record.morale;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CrewSpecialization getSpecialization() {
        return specialization;
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

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public int getExperience() {
        return experience;
    }

    public int getMorale() {
        return morale;
    }

    public int getTotalSkill() {
        return baseSkill + experience + getMoraleBonus();
    }

    public CrewLocation getLocation() {
        return location;
    }

    public void setLocation(CrewLocation location) {
        this.location = location;
    }

    public int getMissionsCompleted() {
        return missionsCompleted;
    }

    public int getMissionsWon() {
        return missionsWon;
    }

    public int getTrainingSessions() {
        return trainingSessions;
    }

    public int getDefeats() {
        return defeats;
    }
}
