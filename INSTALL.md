# Table of Contents

- [Getting started](#getting-started)
- [Importing the project](#importing-the-project)
- [Installing Lombok](#installing-lombok)
- [Building the project](#building-the-project)
- [Running the project](#running-the-project)
- [Conclusion](#conclusion)
- [Advanced](#advanced)
    - [RS Injection Assist](#rs-injection-assist)
- [Troubleshooting](#troubleshooting)
    - [Missing git](#missing-git)
    - [JDK Issues](#jdk-issues)
    - [Client failing to start](#client-failing-to-start)
    - [Enable annotation processing](#enable-annotation-processing)
    - [Update your fork](#update-your-fork)
    - [Skip gradle tests](#skip-gradle-tests)

# Getting started

For working with this project, [IntelliJ IDEA](https://www.jetbrains.com/idea/download) is our recommended IDE and the one used by most collaborators. The free community edition has everything you'll need to start testing and contributing real improvements to the project.

You can build OpenOSRS locally using JDK 11 as the codebase supports language features up to Java 11. [RuneLite mixins](https://github.com/runelite/runelite/wiki/Using-RuneLite's-mixins) are the exception - these are are limited to Java 7.

Direct JDK download links:  
[Linux (x64)](https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.8%2B10/OpenJDK11U-jdk_x64_linux_hotspot_11.0.8_10.tar.gz)  
[MacOS (x64)](https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.8%2B10/OpenJDK11U-jdk_x64_mac_hotspot_11.0.8_10.pkg)  
[Windows (x64)](https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.8%2B10/OpenJDK11U-jdk_x64_windows_hotspot_11.0.8_10.msi)  
[Windows (x86)](https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.8%2B10/OpenJDK11U-jdk_x86-32_windows_hotspot_11.0.8_10.msi)

## Importing the project

After launching IntelliJ IDEA for first time, you will see IntelliJ welcome window. You will now need to clone OpenOSRS repository from git:

![welcome-import-git](https://i.imgur.com/NdmFPtO.png)

After clicking on that you will be greeted with prompt. You can either enter the OpenOSRS repository `https://github.com/open-osrs/runelite` or your own GitHub fork, if you've created one.

**NOTE**: If you plan to make a Pull Request, you must fork the OpenOSRS repository, and clone from your fork. You can do this by navigating to https://github.com/open-osrs/runelite and clicking the fork button.

The prompt should look something like this (if you have issues with finding git.exe see [Troubleshooting](#troubleshooting) section):

![welcome-clone-project](https://i.imgur.com/xDVy8v8.png)

Click _Clone_. Now IntelliJ should start cloning the repository:

![welcome-cloning-project](https://i.imgur.com/BZfV7wK.png)

If everything went well, you should be able to see something like this:

![after-clone-fresh-project](https://i.imgur.com/qbbDsxt.png)

The current status is shown at **(1)**. Wait until this finishes and no longer shows any text like "Indexing" or "Importing dependencies".

The gradle sync status is shown at **(2)**. This should show success. If not check the [JDK Issues](#jdk-issues) section under Troubleshooting.

The project view is shown at **(3)**. If this is not visible you can click "Project" in the sidebar to reveal it.

The gradle sidebar is shown at **(4)**. This will show all Gradle tasks, which we'll get back to later.

Press the Build button **(5)** to test if the import succeeded.

## Installing Lombok

When first viewing the project in IntelliJ IDEA you may come across this error:

![missing-lombok-errors](https://i.imgur.com/a1YDonV.png)

This is because you do not have the [Lombok Plugin](https://plugins.jetbrains.com/plugin/6317-lombok-plugin) installed.

Navigate to the **Plugins** tab under the **File > Settings** menu (**IntelliJ IDEA > Preferences** for Mac). Click the **Marketplace** button and search for **Lombok** to find it. Install the plugin and restart IntelliJ IDEA.

![installing-lombok](https://i.imgur.com/PxzpCcO.png)

If you then get the following dialog, click Enable:

![enable-annotation-processing-dialog](https://i.imgur.com/K53iE0M.png)

If you did not see this dialog but still have issues, see [Enable annotation processing](#enable-annotation-processing).

Success! You should no longer be getting `Cannot resolve symbol` or `Cannot resolve method` errors.

## Building the project [Updated Section] by Joseph for IntelliJ IDEA CE 2012.2

OpenOSRS is using [Gradle](https://gradle.org/) as build tool. It is used for dependency management, resource generation, running tests and any other tooling needed to properly build, run and deploy OpenOSRS.

So, to actually run OpenOSRS, we first need to invoke Gradle.

Locate _Gradle_ on right-side of the screen until you open something like this:

![project-gradle-view](https://i.imgur.com/peE1DTV.png)

Navigate to **Tasks > build > build**, right-click and select `Modify runelite [build]`:

![modify-gradle-build](https://i.imgur.com/sOI0NQ7.png)

A window like this should pop-up.


![project-gradle-build-save](https://i.imgur.com/TW4VwdJ.png)

Modify the **Run** and change it to:

```
build publishToMavenLocal :runelite-client:publishToMavenLocal :runelite-api:publishToMavenLocal :http-api:publishToMavenLocal
```

![project-gradle-build-modify](https://i.imgur.com/I3pEHp7.png)

You can choose to skip running the unit tests for each build, which will speed them up greatly. However, if you intend to submit a pull request you must ensure the tests pass! If you do choose to skip the tests you can append `-x test` to the **Run** textbox.

If you do not intend to to submit a pull request, you may also choose to append `-x checkStyleMain -x checkStyleTest -x test` to the **Run** to not be held to our coding standards.

![project-gradle-add-arguments](https://i.imgur.com/Z67Lb1P.png)

Finally press the icon that looks like a play button and wait until you have a build success

![project-gradle-build-run](https://i.imgur.com/0Z6FXFr.png)

If you are having issues see the [Troubleshooting](#troubleshooting) section.

Now, we need to tell IntelliJ to pick up changes based on Gradle build (it should do that automatically, but sometimes it doesnt). So, still in same Gradle window, just click the Refresh icon:

![project-gradle-refresh](https://i.imgur.com/qUDDMRg.png)

## Running the project

Running the project is very simple, simply copy / duplicate the _build_ task, but rename the **build to **run inside **Run.

Click the dropdown menu which allows you to select a task (open-osrs [build]), but select the **Edit Configurations... item.

![project-gradle-run-task](https://i.imgur.com/XoKt6fV.png)

The Run Configurations window should now be open. You should already know what this is.

Click on the `open-osrs [build]` item underneath the `Gradle` dropdown (or whatever you named it before).

Click the Copy Configuration icon to make a copy of it.

![project-gradle-copy-build-task-hover](https://i.imgur.com/Kp3zufW.png)

![project-gradle-copy-build-task](https://i.imgur.com/kdmMvto.png)

Change the name from whatever it is (e.g. open-osrs **[build] ) to open-osrs **[run]

Inside the configuration, in the **Run textbox, change **build to **run.

![project-gradle-make-runnable](https://i.imgur.com/GfspANj.png)

7. Click **Apply and then **OK.

Then you can run it, and OpenOSRS will open!

If you are having any issues with this step see the [Troubleshooting](#troubleshooting) section.

## Conclusion

Success! You can switch between running and building the client by selecting the build configuration:

![select-run-config](https://i.imgur.com/FDnip9G.png)

Finally if you intend to contribute to the project, check out the [Code Conventions](https://github.com/open-osrs/runelite/wiki/Code-Conventions).

# Advanced

## RS Injection Assist

There is a custom IntelliJ plugin that aids navigating the `runescape-client` and `runelite-mixins` repository their imports & exports: https://plugins.jetbrains.com/plugin/14808-rs-injection-assist

[Demo video](https://i.imgur.com/cfO6CVR.gifv)

# Troubleshooting

## Missing git

If you are getting error about `git.exe` (or `git` on linux and mac) missing, you will need to first download and install Git for your OS. Git is version control software and implementation that OpenOSRS uses to store and track history of it's source code. To download git, just [go here and select your OS version](https://git-scm.com/downloads).

## JDK Issues

If you are getting errors about missing or incorrect JDK versions, make sure to install JDK 11 (see the start of this page).

Then navigate to **File > Settings > Build, Execution, Deployment > Build Tools > Gradle** and ensure the Gradle JVM is set to Java 11 (or later):

![gradle-sdk](https://i.imgur.com/jhJV84v.png)

Also navigate to **File > Project Structure** and ensure the Project SDK is set to Java 11 (or later):

![project-sdk](https://i.imgur.com/cBNrdc5.png)

## Client failing to start

If the client fails to boot or if the applet does not appear, try to rebuild the project by running the Gradle builder again. If that doesn't work, try closing IntelliJ and re-opening it again and running `RuneLite` class again. If that also doesn't work, feel free to ask for helping the discord server. Here are few helpful tips:

### Enable annotation processing

Open up settings and go to `Build, Execution, Deployment` -> `Compiler` -> `Annotation Processors` and then enable `annotation processing`

![enable-annotations](https://media.discordapp.net/attachments/575130572955975691/603811969858011138/unknown.png)

### Update your fork

To sync your fork simply run

    git fetch upstream
    git merge upstream/master

or update the project from IntelliJ:

![update-project](https://i.imgur.com/69R580v.png)

### Skip gradle tests

This will skip the tests, which may

Typically your build will look like `tasks: build` `Arguments: -x test`

Happy development!


### Check jre/lib/security/cacerts file

If you are getting otherwise unexplained errors with Gradle, especially after updating/replacing openJDK installation. I was getting obscure errors about being unable to read file stream of "checkstyle.xml". Ended being because the xml doctype pointed to "https://checkstyle.org/dtds/configuration_1_3.dtd" which was unreachable.

It was only after nuking my .gradle and .m2 folders and trying to build from scratch that I received a useful error message.

```
Exception in thread "main" javax.net.ssl.SSLException: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameter
Exception: the trustAnchors parameter must be non-empty
```

I managed to fix the problem by copying the cacerts file from IntelliJ's default jre to openjdk-9/lib/security and overwriting the existing file. 
