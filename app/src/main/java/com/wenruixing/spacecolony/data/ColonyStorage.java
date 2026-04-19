package com.wenruixing.spacecolony.data;

import android.content.Context;

import com.google.gson.Gson;
import com.wenruixing.spacecolony.model.CrewFactory;
import com.wenruixing.spacecolony.model.CrewLocation;
import com.wenruixing.spacecolony.model.CrewMember;
import com.wenruixing.spacecolony.model.CrewMemberRecord;
import com.wenruixing.spacecolony.model.CrewSpecialization;
import com.wenruixing.spacecolony.model.MissionResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ColonyStorage {

    private static final String AUTO_SAVE_FILE = "autosave.json";
    private static final String MANUAL_SAVE_FILE = "manual_save.json";
    private static final ColonyStorage INSTANCE = new ColonyStorage();

    private final HashMap<Integer, CrewMember> crewMap = new HashMap<>();
    private final Gson gson = new Gson();

    private Context appContext;
    private int nextId = 1;
    private int totalRecruited = 0;
    private int totalMissions = 0;
    private int successfulMissions = 0;
    private int failedMissions = 0;

    private ColonyStorage() {
    }

    public static ColonyStorage getInstance() {
        return INSTANCE;
    }

    public void initialize(Context context) {
        appContext = context.getApplicationContext();
        boolean loaded = loadAutoState();
        boolean emptyColony = crewMap.isEmpty()
                && totalRecruited == 0
                && totalMissions == 0
                && successfulMissions == 0
                && failedMissions == 0;
        if (!loaded || emptyColony) {
            seedStarterCrew();
            saveAutoState();
        }
    }

    public int getNextIdAndAdvance() {
        return nextId++;
    }

    private void seedStarterCrew() {
        crewMap.clear();
        nextId = 1;
        totalRecruited = 0;
        totalMissions = 0;
        successfulMissions = 0;
        failedMissions = 0;

        addStarterCrew("Nova", CrewSpecialization.PILOT);
        addStarterCrew("Rex", CrewSpecialization.SOLDIER);
        addStarterCrew("Mira", CrewSpecialization.MEDIC);
    }

    private void addStarterCrew(String name, CrewSpecialization specialization) {
        CrewMember crewMember = CrewFactory.create(getNextIdAndAdvance(), name, specialization);
        crewMap.put(crewMember.getId(), crewMember);
        totalRecruited += 1;
    }

    public void addCrewMember(CrewMember crewMember) {
        crewMap.put(crewMember.getId(), crewMember);
        totalRecruited += 1;
        saveAutoState();
    }

    public List<CrewMember> getCrewByLocation(CrewLocation location) {
        ArrayList<CrewMember> result = new ArrayList<>();
        for (CrewMember crewMember : crewMap.values()) {
            if (crewMember.getLocation() == location) {
                result.add(crewMember);
            }
        }
        result.sort(Comparator.comparingInt(CrewMember::getId));
        return result;
    }

    public List<CrewMember> getAllCrew() {
        ArrayList<CrewMember> result = new ArrayList<>(crewMap.values());
        result.sort(Comparator.comparingInt(CrewMember::getId));
        return result;
    }

    public int countByLocation(CrewLocation location) {
        int count = 0;
        for (CrewMember crewMember : crewMap.values()) {
            if (crewMember.getLocation() == location) {
                count += 1;
            }
        }
        return count;
    }

    public void moveCrew(Set<Integer> selectedIds, CrewLocation destination) {
        for (Integer id : selectedIds) {
            CrewMember crewMember = crewMap.get(id);
            if (crewMember != null) {
                crewMember.setLocation(destination);
                if (destination == CrewLocation.QUARTERS) {
                    crewMember.healToFull();
                }
            }
        }
        saveAutoState();
    }

    public void trainCrew(Set<Integer> selectedIds) {
        for (Integer id : selectedIds) {
            CrewMember crewMember = crewMap.get(id);
            if (crewMember != null && crewMember.getLocation() == CrewLocation.SIMULATOR) {
                crewMember.train();
            }
        }
        saveAutoState();
    }

    public CrewMember getCrew(int id) {
        return crewMap.get(id);
    }

    public void applyMissionResult(List<CrewMember> participants, MissionResult missionResult) {
        totalMissions += 1;
        if (missionResult.isSuccess()) {
            successfulMissions += 1;
        } else {
            failedMissions += 1;
        }

        for (CrewMember crewMember : participants) {
            if (!crewMember.isAlive()) {
                crewMember.markMissionLoss();
                crewMember.resetAfterDefeat();
                crewMember.setLocation(CrewLocation.MEDBAY);
            } else if (missionResult.isSuccess()) {
                crewMember.markMissionWin();
                crewMember.gainMissionExperience();
                crewMember.setLocation(CrewLocation.MISSION_CONTROL);
            } else {
                crewMember.setLocation(CrewLocation.MISSION_CONTROL);
            }
        }
        saveAutoState();
    }

    public int getTotalRecruited() {
        return totalRecruited;
    }

    public int getTotalMissions() {
        return totalMissions;
    }

    public int getSuccessfulMissions() {
        return successfulMissions;
    }

    public int getFailedMissions() {
        return failedMissions;
    }

    public int getAverageMorale() {
        if (crewMap.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (CrewMember crewMember : crewMap.values()) {
            total += crewMember.getMorale();
        }
        return total / crewMap.size();
    }

    public int getWinRatePercent() {
        if (totalMissions == 0) {
            return 0;
        }
        return (successfulMissions * 100) / totalMissions;
    }

    public int getMissionReadinessPercent() {
        int active = countByLocation(CrewLocation.MISSION_CONTROL) + countByLocation(CrewLocation.QUARTERS);
        int medbay = countByLocation(CrewLocation.MEDBAY);
        int total = active + medbay;
        if (total == 0) {
            return 0;
        }
        return (active * 100) / total;
    }

    public void saveToDisk() {
        saveManualFile();
    }

    public boolean loadFromDisk() {
        return loadManualFile();
    }

    public boolean saveAutoState() {
        return writeStateToFile(AUTO_SAVE_FILE);
    }

    public boolean loadAutoState() {
        return readStateFromFile(AUTO_SAVE_FILE);
    }

    public boolean saveManualFile() {
        return writeStateToFile(MANUAL_SAVE_FILE);
    }

    public boolean loadManualFile() {
        return readStateFromFile(MANUAL_SAVE_FILE);
    }

    private boolean writeStateToFile(String fileName) {
        if (appContext == null) {
            return false;
        }

        StorageState state = new StorageState();
        state.nextId = nextId;
        state.totalRecruited = totalRecruited;
        state.totalMissions = totalMissions;
        state.successfulMissions = successfulMissions;
        state.failedMissions = failedMissions;

        List<CrewMember> allCrew = getAllCrew();
        for (CrewMember crewMember : allCrew) {
            state.crewMembers.add(crewMember.toRecord());
        }

        try (FileOutputStream fileOutputStream = appContext.openFileOutput(fileName, Context.MODE_PRIVATE);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(outputStreamWriter)) {

            writer.write(gson.toJson(state));
            writer.flush();
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private boolean readStateFromFile(String fileName) {
        if (appContext == null) {
            return false;
        }

        try (FileInputStream fileInputStream = appContext.openFileInput(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String json = builder.toString().trim();
            if (json.isEmpty()) {
                return false;
            }

            StorageState state = gson.fromJson(json, StorageState.class);
            if (state == null) {
                return false;
            }

            crewMap.clear();
            nextId = Math.max(1, state.nextId);
            totalRecruited = Math.max(0, state.totalRecruited);
            totalMissions = Math.max(0, state.totalMissions);
            successfulMissions = Math.max(0, state.successfulMissions);
            failedMissions = Math.max(0, state.failedMissions);

            for (CrewMemberRecord record : state.crewMembers) {
                CrewSpecialization specialization = CrewSpecialization.valueOf(record.specialization);
                CrewMember crewMember = CrewFactory.create(record.id, record.name, specialization);
                crewMember.restoreFromRecord(record);
                crewMap.put(crewMember.getId(), crewMember);
            }
            return true;
        } catch (FileNotFoundException exception) {
            return false;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public void resetAllData() {
        crewMap.clear();
        nextId = 1;
        totalRecruited = 0;
        totalMissions = 0;
        successfulMissions = 0;
        failedMissions = 0;
        saveAutoState();
    }
}
