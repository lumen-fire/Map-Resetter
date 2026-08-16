package me.lumen.mapResetter.commands.bukkit;

import me.lumen.mapResetter.MapResetManager;
import me.lumen.mapResetter.MapResetter;
import me.lumen.mapResetter.messages.MessagesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class BukkitMapResetterCommand implements CommandExecutor, TabCompleter {
    public static final BukkitMapResetterCommand COMMAND = new BukkitMapResetterCommand();
    private BukkitMapResetterCommand() {}

    public void register(@NonNull JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("mapresetter");
        assert command != null;
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        String firstArg = args[0];
        switch (firstArg) {
            case "version": {
                if (lacksPermission(sender, "mapresetter.version")) return false;
                if (args.length != 1) {
                    sendUsageMessage(sender);
                    return false;
                }
                String version = MapResetter.getPlugin().getDescription().getVersion();
                MessagesManager.get().sendVersionMessage(sender, version);
                return true;
            }
            case "reload": {
                if (lacksPermission(sender, "mapresetter.reload")) return false;
                if (args.length == 1){
                    MessagesManager.reloadMessages();
                    MapResetManager.getInstance().reloadMapSaves();
                    MapResetter.getPlugin().reloadConfig();
                    MessagesManager.get().sendReloadAllMessage(sender);
                    return true;
                }
                if (args.length != 2){
                    sendUsageMessage(sender);
                    return false;
                }
                String secondArg = args[1];
                switch (secondArg) {
                    case "messages": {
                        MessagesManager.reloadMessages();
                        MessagesManager.get().sendReloadMessagesMessage(sender);
                        return true;
                    }
                    case "mapsaves": {
                        MapResetManager.getInstance().reloadMapSaves();
                        MessagesManager.get().sendReloadMapSavesMessage(sender);
                        return true;
                    }
                    case "config": {
                        MapResetter.getPlugin().reloadConfig();
                        MessagesManager.get().sendReloadConfigMessage(sender);
                        return true;
                    }
                    case "default": {
                        sendUsageMessage(sender);
                        return false;
                    }
                }
            }
            case "info": {
                if (lacksPermission(sender, "mapresetter.info")) return false;
                if (args.length != 1) {
                    sendUsageMessage(sender);
                    return false;
                }
                String description = MapResetter.getPlugin().getDescription().getDescription();
                assert description != null;
                sender.sendMessage(ChatColor.GREEN + description);
                return true;
            }
            case "debug": {
                if (lacksPermission(sender, "mapresetter.set_debug")) return false;
                if (args.length != 2) {
                    sendUsageMessage(sender);
                    return false;
                }
                String secondArg = args[1];
                if (secondArg.equals("true")){
                    MapResetter.getPlugin().getConfig().set("debug", true);
                    MapResetter.getPlugin().saveConfig();
                    MessagesManager.get().sendSetDebugMessage(sender, true);
                    return true;
                } else if (secondArg.equals("false")){
                    MapResetter.getPlugin().getConfig().set("debug", false);
                    MapResetter.getPlugin().saveConfig();
                    MessagesManager.get().sendSetDebugMessage(sender, false);
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid boolean: " + secondArg);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        //if typing the first arg
        if (args.length == 1) {
            return getAllowedSubCommands(sender);
        }
        //if not typing the second arg
        if (args.length != 2){
            return List.of();
        }
        String firstArg = args[0];
        return switch (firstArg) {
            case "reload" -> List.of("messages", "mapsaves", "config");
            case "debug" -> List.of("true", "false");
            default -> List.of();
        };
    }

    private static boolean lacksPermission(@NonNull CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sendUsageMessage(sender);
            return true;
        }
        return false;
    }

    private static void sendUsageMessage(@NotNull CommandSender sender) {
        sender.sendMessage(getUsageMessage(sender));
    }

    private static @NonNull String getUsageMessage(@NonNull CommandSender sender) {
        String baseMessage = ChatColor.RED + "Usage: /mapresetter (";
        baseMessage += String.join("|", getAllowedSubCommands(sender));
        return baseMessage + ") [<arg>]";
    }

    /**Get the subcommands this command sender is allowed to use*/
    private static @NonNull List<String> getAllowedSubCommands(@NonNull CommandSender sender) {
        List<String> subCommands = new ArrayList<>();
        if (sender.hasPermission("mapresetter.version")){
            subCommands.add("version");
        }
        if (sender.hasPermission("mapresetter.reload")){
            subCommands.add("reload");
        }
        if (sender.hasPermission("mapresetter.info")){
            subCommands.add("info");
        }
        if (sender.hasPermission("mapresetter.set_debug")){
            subCommands.add("debug");
        }
        return subCommands;
    }
}
