package me.lumen.mapResetter.messages;

import me.lumen.mapResetter.MapResetter;
import me.lumen.mapResetterAPI.MapSave;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.util.function.Function;

public abstract class MessagesManager {
    private static MessagesManager instance;
    private static final File messagesFile = new File(MapResetter.getPlugin().getDataFolder(), "messages.yml");
    private static YamlConfiguration messages = YamlConfiguration.loadConfiguration(messagesFile);

    static String getBaseMessage(String key) {
        return messages.getString(key);
    }

    static String getBaseMessage(String key, @NonNull Function<String, String> placeholderReplacer){
        return placeholderReplacer.apply(getBaseMessage(key));
    }

    public static @NotNull MessagesManager get(){
        if (instance == null){
            if (hasPaper()){
                instance = PaperMessagesManager.INSTANCE;
            } else {
                instance = BukkitMessagesManager.INSTANCE;
            }
        }
        return instance;
    }

    public static void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    private static boolean hasPaper(){
        try {
            //hopefully there won't be adventure but Player isn't an audience in any environments
            Class.forName("net.kyori.adventure.text.Component");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public void sendDeleteMessage(CommandSender sender, MapSave deleted){
        sendMessage(sender, "delete-map-save", s -> s.replace("%mapsave%", deleted.getId()));
    }

    public void sendMapSaveListHeader(CommandSender sender){
        sendMessage(sender, "map-save-list-header", s -> s);
    }

    public void sendResetMapSaveMessage(CommandSender sender, World world, MapSave mapSave){
        sendMessage(sender, "reset-map-save", s -> s.replace("%mapsave%", mapSave.getId()).replace("%world%", world.getName()));
    }

    public void sendCreateMapSaveMessage(CommandSender sender, World world, String created){
        sendMessage(sender, "create-map-save", s -> s.replace("%mapsave%", created).replace("%world%", world.getName()));
    }

    public void sendUpdateMapSaveMessage(CommandSender sender, World world, MapSave mapSave){
        sendMessage(sender, "update-map-save", s -> s.replace("%mapsave%", mapSave.getId()).replace("%world%", world.getName()));
    }

    public void sendVersionMessage(CommandSender sender, String version){
        sendMessage(sender, "version-message", s -> s.replace("%version%", version));
    }

    public void sendReloadAllMessage(CommandSender sender){
        sendMessage(sender, "reload-all", s -> s);
    }

    public void sendReloadMessagesMessage(CommandSender sender){
        sendMessage(sender, "reload-messages", s -> s);
    }

    public void sendReloadMapSavesMessage(CommandSender sender){
        sendMessage(sender, "reload-map-saves", s -> s);
    }

    public void sendReloadConfigMessage(CommandSender sender){
        sendMessage(sender, "reload-config", s -> s);
    }

    public void sendSetDebugMessage(CommandSender sender, boolean debug){
        sendMessage(sender, "set-debug", s -> s.replace("%debug%", Boolean.toString(debug)));
    }

    abstract void sendMessage(@NonNull CommandSender sender, String key, Function<String, String> replacer);
}
