package city.norain.slimefun4.utils;

import org.bukkit.OfflinePlayer;

import city.norain.slimefun4.SlimefunExtended;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerUtil {
    public boolean isConnected(OfflinePlayer player) {
        if (SlimefunExtended.getMinecraftVersion().isAtLeast(1, 20)) {
            return player.isConnected();
        } else {
            return player.isOnline();
        }
    }
}
