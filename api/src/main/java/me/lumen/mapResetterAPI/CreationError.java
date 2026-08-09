package me.lumen.mapResetterAPI;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public enum CreationError {
    ILLEGAL_CHARACTERS(s -> "There are illegal characters in " + s + "!", id -> id.contains("\"") || id.contains("/") || id.contains("\\") || id.contains(">") || id.contains("<") || id.contains(":") || id.contains("?") || id.contains("|") || id.contains("*")),
    ALREADY_EXISTS(s -> s + " already exists!", s -> MapResetterAPI.get().getMapSaveIds().stream().anyMatch(id -> id.equalsIgnoreCase(s))),
    EMPTY_NAME(ignored -> "The name cannot be empty!", String::isEmpty);

    private final Function<String, String> error;
    private final Predicate<String> predicate;
    CreationError(Function<String, String> error, Predicate<String> predicate) {
        this.error = error;
        this.predicate = predicate;
    }

    /**
     * Get an error message for this error
     * @param attemptedName the name that you tried to create
     * @return an error message based on the attempted name
     */
    public String getErrorMessage(String attemptedName) {
        return error.apply(attemptedName);
    }

    /**
     * Get if a string is a valid name for a map save
     * @param attemptedName the name you are attempting to create
     * @return the creation error if any match, or null if it succeeded
     */
    public static @Nullable CreationError getError(String attemptedName) {
        for (CreationError c : CreationError.values()) {
            if (c.predicate.test(attemptedName)) {
                return c;
            }
        }
        return null;
    }
}
