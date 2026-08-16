package me.lumen.mapResetter;

import me.lumen.mapResetter.commands.brigadier.PaperBrigadierCommandManager;
import me.lumen.mapResetter.commands.bukkit.BukkitMapResetterCommand;
import me.lumen.mapResetter.commands.bukkit.BukkitMapSaveCommand;
import me.lumen.mapResetter.messages.MessagesManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
            PaperBrigadierCommandManager.register(this);
        } else {
            BukkitMapSaveCommand.COMMAND.register(this);
            BukkitMapResetterCommand.COMMAND.register(this);
        }
        files();
    }

    private void files(){
        if (!new File(getDataFolder(), "messages.yml").exists()) {
            saveResource("messages.yml", false);
        }
        saveDefaultConfig();
        MessagesManager.reloadMessages();
        MapResetManager.getInstance().reloadMapSaves();
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
