Introduction
====
For this tutorial you first have to set up and import UltimateCore in your IDE. For a tutorial on how to do that see [here](setting-up.md).<br>
This tutorial should help you understand which package has which purpose in UltimateCore.<br>
You can skip the first 2 chapters if you are already familiar with the gradle package style.

## General structure
The project's source is located in the `src/main` directory.
Anything in this package will go in the jar, anything outside this package will not. Important stuff outside the `src/main` package is:<br>
`docs/` - The docs, which you are looking at right now.<br>
`build.gradle` - The gradle build script, which UltimateCore uses to make the jar.<br>
`build/` - The build folder, anything related to building UltimateCore, expect the build script, is located in here.

## The source package
Inside the source package `src/main` you will find 2 additional folders. <br>
Inside `java` are all the java source files. We will talk about this in the next chapter.<br>
Inside `resources` are all the files which don't have to be compiled. For example, the config files `resources/assets/ultimatecore/config` and language files. `resources/assets/ultimatecore/language`.

## The java structure
All of the project's java files are located in `src/main/java/bammerbom/ultimatecore`. <br>
Here you will find the `Main` class, which is the UltimateCore hub. You can ignore this for now.<br>
More importantly, the `sponge/` package. All sponge classes are located here.

### api
Almost everything not related to a specific module is located in this package. If you search for a non-module located class, it is probably located in here. I will talk about the api package more in depth in later tutorials.

### modules
All the modules are located in this package. I will talk about creating & editing modules in an other tutorial.

### utils
Small utility classes which all modules can use are located here. For example, the `Tuples` class which can hold multiple other objects. If you ever have to make an utility class which can be used by multiple modules, you would put this file here.

### UltimateCore.java
This is the plugin's main class. You can also get all UltimateCore [services](https://docs.spongepowered.org/stable/en/plugin/services.html) via this class.<br>
For example, you can obtain the command service using:<br>
`UltimateCore.get().getCommandService()`