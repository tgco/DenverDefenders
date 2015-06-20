#Denver Defenders

*A TGC x Heart and Hand mobile game*

-----------------------------------

##Description

Denver Defenders is a mobile game project for Android and iOS currently in development with the goal to teach
children about hunger issues in Denver, CO.  The project is a collaboration between The Giving Child, a
nonprofit dedicated to educating and empowering children to combat hunger issues, and Heart and Hand, a Denver based
center dedicated to caring for the local children.

In Denver Defenders, the main character navigates a maze to save children.  When one is found, a minigame is played
and if won, the child follows the main character and can be brought back to the headquarters to collect.  The player
wins if all the children are collected.

##Installation and Dependency Instructions

*Denver Defenders is build with Java, Eclipse, and libGDX*

Download and install Java 7 or higher.
Download the Android SDK.
Use the SDK Manager to download the tools and extras, as well as SDK 4.4.w.2 (API 20)
Remember the location you store the SDK, as the path will be needed later.
Download and install Eclipse.
Run eclipse, go to help -> new software. From here install the ADK tools from https://dl-ssl.google.com/android/eclipse/
In Eclipse, go to help -> new software. Install the correct version of Gradle.
For eclispe 4.4 or greater install from: http://dist.springsource.com/snapshot/TOOLS/gradle/nightly
For eclipse < 4.4 install from: http://dist.springsource.com/release/TOOLS/gradle
Download and install Git Bash.
Git clone the branch you want to work on.
Create a file local.properties inside DenverDefenders/
Add this line of text to the file: sdk.dir=/path/to/android/sdk
Use Gradle to import the folder DenverDefenders into your workspace, by following these steps:
Open Eclipse, right click in Package Explorer, left click “Import…”
The import source screen will open. From here select Gradle-> Gradle Project and left click “Next”.
The import Gradle Project screen will open. From here left click “Browse…” and navigate to the DenverDefenders folder within the cloned git repository.
Click “Build Model”, this generate the projects that can be imported.
Select all the generated projects, as well as DenverDefenders and click “Finish”.
Wait for Gradle to import the projects.
You can now begin modifying the core project.
Changes made to the core project are visible to other projects.
If refreshing the workspace does not work, select all project folders in Eclipse -> right click -> Gradle -> Refresh all.
Changes to assets should be done so through the Android project's assets folder, which Gradle will make visible to the other projects.

##Warnings

Do not use indexed .png for assets, they will not load properly on Android, and likely won’t load on iOS. The .png should be packed using the gdx-texture-packer which can be found in the git repository. If you receive errors when packing, enable rotation. If there are still issues, increase the maximum image dimensions.