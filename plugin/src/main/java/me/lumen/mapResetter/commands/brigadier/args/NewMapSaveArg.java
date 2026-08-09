package me.lumen.mapResetter.commands.brigadier.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import me.lumen.mapResetterAPI.CreationError;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

/**
 * An argument type to ensure the input is a valid map save name
 */
public class NewMapSaveArg implements CustomArgumentType<String, String> {

    private static final DynamicCommandExceptionType CREATION_ERROR = new DynamicCommandExceptionType(o -> MessageComponentSerializer.message().serialize(Component.text(o.toString())));
    public static final NewMapSaveArg ARGUMENT = new NewMapSaveArg();
    private NewMapSaveArg() {}
    @Override
    public @NonNull String parse(@NonNull StringReader reader) throws CommandSyntaxException {
        String argument = getNativeType().parse(reader);
        CreationError error = CreationError.getError(argument);
        if (error != null) {
            throw CREATION_ERROR.create(error.getErrorMessage(argument));
        }
        return argument;
    }

    @Override
    public @NonNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
}
