# Map Resetter
![Map resetting showcase](assets/map%20resetting%20showcase.gif)  
A 1.21.6+ paper and spigot plugin to save worlds as map saves and reset worlds to those map saves.
Resets large worlds in seconds and even supports resetting entities.
Only supports normal worlds however, not nether or end worlds.

Useful for minigames, or whenever you need to reset a world to a map.

## Download Pages
[Modrinth](https://modrinth.com/plugin/map-resetter) | [Hangar](https://hangar.papermc.io/Lumen_Fire/Map-Resetter) | [SpigotMC](https://www.spigotmc.org/resources/map-resetter.138182/)

## Commands
Commands support brigadier on paper.
### /mapsave
The command for managing map saves.
If the world argument is optional, it will default to the world you are in if not specified.
For saving a map save you can also drop a custom map/world into the maps directory and run ``/mapresetter reload mapsaves``

| Subcommand                              | Permission         | Description                                            |
|-----------------------------------------|--------------------|--------------------------------------------------------|
| /mapsave save \<name> \[\<world>]       | mapresetter.save   | Create a new map save using the world file             |
| /mapsave delete \<map-save>             | mapresetter.delete | Delete a map save                                      |
| /mapsave list                           | mapresetter.list   | Show a list of all map saves in chat                   |
| /mapsave reset \<world> \<map-save>     | mapresetter.reset  | Reset a world to a map save                            |
| /mapsave update \<map-save> \[\<world>] | mapresetter.update | Update a map save to use the world file from the world |

### /mapresetter
The main plugin command for configuration and stuff

| Subcommand                                          | Permission          | Description                                                                                        |
|-----------------------------------------------------|---------------------|----------------------------------------------------------------------------------------------------|
| /mapresetter version                                | mapresetter.version | Shows the version of the plugin you are running in chat                                            |
| /mapresetter reload \[(messages\|mapsaves\|config)] | mapresetter.reload  | Reloads messages.yml, config.yml, and the map save save files, or only one of these if specified   |
| /mapresetter info                                   | mapresetter.info    | Sends the plugin description in chat                                                               |
| /mapresetter debug \<boolean>                       | mapresetter.debug   | Sets whether debugging logging is enabled - currently just whether to log skipped files in copying |

## Config
```yaml
#More config options may come in future

#Whether to show debug messages when a file is skipped copying
debug: false
```

## Messages
```yaml
#Supports mini message on paper
delete-map-save: "&eDeleted map save %mapsave%"
map-save-list-header: "&aMap resets:"
reset-map-save: "&eReset world %world% to map save %mapsave%"
create-map-save: "&aCreated new map save %mapsave% using world file from %world%"
update-map-save: "&aUpdated map save %mapsave% to use world file from %world%"
version-message: "&aMapresetter is running on version %version%"
reload-all: "&aReloaded messages, map save folders, and config"
reload-messages: "&aReloaded messages.yml"
reload-map-saves: "&aReloaded map save folders"
reload-config: "&aReloaded config.yml"
set-debug: "&aSet send debug logs to %debug%"
```

## Developer API
The plugin comes with an API you can hook into from bukkit plugins to create, reset, get map saves and more.
See the developer api usage in [DEVELOPER_API.md](DEVELOPER_API.md)


