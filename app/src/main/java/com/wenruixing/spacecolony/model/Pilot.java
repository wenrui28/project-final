package com.wenruixing.spacecolony.model;

public class Pilot extends CrewMember {

    public Pilot(int id, String name) {
        super(id, name, CrewSpecialization.PILOT);
    }

    @Override
    public int getThreatBonus(Threat threat) {
        return threat.getType() == ThreatType.ASTEROID_STORM ? 2 : 0;
    }
}
