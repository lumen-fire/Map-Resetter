package me.lumen.exampleAPI;

import me.lumen.mapResetterAPI.CreationError;
import me.lumen.mapResetterAPI.MapResetterAPI;
import me.lumen.mapResetterAPI.MapSave;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class ExampleAPIUsage {
    public static void run(JavaPlugin plugin){
        //GETTING MAP SAVE IDS
        for (String id : MapResetterAPI.get().getMapSaveIds()){
            plugin.getLogger().info("Found map save: " + id);
        }

        //GETTING A MAP SAVE
        Optional<MapSave> testSave = MapResetterAPI.get().getMapSave("test");
        //this is more an example of getting a map save, not checking if one exists, as when checking if one exists it would be quicker to use MapResetterAPI.get().getMapSaveIds().contains("id")
        if (testSave.isPresent()){
            MapSave ignored = testSave.get();
            plugin.getLogger().info("Map save 'test' found!");
        } else {
            plugin.getLogger().info("Map save 'test' not found!");
        }

        //CREATING A MAP SAVE
        CreationError error = MapResetterAPI.get().createMapSave("test1", Bukkit.getWorlds().getFirst());
        if (error != null){
            plugin.getLogger().info("Error creating map save test:" + error.getErrorMessage("test1"));
        }

        MapSave test1 = MapResetterAPI.get().getMapSave("test1").orElseThrow();
        //getting a map saves id
        assert test1.getId().equals("test1");

        World testWorld = Bukkit.createWorld(WorldCreator.name("test"));
        if (testWorld == null){
            plugin.getLogger().warning("Error creating map save test world");
        } else {
            //UPDATING A MAP SAVE
            test1.updateSave(testWorld);
            plugin.getLogger().info("Updated map save test1 to use world file from world test");

            //RESETTING A MAP SAVE
            //pointlessly resets the world to itself, as I already updated the save to this world
            test1.resetWorld(testWorld);
        }

        //DELETING A MAP SAVE
        MapResetterAPI.get().deleteMapSave(test1);
        assert !MapResetterAPI.get().getMapSaveIds().contains("test1");
        plugin.getLogger().info("Deleted map save test1");

        plugin.getLogger().info("Done!");
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}
