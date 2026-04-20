Updated UML notes

Main classes shown in the UML diagram:
- `CrewMember` as the abstract base class
- Five concrete crew subclasses: `Pilot`, `Engineer`, `Medic`, `Scientist`, `Soldier`
- `Threat` as the generated opponent or incident
- `MissionAction` for tactical choices
- `MissionEngine` for battle logic
- `MissionResult` for mission output and visualization data
- `ColonyStorage` for HashMap-based state management and file persistence

Recent updates reflected in this UML:
- morale field added to `CrewMember`
- `MissionAction` enum added
- `MissionEngine.runMission(...)` now reads an action plan
- Medbay flow is handled through location changes in storage and UI
