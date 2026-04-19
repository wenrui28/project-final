package com.wenruixing.spacecolony.data;

import com.wenruixing.spacecolony.model.CrewMemberRecord;

import java.util.ArrayList;
import java.util.List;

public class StorageState {
    public int nextId = 1;
    public int totalRecruited = 0;
    public int totalMissions = 0;
    public int successfulMissions = 0;
    public int failedMissions = 0;
    public List<CrewMemberRecord> crewMembers = new ArrayList<>();
}
