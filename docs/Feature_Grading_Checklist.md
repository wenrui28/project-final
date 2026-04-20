Space Colony – Bonus Feature Grading Checklist

Mandatory requirements
- OOP structure with abstract base class and subclasses
- Java Android app
- Recruit / move / train / mission / recovery flow
- HashMap-based storage
- Documentation, UML, team composition, AI disclaimer

Bonus features submitted for grading
- RecyclerView (+1)
- Crew Images (+1)
- Mission Visualization (+2)
- Tactical Combat (+2)
- Statistics (+1)
- No Death (+1)
- Randomness in Missions (+1)
- Specialization Bonuses (+2)
- Larger Squads (+2)
- Fragments (+2)
- Data Storage & Loading (+2)
- Statistics Visualization (+2)
- Custom Feature X: morale system + commander recommendation (+2)

Quick places to verify in code
- Mission actions and tactical mission logic:
  - `app/src/main/java/com/wenruixing/spacecolony/model/MissionAction.java`
  - `app/src/main/java/com/wenruixing/spacecolony/data/MissionEngine.java`
- Medbay / no-death handling:
  - `app/src/main/java/com/wenruixing/spacecolony/model/CrewLocation.java`
  - `app/src/main/java/com/wenruixing/spacecolony/data/ColonyStorage.java`
  - `app/src/main/java/com/wenruixing/spacecolony/ui/CrewListFragment.java`
- Morale system and custom feature:
  - `app/src/main/java/com/wenruixing/spacecolony/model/CrewMember.java`
  - Mission Control recommendation panel in `CrewListFragment`
- Statistics visualization:
  - `app/src/main/java/com/wenruixing/spacecolony/ui/StatisticsFragment.java`
  - `app/src/main/res/layout/fragment_statistics.xml`
