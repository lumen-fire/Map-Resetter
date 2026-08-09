package me.lumen.mapResetter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MapSave implements me.lumen.mapResetterAPI.MapSave {
    private final String id;

    MapSave(@NotNull String id){
        this.id = id;
    }

    @Override
    public @NonNull String getId() {
        return id;
    }

    @Override
    public void resetWorld(@NonNull World world) {
        Set<ResetWaitingPlayerData> waitingPlayers = new HashSet<>();
        for (Player player : world.getPlayers()) {
            ResetWaitingPlayerData resetWaitingPlayerData = new ResetWaitingPlayerData(player);
            waitingPlayers.add(resetWaitingPlayerData);
            Location spawn = Bukkit.getWorlds().getFirst().getSpawnLocation();
            Location teleportTo = new Location(spawn.getWorld(), spawn.getX(), spawn.getY() + 1000000 + ThreadLocalRandom.current().nextDouble(1000), spawn.getZ());
            player.teleport(teleportTo);
        }
        boolean result = Bukkit.unloadWorld(world, false);
        if (!result){
            MapResetter.getPlugin().getLogger().warning("Failed to unload world " + world.getName());
            return;
        }
        File sourceDir = new File(MapResetManager.getMapSaveFolder(), id);
        MapResetManager.copyDirectory(sourceDir, world.getWorldFolder());
        world = Bukkit.createWorld(WorldCreator.name(world.getName()));
        for (ResetWaitingPlayerData resetWaitingPlayerData : waitingPlayers) {
            resetWaitingPlayerData.teleportBack(world);
        }
    }

    @Override
    public void updateSave(@NonNull World world) {
        world.save();
        File targetDir = new File(MapResetManager.getMapSaveFolder(), id);
        MapResetManager.copyDirectory(world.getWorldFolder(), targetDir);
    }
}
