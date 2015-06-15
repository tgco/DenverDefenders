TheGivingChild git repository

Installation Instructions:

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
Create a file local.properties inside the TheGivingChildEngine/LibGDX_Generate/
Add this line of text to the file: sdk.dir=F:/android-sdk_r24.2-windows/android-sdk-windows where sdk.dir= points to the path on the right of your Android SDK
Use Gradle to import the folder LibGDX_Generate into your workspace, by following these steps:
Open Eclipse, right click in Package Explorer, left click “Import…”
The import source screen will open. From here select Gradle-> Gradle Project and left click “Next”.
The import Gradle Project screen will open. From here left click “Browse…” and navigate to the LibGDX_Generate folder within the cloned git repository.
Click “Build Model”, this generate the projects that can be imported.
Select all the generated projects under the LibGDX_Generate path, as well as LibGDX_Generate and lift click “Finish”.
Wait for Gradle to import the projects.
You can now begin working on The Giving Child Engine through the core project.
Changes made to the core project will be made to other projects as needed by Gradle.
If refreshing the workspace does not work, select all project folders in Eclipse -> right click -> Gradles -> Refresh all.
Changes to Assets should be done so through the Android project's Assets folder, which Gradle will push to the other projects.
Do not push to the Master Branch if your code does not work. If you want to push code that is not ready to be merged, do so in a custom branch.
If you are working on a separate branch and want to merge with Master, pull from Master, fix any conflicts, commit, then push to Master.

Warnings:

Do not use indexed .png for assets, they will not load properly on Android, and likely won’t load on iOS. The .png should be packed using the gdx-texture-packer which can be found in the git repository. If you receive errors when packing, enable rotation. If there are still issues, increase the maximum image dimensions.

