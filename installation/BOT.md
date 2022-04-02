# Installing Adonai Bot on Fresh OpenOSRS Install

The following directories must be moved to the proper location in the OpenOSRS directory for this to work.

```
runelite-api/src/main/java/net/runelite/api/widgets/menu/
runelite-client/src/main/java/net/runelite/client/external/
runelite-client/src/main/java/net/runelite/client/plugins/adonaibanker/
runelite-client/src/main/java/net/runelite/client/plugins/adonaicore/
runelite-client/src/main/java/net/runelite/client/plugins/adonaicoremenu/
runelite-client/src/main/java/net/runelite/client/plugins/adonaifarmer/
runelite-client/src/main/java/net/runelite/client/plugins/adonaifisher/
runelite-client/src/main/java/net/runelite/client/plugins/adonaimenuchanger/
runelite-client/src/main/java/net/runelite/client/plugins/adonairandoms/
runelite-client/src/main/java/net/runelite/client/plugins/adonaitreecutter/
runelite-client/src/main/java/net/runelite/client/plugins/autopath/
```


###Basic Instruction Layout (Core for Everything)
- Copy and Paste Bot Files to `client/external` -- This is the bot.
- Copy and Paste Plugins to `/plugins` -- This is the implementation of the bot (scripts).
- Edit `runelite-api/src/main/java/net/runelite/api/Client.java` file to layout right-click menu functionality.
- Edit `runelite-mixins/src/main/java/net/runelite/mixins/MenuMixin.java` to implement this functionality.
- Build & Run

###Supplementary Menu Items (Instead of just adding the entire folder)
```
new file:   runelite-api/src/main/java/net/runelite/api/widgets/menu/ContextMenu.java
new file:   runelite-api/src/main/java/net/runelite/api/widgets/menu/MenuRow.java
new file:   runelite-api/src/main/java/net/runelite/api/widgets/menu/MenuTargetIdentifier.java
```

### All External Bot Files
```
new file:   runelite-client/src/main/java/net/runelite/client/external/ExtUtils.java
new file:   runelite-client/src/main/java/net/runelite/client/external/ExternalPlugins
new file:   runelite-client/src/main/java/net/runelite/client/external/PrayerMap.java
new file:   runelite-client/src/main/java/net/runelite/client/external/Spells.java
new file:   runelite-client/src/main/java/net/runelite/client/external/Tab.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/Adonai.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/Buttons.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/Clickable.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/EquipmentMap.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/ExtUtils.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/Keyboard.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/MagicMap.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/MenuSession.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/PrayerMap.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/Spells.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/StatsMap.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/Tab.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/TabMap.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/mouse/ScreenPosition.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/widgets/WidgetID.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonai/widgets/WidgetInfo.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/Auto.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/ClickService.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/Interact.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/item/BankItem.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/item/GameTileObject.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/item/GroundItem.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/item/Items.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/item/UserBank.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/item/UserEquippedItems.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/item/UserInventory.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/location/WorldArea.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/menu/Menus.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/menu/examine/CacheKey.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/menu/examine/ExaminePlugin.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/menu/examine/ExamineType.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/menu/examine/PendingExamine.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/methods/Chats.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/npc/NPCs.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/objects/Objects.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/player/Players.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/screen/CanvasLocation.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/screen/Point.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/screen/Screen.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/screen/ScreenLocation.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/toolbox/Calculations.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/toolbox/HitBox.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/toolbox/Serialize.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/toolbox/Text.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/utils/Colors.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/utils/Coordinates.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/utils/GameStatus.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/utils/Messages.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/wrappers/Menu.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/wrappers/MenuEvents.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/wrappers/MenuInterface.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/wrappers/MenuOption.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/wrappers/MenuRow.java
new file:   runelite-client/src/main/java/net/runelite/client/external/adonaicore/wrappers/MouseSession.java
```