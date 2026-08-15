package me.lumen.mapResetter.commands.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.lumen.mapResetter.MapResetManager;
import me.lumen.mapResetter.MapSave;
import me.lumen.mapResetter.commands.brigadier.args.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

import java.util.Collection;

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
                                context.getSource().getSender().sendMessage(Component.text("Deleted map save " + mapSave.getId(), NamedTextColor.YELLOW));
                                return Command.SINGLE_SUCCESS;
                            })
                    )
            )
            .then(Commands.literal("list")
                    .requires(Commands.restricted(stack -> stack.getSender().hasPermission("mapresetter.list")))
                    .executes(context -> {
                        CommandSender sender = context.getSource().getSender();
                        Collection<String> ids = MapResetManager.getInstance().getMapSaveIds();
                        sender.sendMessage(Component.text("Map resets:", NamedTextColor.GREEN));
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
                                        context.getSource().getSender().sendMessage(Component.text("Reset world " + world.getKey().value() + " to map save " + mapSave.getId(), NamedTextColor.YELLOW));
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


    private static int createSave(@NonNull CommandContext<CommandSourceStack> context, World world) {
        String id = context.getArgument("name", String.class);
        MapResetManager.getInstance().createMapSave(id, world);
        context.getSource().getSender().sendMessage(Component.text("Created new map save " + id + " using world file from " + world.getKey().value(), NamedTextColor.GREEN));
        return Command.SINGLE_SUCCESS;
    }

    private static int updateSave(@NonNull CommandContext<CommandSourceStack> context, @NonNull World world) {
        MapSave mapSave = context.getArgument("map-save", MapSave.class);
        mapSave.updateSave(world);
        context.getSource().getSender().sendMessage(Component.text("Updated map save " + mapSave.getId() + " to use world file from " + world.getKey().value(), NamedTextColor.GREEN));
        return Command.SINGLE_SUCCESS;
    }
}
