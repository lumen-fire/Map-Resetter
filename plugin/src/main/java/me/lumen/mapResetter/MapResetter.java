package me.lumen.mapResetter;

import me.lumen.mapResetter.commands.brigadier.PaperBrigadierMapSaveCommand;
import me.lumen.mapResetter.commands.bukkit.BukkitMapSaveCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
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
        //register api
        Bukkit.getServicesManager().register(me.lumen.mapResetterAPI.MapResetManager.class, MapResetManager.getInstance(), this, ServicePriority.Normal);
        //register commands
        if (hasPaperCommands()){
            PaperBrigadierMapSaveCommand.register(this);
        } else {
            BukkitMapSaveCommand.COMMAND.register(this);
        }
        MapResetManager.loadMapSaves();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static boolean hasPaperCommands(){
        try {
            Class.forName("io.papermc.paper.command.brigadier.Commands");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
