#Bot Development
## Menu & Right Click (October 28, 2021)
1. Working on getting the menu row where the mouse is located.
   - net/runelite/api/widgets/menu/ContextMenu.java:28
   - net.runelite.api.widgets.menu.ContextMenu.getRowAt
2. Modifying Plugin to display canvas location, and easier debugging access to information (mouseover) on 
   - net.runelite.client.plugins.mousehighlight.MouseHighlightOverlay.tooltipManager
   - net/runelite/client/plugins/mousehighlight/MouseHighlightOverlay.java:180 (works)
   - ```
     // todo: copy this entire plugin and add it as a configuration for the Adonai Plugin (for context and menu help)
			// just add the row here, accessed by ContextMenu.getRowAt
			// net.runelite.api.widgets.menu.ContextMenu.getRowAt
			// or add the canvas information here
//			tooltipManager.addFront(new Tooltip("Menu Options" + (Strings.isNullOrEmpty(target) ? "" : " " + target + " [" + client.getMouseCanvasPosition().getX() + ", " +client.getMouseCanvasPosition().getY() + " ]")));```
3. Inside plugin itself: gotta fix.. Give me the OBJECT that the mouse is over...
   1. net.runelite.client.plugins.adonaicore.MenuSession.getAllTargetTileObjects

# The plugins hot loading...
1. Clone the plugins repository
2. Add the plugins configuration
3. Update the `external.system.substitute.library.dependencies` to checked
   1. `plugins/gradle.build.kts`
   2. Modify the `build` configuration:
      1. `build copyDeps --stacktrace -x test`
   3. Modify the `client` build configuration:
      1. `build publishToMavenLocal :runelite-client:publishToMavenLocal :runelite-api:publishToMavenLocal :http-api:publishToMavenLocal -x test -x checkStyleMain -x checkStyleTest`
   4. Build the client
   5. Build the plugins
4. Paste `PLUGIN_DEVELOPMENT_PATH=../OpenOSRSPlugins` into `run` config ENVIRONMENTAL variables

# If the plugins are not compiling, not finding the built client...
1. We need to change the version of the built files
2. Change the `buildSrc/src/main/kotlin/Dependencies.kt` version...
   1. to the current version, listed at `/Users/josephalai/.m2/repository/com/openosrs/runelite-api`

![](https://i.imgur.com/0D5106S.png)


# OpenOSRS injected RuneLite 

[![Build Status](https://github.com/open-osrs/runelite/workflows/OpenOSRS%20-%20CI%20(push)/badge.svg)](https://github.com/open-osrs/runelite/actions?query=workflow%3A%22OpenOSRS+-+CI+%28push%29%22)
[![HitCount](http://hits.dwyl.com/open-osrs/runelite.svg?style=flat)](http://hits.dwyl.com/open-osrs/runelite)

[OpenOSRS](https://openosrs.com) is a fully open-source client with no restrictions. We are not affiliated with Jagex or RuneLite.  
  
This is a special branch that uses the upstream client (RuneLite) with the OpenOSRS injector. (bundled)  

From the root module, run the following gradle tasks:  
```clean build run```
  
This branch is still in bringup but most functionality works as intended.  
This branch uses upstreams PlayerManager/OverlayManager etc so keep that in mind if porting a plugin.  
  
Contributions are welcome, but there should be no changes made to runelite-client unless necessary/minor. Mould the api around the client.
## Discord  

[![Discord](https://img.shields.io/discord/373382904769675265.svg)](https://discord.gg/openosrs)

## Project Layout  

- [cache](cache/src/main/java/net/runelite/cache) - Libraries used for reading/writing cache files, as well as the data in it
- [deobfuscator](deobfuscator/src/main/java/net/runelite/deob) - Can decompile and cleanup gamepacks as well as map updates to newer revs
- [http-api](http-api/src/main/java/net/runelite/http/api) - API for runelite and OpenOSRS
- [injector](injector/src/main/java/com/openosrs/injector) - Bytecode weaver that allows us to add code to the obfuscated gamepack
- [runelite-api](runelite-api/src/main/java/net/runelite/api) - RuneLite API, interfaces for accessing the client
- [runelite-mixins](runelite-mixins/src/main/java/net/runelite) - Classes containing the Objects to be injected using the injector-plugin
- [runescape-api](runescape-api/src/main/java/net/runelite) - Mappings correspond to these interfaces, runelite-api is a subset of this
- [runelite-client](runelite-client/src/main/java/net/runelite/client) - Game client with plugins
- [wiki-scraper](wiki-scraper/src/main/java/net/runelite/data) - Scrapes the runescape wiki https://oldschool.runescape.wiki for the latest npc data

## Modified Mixins (Menu)
These are used to make a modified version of the OSRS Right-Click Context Menu. Including the ALai
`git status`
```
modified:   ../../../../../../../../runelite-api/src/main/java/net/runelite/api/Client.java
modified:   ../../../../../../../../runelite-api/src/main/java/net/runelite/api/widgets/Menu/RightClickMenuHelper.java
modified:   ../../../../../../../../runelite-mixins/src/main/java/net/runelite/mixins/MenuMixin.java
```

## Building  

# Gradle Run Installation

## Project Structure
- File -> Project Structure -> Project Tab -> Project Settings
  - SDK: Select `11 corretto-11 (java version "11.0.12")`   
  - Language Level: Select `8 - Lambdas, type annotations etc.`
  - 
## More Compiler / Language Preferences
  - Preferences  `CMD+,`
    - `Build, Execution, Deployment` Section
    - -> Compiler -> Java Compiler`
        - Project bytecode version: Select `1.8`
    - -> Build Tools -> Gradle`
        - Gradle JVM: Select `corretto-11`
## Gradle build Tasks
  - Gradle Tab -> OpenOSRS -> Tasks
    - build -> build (Right Click: `Modify Configuration`)
    - Build Task: 
      1. Modify contents inside Run from `build` to `build publishToMavenLocal -x test -x checkStyleMain -x checkStyleTest`
      2. Checkbox the box that says `Store as project file`
    - Run Task:
      1. Duplicate `openosrs [build]` to `openosrs [run]`
      2. Modify contents inside Run textbox
         1. `build publishToMavenLocal -x test -x checkStyleMain -x checkStyleTest`
         2. to `run publishToMavenLocal -x test -x checkStyleMain -x checkStyleTest`
         3. Checkbox the box that says `Store as project file`



We have migrated the project to Gradle. Information on how to setup and build the project can be found at https://github.com/open-osrs/runelite/wiki/Building-with-IntelliJ-IDEA
## Install from Pre-built Binaries 

Installers for Windows, Mac and Linux can be found at https://github.com/open-osrs/launcher/releases

## License  

OpenOSRS is licensed under the BSD 2-clause license. See the license header in the respective file to be sure.

## Contribute and Develop  

We've set up a separate document for our [contribution guidelines](https://github.com/open-osrs/runelite/blob/master/.github/CONTRIBUTING.md).

## Supported By  

OpenOSRS uses profiling tools provided by [YourKit](https://www.yourkit.com/)

Thanks [JetBrains](https://www.jetbrains.com/idea/download/) for providing our developers with IntelliJ IDEA Ultimate Edition.

## Wiki

Check out the [OpenOSRS wiki to learn more](https://github.com/open-osrs/runelite/wiki)
