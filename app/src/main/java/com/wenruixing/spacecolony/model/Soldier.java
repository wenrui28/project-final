package com.wenruixing.spacecolony.model;

public class Soldier extends CrewMember {

    public Soldier(int id, String name) {
        super(id, name, CrewSpecialization.SOLDIER);
    }

    @Override
    public int getThreatBonus(Threat threat) {
        return threat.getType() == ThreatType.ALIEN_ATTACK ? 2 : 0;
    }
}
