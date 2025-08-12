package city.norain.slimefun4.utils;

import city.norain.slimefun4.compatibillty.CompatibilityUtil;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import io.github.thebusybiscuit.slimefun4.core.debug.Debug;
import io.github.thebusybiscuit.slimefun4.core.debug.TestCase;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InventoryUtil {
    public void openInventory(Player p, Inventory inventory) {
        Debug.log(TestCase.INVENTORY, "Opening inv in {}", Bukkit.isPrimaryThread() ? "sync" : "async");

        if (p == null || inventory == null) {
            return;
        }

        if (!CompatibilityUtil.isConnected(p)) {
            Debug.log(
                    TestCase.INVENTORY,
                    "Tried to open an inventory for a player that is not connected: " + p.getName());
            return;
        }

        if (Bukkit.isPrimaryThread()) {
            p.openInventory(inventory);
        } else {
            Slimefun.runSync(() -> p.openInventory(inventory));
        }
    }

    /**
     * Close inventory for all viewers.
     *
     * @param inventory {@link Inventory}
     */
    public void closeInventory(Inventory inventory) {
        if (inventory == null) {
            return;
        }

        if (Bukkit.isPrimaryThread()) {
            new LinkedList<>(inventory.getViewers()).forEach(HumanEntity::closeInventory);
        } else {
            Slimefun.runSync(() -> new LinkedList<>(inventory.getViewers()).forEach(HumanEntity::closeInventory));
        }
    }

    public void closeInventory(Inventory inventory, Runnable callback) {
        closeInventory(inventory);

        if (Bukkit.isPrimaryThread()) {
            callback.run();
        } else {
            Slimefun.runSync(callback);
        }
    }
}
