package com.wenruixing.spacecolony.model;

import java.util.ArrayList;
import java.util.List;

public class MissionResult {
    private final List<String> logLines = new ArrayList<>();
    private final List<String> participantSummaryLines = new ArrayList<>();
    private final List<Integer> participantCurrentEnergy = new ArrayList<>();
    private final List<Integer> participantMaxEnergy = new ArrayList<>();
    private final Threat threat;
    private final boolean success;

    public MissionResult(Threat threat, boolean success) {
        this.threat = threat;
        this.success = success;
    }

    public void addLog(String line) {
        logLines.add(line);
    }

    public void addParticipantSnapshot(CrewMember crewMember, MissionAction action) {
        participantSummaryLines.add(crewMember.getName() + " - "
                + crewMember.getSpecialization().getDisplayName()
                + " - action " + action.getDisplayName()
                + " - energy " + crewMember.getCurrentEnergy() + "/" + crewMember.getMaxEnergy()
                + " - morale " + crewMember.getMorale());
        participantCurrentEnergy.add(crewMember.getCurrentEnergy());
        participantMaxEnergy.add(crewMember.getMaxEnergy());
    }

    public List<String> getLogLines() {
        return logLines;
    }

    public String getFullLog() {
        StringBuilder builder = new StringBuilder();
        for (String line : logLines) {
            builder.append(line).append("\n");
        }
        return builder.toString().trim();
    }

    public List<String> getParticipantSummaryLines() {
        return participantSummaryLines;
    }

    public List<Integer> getParticipantCurrentEnergy() {
        return participantCurrentEnergy;
    }

    public List<Integer> getParticipantMaxEnergy() {
        return participantMaxEnergy;
    }

    public Threat getThreat() {
        return threat;
    }

    public boolean isSuccess() {
        return success;
    }
}
