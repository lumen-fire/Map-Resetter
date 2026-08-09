package me.lumen.mapResetterAPI;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface MapSave {
    @NotNull String getId();
    void resetWorld(@NotNull World world);

    /**
     * Updates the save
     * @param world the world to load the new save from
     */
    void updateSave(@NotNull World world);
}
