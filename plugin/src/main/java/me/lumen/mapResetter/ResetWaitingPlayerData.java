package me.lumen.mapResetter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class ResetWaitingPlayerData {
    private final Player player;
    private final Location lastLocation;

    public ResetWaitingPlayerData(@NonNull Player player) {
        this.player = player;
        this.lastLocation = player.getLocation();
    }

    public void teleportBack(World world) {
        lastLocation.setWorld(world);
        player.teleport(lastLocation);
    }
}
