package me.lumen.mapResetter.commands.brigadier;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public class PaperBrigadierCommandManager {
    public static void register(@NonNull JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands registrar = event.registrar();
            registrar.register(PaperBrigadierMapSaveCommand.COMMAND, "Manage your map saves");
            registrar.register(PaperBrigadierMapResetterCommand.COMMAND, "The main plugin command - reload things, etc");
        });
    }
}
