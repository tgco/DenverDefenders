TheGivingChild git repository

Download and install Java 7 or higher.
Download the Android SDK.
Use the SDK Manager to download the tools and extras, as well as SDK 4.4.w.2 (API 20)
Remember the location you store the SDK, as the path will be needed later.

Download and install Eclipse (I'm using Luna).
With eclipse, go to help -> new software. From here install the ADK tools from https://dl-ssl.google.com/android/eclipse/
Also install the correct version of Gradle through the new software GUI.
For eclispe 4.4 or greater install from: http://dist.springsource.com/snapshot/TOOLS/gradle/nightly
For eclipse < 4.4 install from: http://dist.springsource.com/release/TOOLS/gradle

Download and install Git Bash.

Git clone the branch you want to work on.
Create a file local.properties inside the TheGivingChildEngine/LibGDX_Generate/
Add this line of text to the file: sdk.dir=F:/android-sdk_r24.2-windows/android-sdk-windows
where sdk.dir= points to the path on the right of your Android SDK

Use Gradle to import the folder LibGDX_Generate into your workspace.

Changes made to the core project will be made to other projects as needed by Gradle.
If refreshing the workspace does not work, select all project folders in Eclipse -> right click -> Gradles -> Refresh all.

Changes to Assets should be done so through the Android projects Assets folder, which Gradle will push to the other projects.

Do not push to the Master Branch if your code does not work. If you want to push code that you are working on, do so in a custom branch.
If you are working on a seperate branch and want to merge with Master, pull from Master, fix any conflicts, then push to Master.



Relevant links for project:
https://github.com/libgdx/libgdx/wiki/Managing-your-assets