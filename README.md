# Space Colony

Space Colony is an Android application built in Java in Android Studio. The player manages a crew of colony specialists, trains them, tracks morale, and sends two- or three-person squads on tactical turn-based missions against generated threats.

This repository is structured to match the final submission requirements of the course project. The project includes a public Android Studio project, documentation, UML diagram, GitHub-ready README, and submission text.

## Quick links
- Documentation: [`docs/documentation.md`](docs/documentation.md)
- UML diagram: [`docs/UML-class-diagram.png`](docs/UML-class-diagram.png)
- UML source: [`docs/UML-class-diagram.md`](docs/UML-class-diagram.md)
- Moodle submission text: [`docs/Moodle_Submission_Text.txt`](docs/Moodle_Submission_Text.txt)

## Team composition
- Wenrui Xing, student number 002672559

## Implemented mandatory features
- Android application written in Java
- Object-oriented design with encapsulation, inheritance, and polymorphism
- Five crew specializations: Pilot, Engineer, Medic, Scientist, Soldier
- Recruit crew members to Quarters
- Move crew members between Quarters, Simulator, Mission Control, and Medbay
- Training system that increases experience and morale
- Cooperative turn-based mission system with generated threats and scaling difficulty
- Recovery in Quarters restores full energy while preserving progress
- HashMap storage for crew and RecyclerView-based lists

## Implemented bonus features
- RecyclerView (+1)
- Crew Images using unique specialization icons (+1)
- Mission Visualization with threat and crew energy bars, mission summary card, and tactical log (+2)
- Tactical Combat with Attack / Defend / Special action choices (+2)
- Statistics (+1)
- No Death / Medbay recovery flow (+1)
- Randomness in missions (+1)
- Specialization bonuses (+2)
- Larger Squads: support for 2 or 3 crew members (+2)
- Fragments (+2)
- File-based data storage and loading using `manual_save.json` and `autosave.json` (+2)
- Statistics Visualization with progress bars for win rate, readiness, and morale (+2)
- Custom Feature X: morale system and commander recommendation panel (+2)

## Build instructions
1. Open the project root in Android Studio.
2. Allow Gradle to sync.
3. Run the app on an Android emulator or device with Android 7.0 or newer.
4. The launcher activity is `MainActivity`.

## AI usage disclaimer
AI assistance was used for project packaging review, documentation polishing, and checking missing submission items. The student remained responsible for the project structure, final decisions, and final submission.
