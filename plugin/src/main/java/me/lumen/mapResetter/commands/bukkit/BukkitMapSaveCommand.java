package me.lumen.mapResetter.commands.bukkit;

import me.lumen.mapResetter.MapResetManager;
import me.lumen.mapResetter.messages.MessagesManager;
import me.lumen.mapResetterAPI.CreationError;
import me.lumen.mapResetterAPI.MapSave;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The command executor for the bukkit and spigot version of the command.
 */
@SuppressWarnings("deprecation")
public class BukkitMapSaveCommand implements CommandExecutor, TabCompleter {

    public static final BukkitMapSaveCommand COMMAND = new BukkitMapSaveCommand();

    private BukkitMapSaveCommand() {}

    public void register(@NonNull JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("mapsave");
        assert command != null;
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    private static @NonNull String getUsageMessage(@NonNull CommandSender sender) {
        String baseMessage = ChatColor.RED + "Usage: /mapsave (";
        baseMessage += String.join("|", getAllowedSubCommands(sender));
        return baseMessage + ") [args...]";
    }

    private static boolean lacksPermission(@NonNull CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(getUsageMessage(sender));
            return true;
        }
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0){
            sender.sendMessage(getUsageMessage(sender));
            return false;
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "save" -> {
                if (lacksPermission(sender, "mapresetter.save")) {
                    return false;
                }
                if (args.length != 3 && args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " save <name> <world>");
                    return false;
                }
                String saveName = args[1];
                Optional<World> worldOptional = getWorldOrPlayersWorld(sender, 2, args);
                if (worldOptional.isEmpty()){
                    return false;
                }
                World world = worldOptional.get();

                CreationError creationError = MapResetManager.getInstance().createMapSave(saveName, world);
                if (creationError != null) {
                    sender.sendMessage(ChatColor.RED + creationError.getErrorMessage(saveName));
                    return false;
                }
                MessagesManager.get().sendCreateMapSaveMessage(sender, world, saveName);
                return true;
            }
            case "delete" -> {
                if (lacksPermission(sender, "mapresetter.delete")) {
                    return false;
                }
                if (args.length != 2){
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " delete <save>");
                    return false;
                }
                String saveName = args[1];
                Optional<MapSave> mapSave = MapResetManager.getInstance().getMapSave(saveName);
                if (mapSave.isEmpty()){
                    sender.sendMessage(ChatColor.RED + "Map save " + saveName + " does not exist!");
                    return false;
                }
                MapResetManager.getInstance().deleteMapSave(mapSave.get());
                MessagesManager.get().sendDeleteMessage(sender, mapSave.get());
                return true;
            }
            case "reset" -> {
                if (lacksPermission(sender, "mapresetter.reset")) {
                    return false;
                }
                if (args.length != 3){
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " reset <world> <save>");
                    return false;
                }

                String worldName = args[1];
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    sender.sendMessage(ChatColor.RED + "World " + worldName + " does not exist! Usage: /" + label + " reset <world> <save>");
                    return false;
                }

                if (Bukkit.getWorlds().getFirst().equals(world)){
                    sender.sendMessage(ChatColor.RED + worldName + " is the default world in the server, cannot be reset!");
                    return false;
                }

                if (world.getEnvironment() != World.Environment.NORMAL) {
                    sender.sendMessage(ChatColor.RED + "World " + world.getName() + " is a " + world.getEnvironment().name() + " world, only normal worlds are supported!");
                    return false;
                }

                String saveName = args[2];
                Optional<MapSave> mapSave = MapResetManager.getInstance().getMapSave(saveName);
                if (mapSave.isEmpty()){
                    sender.sendMessage(ChatColor.RED + "Map save " + saveName + " does not exist!");
                    return false;
                }

                mapSave.get().resetWorld(world);
                MessagesManager.get().sendResetMapSaveMessage(sender, world, mapSave.get());
                return true;
            }
            case "list" -> {
                if (lacksPermission(sender, "mapresetter.list")) {
                    return false;
                }
                MessagesManager.get().sendMapSaveListHeader(sender);
                for (String mapId : MapResetManager.getInstance().getMapSaveIds()){
                    sender.sendMessage(ChatColor.GREEN + " - " + mapId);
                }
                return true;
            }
            case "update" -> {
                if (lacksPermission(sender, "mapresetter.update")) {
                    return false;
                }
                if (args.length != 3 && args.length != 2){
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " update <save> <world>");
                    return false;
                }

                String saveName = args[1];
                Optional<MapSave> mapSave = MapResetManager.getInstance().getMapSave(saveName);
                if (mapSave.isEmpty()){
                    sender.sendMessage(ChatColor.RED + "Map save " + saveName + " does not exist!");
                    return false;
                }

                Optional<World> worldOptional = getWorldOrPlayersWorld(sender, 2, args);
                if (worldOptional.isEmpty()){
                    return false;
                }
                World world = worldOptional.get();

                mapSave.get().updateSave(world);
                MessagesManager.get().sendUpdateMapSaveMessage(sender, world, mapSave.get());
                return true;
            }
            default -> {
                sender.sendMessage(getUsageMessage(sender));
                return false;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return getAllowedSubCommands(sender);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "save" -> {
                if (!sender.hasPermission("mapresetter.save")) {
                    return List.of();
                }
                if (args.length == 2){
                    return List.of();
                } else if (args.length == 3){
                    return getMapSavableWorlds(false);
                }
            }
            case "reset" -> {
                if (!sender.hasPermission("mapresetter.reset")) {
                    return List.of();
                }
                if (args.length == 2){
                    return getMapSavableWorlds(true);
                } else if (args.length == 3){
                    return MapResetManager.getInstance().getMapSaveIds().stream().toList();
                }
            }
            case "delete" -> {
                if (!sender.hasPermission("mapresetter.delete")) {
                    return List.of();
                }
                if (args.length == 2){
                    return MapResetManager.getInstance().getMapSaveIds().stream().toList();
                }
            }
            case "update" -> {
                if (!sender.hasPermission("mapresetter.update")) {
                    return List.of();
                }
                if (args.length == 2){
                    return MapResetManager.getInstance().getMapSaveIds().stream().toList();
                } else if (args.length == 3){
                    return getMapSavableWorlds(false);
                }
            }
        }
        return List.of();
    }

    /**Get the subcommands this command sender is allowed to use*/
    private static @NonNull List<String> getAllowedSubCommands(@NonNull CommandSender sender) {
        List<String> subCommands = new ArrayList<>();
        if (sender.hasPermission("mapresetter.save")){
            subCommands.add("save");
        }
        if (sender.hasPermission("mapresetter.reset")){
            subCommands.add("reset");
        }
        if (sender.hasPermission("mapresetter.delete")){
            subCommands.add("delete");
        }
        if (sender.hasPermission("mapresetter.list")){
            subCommands.add("list");
        }
        if (sender.hasPermission("mapresetter.update")){
            subCommands.add("update");
        }
        return subCommands;
    }

    private static @NotNull List<String> getMapSavableWorlds(boolean excludeSpawnWorld){
        ArrayList<String> worlds = new ArrayList<>();
        for (World world : Bukkit.getWorlds()){
            if (world.getEnvironment() != World.Environment.NORMAL || (excludeSpawnWorld && Bukkit.getWorlds().getFirst().equals(world))){
                continue;
            }
            worlds.add(world.getName());
        }
        return worlds;
    }

    @SuppressWarnings("SameParameterValue")
    private static Optional<World> getWorldOrPlayersWorld(CommandSender sender, int worldNameIndex, String @NonNull [] args){
        World world;
        if (args.length == worldNameIndex + 1) {
            String worldName = args[worldNameIndex];
            world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.sendMessage(ChatColor.RED + "World " + worldName + " does not exist!");
                return Optional.empty();
            }
        } else {
            if (sender instanceof Player player){
                world = player.getWorld();
            } else {
                sender.sendMessage(getUsageMessage(sender));
                return Optional.empty();
            }
        }

        if (world.getEnvironment() != World.Environment.NORMAL) {
            sender.sendMessage(ChatColor.RED + "World " + world.getName() + " is a " + world.getEnvironment().name() + " world, only normal worlds are supported!");
            return Optional.empty();
        }
        return Optional.of(world);
    }
}
