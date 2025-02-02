package io.github.thebusybiscuit.slimefun4.core.ticker;

import java.util.UUID;

import org.bukkit.Location;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import lombok.Getter;

@Getter
public class TickLocation {
    private final BlockPosition position;
    private final UUID uuid;

    public TickLocation(BlockPosition position) {
        this.position = position;
        uuid = null;
    }

    public TickLocation(BlockPosition position, UUID uuid) {
        this.position = position;
        this.uuid = uuid;
    }

    public boolean isUniversal() {
        return uuid != null;
    }

    public Location getLocation() {
        return position.toLocation();
    }
}
