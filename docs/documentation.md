Space Colony – Project Documentation

1. General project description
Space Colony is an Android application where the user manages a group of specialized crew members in a space colony. The app supports recruiting crew members, moving them between colony areas, training them to improve experience, monitoring morale, and launching cooperative missions against generated threats.

The application follows the course theme closely while keeping the interface simple and easy to demonstrate. The app is divided into clear screens for home overview, recruitment, quarters management, simulator management, mission control, medbay, and statistics.

2. Team composition
This project was completed individually by:
- Wenrui Xing
- Student number: 002672559

3. Division of work
This was an individual project. All planning, implementation, UI design, debugging, documentation writing, UML preparation, GitHub packaging, and video preparation were completed by the same student.

4. How the project was implemented
The application was implemented in Java using Android Studio. The project uses a single-activity, multi-fragment structure so that navigation is simple and the main logic stays organized.

Main implementation ideas
- `MainActivity` hosts the fragment container and navigation.
- `CrewMember` is the abstract base class.
- `Pilot`, `Engineer`, `Medic`, `Scientist`, and `Soldier` are subclasses.
- `Threat` represents the generated mission enemy or colony problem.
- `ColonyStorage` stores all current crew members in a `HashMap<Integer, CrewMember>` and tracks colony-wide statistics.
- `MissionEngine` performs tactical turn-based mission logic and generates mission logs.
- `MissionAction` stores the user-selected tactical action for each crew member.

The app uses inheritance through the crew subclasses and polymorphism through specialization-specific mission bonuses and special actions. It uses encapsulation by keeping state inside the data model classes and exposing only needed methods.

5. Application use-flow
1. Start on the Home screen.
2. Open Recruit Crew Member and create a new crew member.
3. The new crew member appears in Quarters.
4. Move crew members from Quarters to the Simulator for training or to Mission Control for missions.
5. In the Simulator, train selected crew members. This increases experience, morale, and total skill.
6. In Mission Control, select two or three crew members and choose a tactical action for each one.
7. Read the commander recommendation panel, which suggests a strong leader and current morale status.
8. Launch the tactical mission.
9. A threat is generated automatically based on the number of completed missions.
10. The mission result card shows threat energy, final crew energy bars, and the tactical log.
11. Surviving crew members gain experience. Defeated crew members are moved to Medbay instead of being removed.
12. Open Medbay and return recovered crew members to Quarters.
13. Use Save Data to create `manual_save.json` and Load Data to restore that saved state when needed.
14. Open Statistics to review colony-wide and per-crew performance.

6. Installation instructions
1. Clone or download the repository.
2. Open the project root folder in Android Studio.
3. Let Gradle sync the project.
4. Run the app on an Android device or emulator.
5. Minimum SDK is Android 7.0 (API 24).

7. Data structures used
The project uses the data structures suggested by the assignment.
- `HashMap<Integer, CrewMember>` is used in `ColonyStorage` for storing crew members by ID.
- `ArrayList<CrewMember>` is used to present filtered lists in the RecyclerView adapters.
- `Set<Integer>` is used for selected crew member IDs in the list screens.
- `Map<Integer, MissionAction>` is used to store tactical choices for the current mission.

8. Implemented features
Mandatory features
- OOP-based design
- Java Android app
- Five recruitable crew specializations
- Quarters, Simulator, Mission Control, and Medbay locations
- Training system with experience gain
- Experience affecting skill power
- Generated threats with scaling stats
- Turn-based mission loop
- Crew recovery in Quarters
- HashMap-based data storage and RecyclerView-based UI

Bonus features
- RecyclerView (+1): all crew and statistics lists use RecyclerView.
- Crew Images (+1): every specialization is shown with a unique icon in the crew list.
- Mission Visualization (+2): mission results use a visual card with a threat energy bar, crew energy bars, outcome summary, and tactical mission log.
- Tactical Combat (+2): the player chooses Attack, Defend, or Special for each selected crew member before launching the mission.
- Statistics (+1): the app tracks missions, wins, defeats, and training counts.
- No Death (+1): defeated crew members are sent to Medbay and can later return to Quarters.
- Randomness in Missions (+1): both crew actions and threat attacks include random variation.
- Specialization Bonuses (+2): each specialization gains a bonus on a preferred threat type, and each role also has a custom special action.
- Larger Squads (+2): missions support two or three crew members.
- Fragments (+2): the app uses fragments meaningfully for all screens.
- Data Storage & Loading (+2): the app supports file-based save and load. Crew members and colony statistics can be manually saved into `manual_save.json` and loaded later from the Home screen. The app also keeps an automatic backup in `autosave.json`.
- Statistics Visualization (+2): progress bars are used to visualize win rate, mission readiness, and average morale.
- Custom Feature X (+2): a morale system affects crew skill through morale bonuses, and the commander recommendation panel in Mission Control highlights the best current leader.

9. Tools used
- Android Studio
- Java
- Gradle
- RecyclerView
- Internal JSON file storage (`manual_save.json` and `autosave.json`) + Gson for serialization
- Markdown for documentation
- Graphviz for UML diagram export

10. GitHub and video links
- GitHub repository: https://github.com/wenrui28/project.git
- Explanation video: [PASTE_VIDEO_URL_HERE]

11. AI usage disclaimer
AI was used only as a support tool to check missing submission items and improve wording in the documentation. The implementation choices, code integration, project packaging, and final submission remain the student’s responsibility.
