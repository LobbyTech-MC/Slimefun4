package city.norain.slimefun4.utils;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class InventoryUtil {
    /**
     * Close inventory for all viewers.
     *
     * @param inventory {@link Inventory}
     */
    public static void closeInventory(Inventory inventory) {
        if (inventory == null) {
            return;
        }

        if (Bukkit.isPrimaryThread()) {
            new LinkedList<>(inventory.getViewers()).forEach(HumanEntity::closeInventory);
        } else {
            Slimefun.runSync(() -> new LinkedList<>(inventory.getViewers()).forEach(HumanEntity::closeInventory));
        }
    }

    public static void closeInventory(Inventory inventory, Runnable callback) {
        closeInventory(inventory);

        if (Bukkit.isPrimaryThread()) {
            callback.run();
        } else {
            Slimefun.runSync(callback);
        }
    }
}
