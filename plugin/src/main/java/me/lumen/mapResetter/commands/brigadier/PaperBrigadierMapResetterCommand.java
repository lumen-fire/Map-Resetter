package me.lumen.mapResetter.commands.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.lumen.mapResetter.MapResetManager;
import me.lumen.mapResetter.MapResetter;
import me.lumen.mapResetter.messages.MessagesManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class PaperBrigadierMapResetterCommand {
    public static final LiteralCommandNode<CommandSourceStack> COMMAND = Commands.literal("mapresetter")
            .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.mapresettercommand")))
            .then(Commands.literal("version")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.version")))
                    .executes(context -> {
                        CommandSender sender = context.getSource().getSender();
                        String version = MapResetter.getPlugin().getPluginMeta().getVersion();
                        MessagesManager.get().sendVersionMessage(sender, version);
                        return Command.SINGLE_SUCCESS;
                    })
            )
            .then(Commands.literal("reload")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.reload")))
                    .executes(context -> {
                        CommandSender sender = context.getSource().getSender();
                        MessagesManager.reloadMessages();
                        MapResetManager.getInstance().reloadMapSaves();
                        MapResetter.getPlugin().reloadConfig();
                        MessagesManager.get().sendReloadAllMessage(sender);
                        return Command.SINGLE_SUCCESS;
                    })
                    .then(Commands.literal("messages")
                            .executes(context -> {
                                CommandSender sender = context.getSource().getSender();
                                MessagesManager.reloadMessages();
                                MessagesManager.get().sendReloadMessagesMessage(sender);
                                return Command.SINGLE_SUCCESS;
                            })
                    )
                    .then(Commands.literal("mapsaves")
                            .executes(context -> {
                                CommandSender sender = context.getSource().getSender();
                                MapResetManager.getInstance().reloadMapSaves();
                                MessagesManager.get().sendReloadMapSavesMessage(sender);
                                return Command.SINGLE_SUCCESS;
                            })
                    )
                    .then(Commands.literal("config")
                            .executes(context -> {
                                CommandSender sender = context.getSource().getSender();
                                MapResetter.getPlugin().reloadConfig();
                                MessagesManager.get().sendReloadConfigMessage(sender);
                                return Command.SINGLE_SUCCESS;
                            })
                    )
            )
            .then(Commands.literal("info")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.info")))
                    .executes(context -> {
                        CommandSender sender = context.getSource().getSender();
                        String description = MapResetter.getPlugin().getPluginMeta().getDescription();
                        assert description != null;
                        sender.sendMessage(Component.text(description, NamedTextColor.GREEN));
                        return Command.SINGLE_SUCCESS;
                    })
            )
            .then(Commands.literal("debug")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.set_debug")))
                    .then(Commands.argument("debug-enabled", BoolArgumentType.bool())
                            .executes(context -> {
                                CommandSender sender = context.getSource().getSender();
                                boolean debugEnabled = BoolArgumentType.getBool(context, "debug-enabled");
                                MapResetter.getPlugin().getConfig().set("debug", debugEnabled);
                                MapResetter.getPlugin().saveConfig();
                                MessagesManager.get().sendSetDebugMessage(sender, debugEnabled);
                                return Command.SINGLE_SUCCESS;
                            })
                    )
            )
            .build();
}
