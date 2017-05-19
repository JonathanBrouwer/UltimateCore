Setting up & Building
====

## Building against UltimateCore
UltimateCore currently does not have a repo, but I will make sure to make one soon. 
You can use JitPack to build against UltimateCore if you are using maven or gradle. 
Take a look at https://jitpack.io/#Bammerbom/UltimateCore

## Setting up & Building UltimateCore
Execute these commands to set up UltimateCore.<br>
Note: Use 'gradlew' instead of 'gradle' if you don't have gradle installed and want to use the wrapper instead.
1. Navigate to the directory you want to clone UltimateCore in, and open a command prompt.
2. `git clone https://github.com/Bammerbom/UltimateCore.git`
3. `cd UltimateCore`
3. `gradle build`
5. Open the project in Intellij (or any other IDE, I personally use Intellij)
6. Make your changes
7. Build UltimateCore using `gradle build`, this will create a jar file in `UltimateCore/build/libs/UltimateCore.jar`