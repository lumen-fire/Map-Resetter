package me.lumen.mapResetterAPI;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface MapResetManager {
    @NotNull Collection<String> getMapSaveIds();
    @NotNull Optional<MapSave> getMapSave(String id);

    /**
     * Attempt to create a map save
     * @param id the id of the map save to create
     * @return a {@link CreationError} if there was an error, or null if it succeeded
     */
    @Nullable CreationError createMapSave(@NotNull String id, World loadFrom);

    void deleteMapSave(@NotNull MapSave mapSave);
}
