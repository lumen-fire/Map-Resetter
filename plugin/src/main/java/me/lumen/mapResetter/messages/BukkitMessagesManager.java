package me.lumen.mapResetter.messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

@SuppressWarnings("deprecation")
public class BukkitMessagesManager extends MessagesManager {
    static BukkitMessagesManager INSTANCE = new BukkitMessagesManager();
    private  BukkitMessagesManager(){}
    private static @NonNull String getMessage(String key, Function<String, String> replacer) {
        String raw = MessagesManager.getBaseMessage(key, replacer);
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    @Override
    void sendMessage(@NonNull CommandSender sender, String key, Function<String, String> replacer) {
        sender.sendMessage(getMessage(key, replacer));
    }
}
