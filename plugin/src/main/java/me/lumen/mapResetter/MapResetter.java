package me.lumen.mapResetter;

import me.lumen.mapResetter.commands.bukkit.BukkitMapSaveCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MapResetter extends JavaPlugin{
    private static MapResetter instance;
    public static @NotNull MapResetter getPlugin() {
        if (instance == null) {
            throw new IllegalStateException("Plugin has not been initialized");
        }
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        BukkitMapSaveCommand.COMMAND.register(this);
        MapResetManager.loadMapSaves();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
