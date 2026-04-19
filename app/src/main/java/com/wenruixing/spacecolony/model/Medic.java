package com.wenruixing.spacecolony.model;

public class Medic extends CrewMember {

    public Medic(int id, String name) {
        super(id, name, CrewSpecialization.MEDIC);
    }

    @Override
    public int getThreatBonus(Threat threat) {
        return threat.getType() == ThreatType.MEDICAL_EMERGENCY ? 2 : 0;
    }
}
