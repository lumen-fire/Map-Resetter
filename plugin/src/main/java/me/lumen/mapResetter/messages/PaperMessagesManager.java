package me.lumen.mapResetter.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class PaperMessagesManager extends MessagesManager{
    static PaperMessagesManager INSTANCE = new PaperMessagesManager();
    private  PaperMessagesManager(){}
    private static @NonNull Component getMessage(String key, Function<String, String> placeholderReplacer) {
        String raw = MessagesManager.getBaseMessage(key, placeholderReplacer);
        //turn legacy codes into a component
        TextComponent legacyDeserialized = LegacyComponentSerializer.legacyAmpersand().deserialize(raw);
        //turn component into a minimessage
        String minimessageText = MiniMessage.miniMessage().serialize(legacyDeserialized);

        //return the deserialized minimessage text
        return MiniMessage.miniMessage().deserialize(minimessageText);
    }

    @Override
    void sendMessage(@NonNull CommandSender sender, String key, Function<String, String> replacer) {
        sender.sendMessage(getMessage(key, replacer));
    }
}
