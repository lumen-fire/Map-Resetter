package me.lumen.mapResetter.commands.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.lumen.mapResetter.MapResetManager;
import me.lumen.mapResetter.MapSave;
import me.lumen.mapResetter.commands.brigadier.args.*;
import me.lumen.mapResetter.messages.MessagesManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.Collection;

import static me.lumen.mapResetter.commands.brigadier.args.MapSavableWorldArg.NOT_NORMAL_ENVIRONMENT;

public class PaperBrigadierMapSaveCommand {
    public static final LiteralCommandNode<CommandSourceStack> COMMAND = Commands.literal("mapsave")
            .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.mapsave")))
            .then(Commands.literal("save")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.save")))
                    .then(Commands.argument("name", NewMapSaveArg.ARGUMENT)
                            .then(Commands.argument("world", MapSavableWorldArg.DEFAULT)
                                    .executes(context -> createSave(context, context.getArgument("world", World.class)))
                            )
                            .executes(context -> createSave(context, context.getSource().getLocation().getWorld()))
                    )
            )
            .then(Commands.literal("delete")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.delete")))
                    .then(Commands.argument("map-save", MapSaveArg.ARGUMENT)
                            .executes(context -> {
                                MapSave mapSave = context.getArgument("map-save", MapSave.class);
                                MapResetManager.getInstance().deleteMapSave(mapSave);
                                MessagesManager.get().sendDeleteMessage(context.getSource().getSender(), mapSave);
                                return Command.SINGLE_SUCCESS;
                            })
                    )
            )
            .then(Commands.literal("list")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.list")))
                    .executes(context -> {
                        CommandSender sender = context.getSource().getSender();
                        Collection<String> ids = MapResetManager.getInstance().getMapSaveIds();
                        MessagesManager.get().sendMapSaveListHeader(sender);
                        for (String id : ids) {
                            sender.sendMessage(Component.text(" - " + id, NamedTextColor.GREEN));
                        }
                        return ids.size();
                    })
            )
            .then(Commands.literal("reset")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.reset")))
                    .then(Commands.argument("world", MapSavableWorldArg.EXCLUDE_SPAWN_WORLD)
                            .then(Commands.argument("map-save", MapSaveArg.ARGUMENT)
                                    .executes(context -> {
                                        World world = context.getArgument("world", World.class);
                                        MapSave mapSave = context.getArgument("map-save", MapSave.class);
                                        mapSave.resetWorld(world);
                                        MessagesManager.get().sendResetMapSaveMessage(context.getSource().getSender(), world, mapSave);
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                    )
            )
            .then(Commands.literal("update")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.update")))
                    .then(Commands.argument("map-save", MapSaveArg.ARGUMENT)
                            .then(Commands.argument("world", MapSavableWorldArg.DEFAULT)
                                    .executes(context -> updateSave(context, context.getArgument("world", World.class)))
                            )
                            .executes(context -> updateSave(context, context.getSource().getLocation().getWorld()))
                    )
            )
            .build();


    private static int createSave(@NonNull CommandContext<CommandSourceStack> context, @NonNull World world) throws CommandSyntaxException {
        if (world.getEnvironment() != World.Environment.NORMAL) {
            throw NOT_NORMAL_ENVIRONMENT.create(world.getKey().value(), world.getEnvironment().name().toLowerCase());
        }
        String id = context.getArgument("name", String.class);
        MapResetManager.getInstance().createMapSave(id, world);
        MessagesManager.get().sendCreateMapSaveMessage(context.getSource().getSender(), world, id);
        return Command.SINGLE_SUCCESS;
    }

    private static int updateSave(@NonNull CommandContext<CommandSourceStack> context, @NonNull World world) throws CommandSyntaxException {
        if (world.getEnvironment() != World.Environment.NORMAL) {
            throw NOT_NORMAL_ENVIRONMENT.create(world.getKey().value(), world.getEnvironment().name().toLowerCase());
        }
        MapSave mapSave = context.getArgument("map-save", MapSave.class);
        mapSave.updateSave(world);
        MessagesManager.get().sendUpdateMapSaveMessage(context.getSource().getSender(), world, mapSave);
        return Command.SINGLE_SUCCESS;
    }
}
