package com.wenruixing.spacecolony.model;

public class Scientist extends CrewMember {

    public Scientist(int id, String name) {
        super(id, name, CrewSpecialization.SCIENTIST);
    }

    @Override
    public int getThreatBonus(Threat threat) {
        return threat.getType() == ThreatType.SOLAR_FLARE ? 2 : 0;
    }
}
