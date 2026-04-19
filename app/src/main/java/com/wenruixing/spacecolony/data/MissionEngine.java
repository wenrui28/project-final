package com.wenruixing.spacecolony.data;

import com.wenruixing.spacecolony.model.CrewMember;
import com.wenruixing.spacecolony.model.MissionAction;
import com.wenruixing.spacecolony.model.MissionResult;
import com.wenruixing.spacecolony.model.Threat;

import java.util.List;
import java.util.Map;
import java.util.Random;

public final class MissionEngine {

    private MissionEngine() {
    }

    public static MissionResult runMission(List<CrewMember> participants,
                                           int completedMissions,
                                           Map<Integer, MissionAction> actionPlan) {
        Random random = new Random();
        Threat threat = Threat.generate(completedMissions, random);
        MissionResult result = new MissionResult(threat, false);

        result.addLog("=== MISSION START ===");
        result.addLog("Threat: " + threat.getName()
                + " | skill " + threat.getSkill()
                + " | resilience " + threat.getResilience()
                + " | energy " + threat.getCurrentEnergy() + "/" + threat.getMaxEnergy());

        for (CrewMember crewMember : participants) {
            MissionAction action = actionPlan.containsKey(crewMember.getId())
                    ? actionPlan.get(crewMember.getId()) : MissionAction.ATTACK;
            result.addLog(crewMember.getSpecialization().getDisplayName() + "(" + crewMember.getName() + ")"
                    + " | skill " + crewMember.getTotalSkill()
                    + " | resilience " + crewMember.getResilience()
                    + " | xp " + crewMember.getExperience()
                    + " | morale " + crewMember.getMorale()
                    + " | action " + action.getDisplayName());
        }

        int round = 1;
        while (threat.isAlive() && hasAnyAliveCrew(participants)) {
            result.addLog("--- Round " + round + " ---");
            for (CrewMember crewMember : participants) {
                if (!crewMember.isAlive()) {
                    continue;
                }

                MissionAction action = actionPlan.containsKey(crewMember.getId())
                        ? actionPlan.get(crewMember.getId()) : MissionAction.ATTACK;
                int guardBonus = 0;

                if (action == MissionAction.DEFEND) {
                    crewMember.recoverEnergy(2);
                    guardBonus = 3;
                    result.addLog(crewMember.getName() + " takes a defensive stance, recovers 2 energy, and prepares to block incoming damage.");
                } else if (action == MissionAction.SPECIAL) {
                    executeSpecialAction(crewMember, participants, threat, random, result);
                } else {
                    int totalPower = crewMember.rollActionPower(random, threat);
                    int specializationBonus = crewMember.getThreatBonus(threat);
                    int damage = threat.takeDamage(totalPower);
                    result.addLog(crewMember.getSpecialization().getDisplayName() + "(" + crewMember.getName() + ") attacks "
                            + threat.getName() + " for " + damage + " damage"
                            + " (power " + totalPower
                            + ", specialization bonus " + specializationBonus + ")");
                }

                result.addLog(threat.getName() + " energy: " + threat.getCurrentEnergy() + "/" + threat.getMaxEnergy());
                if (!threat.isAlive()) {
                    break;
                }

                int retaliation = Math.max(1, threat.rollAttack(random) - guardBonus);
                int received = crewMember.takeDamage(retaliation);
                result.addLog(threat.getName() + " retaliates against " + crewMember.getName() + " for " + received + " damage");
                result.addLog(crewMember.getName() + " energy: " + crewMember.getCurrentEnergy() + "/" + crewMember.getMaxEnergy());
                if (!crewMember.isAlive()) {
                    result.addLog(crewMember.getName() + " is down and will be transferred to Medbay after the mission.");
                }
            }
            round += 1;
        }

        boolean success = threat.getCurrentEnergy() <= 0;
        MissionResult finalResult = new MissionResult(threat, success);
        for (String line : result.getLogLines()) {
            finalResult.addLog(line);
        }
        if (success) {
            finalResult.addLog("=== MISSION COMPLETE ===");
            finalResult.addLog("The threat has been neutralized. Surviving crew members gain 1 experience point.");
        } else {
            finalResult.addLog("=== MISSION FAILED ===");
            finalResult.addLog("The colony team was defeated. Downed crew members will recover in Medbay instead of being removed forever.");
        }

        for (CrewMember crewMember : participants) {
            MissionAction action = actionPlan.containsKey(crewMember.getId())
                    ? actionPlan.get(crewMember.getId()) : MissionAction.ATTACK;
            finalResult.addParticipantSnapshot(crewMember, action);
        }
        return finalResult;
    }

    private static void executeSpecialAction(CrewMember actor,
                                             List<CrewMember> participants,
                                             Threat threat,
                                             Random random,
                                             MissionResult result) {
        switch (actor.getSpecialization()) {
            case PILOT: {
                int damage = threat.takeDamage(actor.rollActionPower(random, threat) + 3);
                result.addLog(actor.getName() + " uses Evasive Burst for a precision strike and deals " + damage + " damage.");
                break;
            }
            case ENGINEER: {
                int damage = threat.takeDamage(actor.rollActionPower(random, threat) + 1);
                healWeakestAlly(participants, 2, result, actor.getName() + " uses Repair Pulse and restores 2 energy to the weakest ally.");
                result.addLog(actor.getName() + " also damages the threat for " + damage + " points.");
                break;
            }
            case MEDIC: {
                healWeakestAlly(participants, 4, result, actor.getName() + " uses Emergency Care and restores 4 energy to the weakest ally.");
                int damage = threat.takeDamage(Math.max(1, actor.rollActionPower(random, threat) - 2));
                result.addLog(actor.getName() + " follows up with a support strike for " + damage + " damage.");
                break;
            }
            case SCIENTIST: {
                int damage = threat.takeDamage(actor.rollActionPower(random, threat) + 4);
                result.addLog(actor.getName() + " uses Weakness Scan and deals " + damage + " damage with a boosted analysis strike.");
                break;
            }
            case SOLDIER:
            default: {
                int damage = threat.takeDamage(actor.rollActionPower(random, threat) + 5);
                actor.adjustMorale(2);
                result.addLog(actor.getName() + " uses Heavy Assault and deals " + damage + " damage.");
                break;
            }
        }
    }

    private static void healWeakestAlly(List<CrewMember> participants, int amount, MissionResult result, String actionLog) {
        CrewMember weakest = null;
        for (CrewMember crewMember : participants) {
            if (!crewMember.isAlive()) {
                continue;
            }
            if (weakest == null || crewMember.getCurrentEnergy() < weakest.getCurrentEnergy()) {
                weakest = crewMember;
            }
        }
        if (weakest != null) {
            weakest.recoverEnergy(amount);
            result.addLog(actionLog + " Target: " + weakest.getName() + ".");
        }
    }

    private static boolean hasAnyAliveCrew(List<CrewMember> participants) {
        for (CrewMember crewMember : participants) {
            if (crewMember.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
