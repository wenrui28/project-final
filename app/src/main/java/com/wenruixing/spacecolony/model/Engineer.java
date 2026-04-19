package com.wenruixing.spacecolony.model;

public class Engineer extends CrewMember {

    public Engineer(int id, String name) {
        super(id, name, CrewSpecialization.ENGINEER);
    }

    @Override
    public int getThreatBonus(Threat threat) {
        return threat.getType() == ThreatType.BROKEN_HEATING ? 2 : 0;
    }
}
