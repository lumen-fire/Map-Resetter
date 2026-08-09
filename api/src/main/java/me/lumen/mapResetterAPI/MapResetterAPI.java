package me.lumen.mapResetterAPI;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class MapResetterAPI {
    private static MapResetManager instance;

    /**
     * Get the api - trys to load it from bukkit services if this is the first time accessed, and throws an {@link IllegalStateException} if not found
     * @return an instance of the api
     */
    public static @NotNull MapResetManager get() {
        if (instance == null){
            instance = Bukkit.getServicesManager().load(MapResetManager.class);
        }
        //if the instance is still null throw an error
        if (instance == null){
            throw new IllegalStateException("Map resetter api is not loaded yet");
        }
        return instance;
    }
}
