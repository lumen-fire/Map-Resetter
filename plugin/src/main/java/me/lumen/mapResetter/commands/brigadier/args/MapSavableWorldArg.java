package me.lumen.mapResetter.commands.brigadier.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class MapSavableWorldArg implements CustomArgumentType<World, World> {
    private static final DynamicCommandExceptionType SPAWN_WORLD_UNALLOWED = new DynamicCommandExceptionType(o ->
            MessageComponentSerializer.message().serialize(Component.text("World " + o + " is the spawn world, the spawn world cannot be used for this action!")));
    private static final Dynamic2CommandExceptionType NOT_NORMAL_ENVIRONMENT = new Dynamic2CommandExceptionType((a, b) ->
            MessageComponentSerializer.message().serialize(Component.text("World " + a + " is a " + b + " world, only normal worlds are supported!")));

    private final boolean excludeSpawnWorld;

    public static final MapSavableWorldArg DEFAULT = new MapSavableWorldArg(false);
    public static final MapSavableWorldArg EXCLUDE_SPAWN_WORLD = new MapSavableWorldArg(true);

    private MapSavableWorldArg(boolean excludeSpawnWorld) {
        this.excludeSpawnWorld = excludeSpawnWorld;
    }

    @Override
    public @NonNull World parse(@NonNull StringReader reader) throws CommandSyntaxException {
        World world = getNativeType().parse(reader);
        if (world.getEnvironment() != World.Environment.NORMAL) {
            throw NOT_NORMAL_ENVIRONMENT.create(world.getKey().value(), world.getEnvironment().name().toLowerCase());
        }
        if (excludeSpawnWorld && Bukkit.getWorlds().getFirst().equals(world)) {
            throw SPAWN_WORLD_UNALLOWED.create(world.getKey().value());
        }
        return world;
    }

    @Override
    public @NonNull ArgumentType<World> getNativeType() {
        return ArgumentTypes.world();
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(@NonNull CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
        for (World world : Bukkit.getWorlds()) {
            if (world.getEnvironment() != World.Environment.NORMAL) {
                continue;
            }
            if (excludeSpawnWorld && Bukkit.getWorlds().getFirst().equals(world)) {
                continue;
            }
            builder.suggest(world.getKey().asString());
        }
        return builder.buildFuture();
    }
}
