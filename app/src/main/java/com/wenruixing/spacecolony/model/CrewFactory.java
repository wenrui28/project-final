package com.wenruixing.spacecolony.model;

public final class CrewFactory {

    private CrewFactory() {
    }

    public static CrewMember create(int id, String name, CrewSpecialization specialization) {
        switch (specialization) {
            case PILOT:
                return new Pilot(id, name);
            case ENGINEER:
                return new Engineer(id, name);
            case MEDIC:
                return new Medic(id, name);
            case SCIENTIST:
                return new Scientist(id, name);
            case SOLDIER:
            default:
                return new Soldier(id, name);
        }
    }
}
