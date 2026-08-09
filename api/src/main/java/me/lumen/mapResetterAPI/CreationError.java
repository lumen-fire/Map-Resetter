package me.lumen.mapResetterAPI;

import java.util.function.Function;

public enum CreationError {
    ILLEGAL_CHARACTERS(s -> "There are illegal characters in " + s + "!"),
    ALREADY_EXISTS(s -> s + " already exists!"),
    EMPTY_NAME(ignored -> "The name cannot be empty!");

    private final Function<String, String> error;
    CreationError(Function<String, String> error) {
        this.error = error;
    }

    /**
     * Get an error message for this error
     * @param attemptedName the name that you tried to create
     * @return an error message based on the attempted name
     */
    public String getErrorMessage(String attemptedName) {
        return error.apply(attemptedName);
    }
}
