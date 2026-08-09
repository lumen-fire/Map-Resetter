package me.lumen.mapResetter.commands.brigadier.args;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import me.lumen.mapResetter.MapResetManager;
import me.lumen.mapResetterAPI.MapSave;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class MapSaveArg implements CustomArgumentType.Converted<MapSave, String> {
    private static final DynamicCommandExceptionType MAP_SAVE_NOT_EXISTS = new DynamicCommandExceptionType(o -> MessageComponentSerializer.message().serialize(Component.text("Map save " + o + " does not exist!")));
    public static final MapSaveArg ARGUMENT = new MapSaveArg();
    private static final Pattern VALID_WORD = Pattern.compile("[a-zA-Z+-_.]+");
    private MapSaveArg() {}
    @Override
    public @NonNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(@NonNull CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
        for (String id : MapResetManager.getInstance().getMapSaveIds()){
            //if not a valid word argument type add quotations around it
            if (!VALID_WORD.matcher(id).matches() || id.contains(" ")){
                id = "\"" + id + "\"";
            }
            builder.suggest(id);
        }
        return builder.buildFuture();
    }

    @Override
    public @NonNull MapSave convert(@NonNull String nativeType) throws CommandSyntaxException {
        Optional<MapSave> mapSave = MapResetManager.getInstance().getMapSave(nativeType);
        if (mapSave.isEmpty()){
            throw MAP_SAVE_NOT_EXISTS.create(nativeType);
        }
        return mapSave.get();
    }
}
