package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import city.norain.slimefun4.compatibillty.VersionedEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.SyntheticEmerald;

/**
 * This {@link Listener} prevents any {@link SlimefunItem} from being used to trade with
 * Villagers, with one exception being {@link SyntheticEmerald}.
 *
 * @author TheBusyBiscuit
 *
 */
public class VillagerTradingListener implements Listener {

    public VillagerTradingListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPreTrade(InventoryClickEvent e) throws IllegalAccessException, InvocationTargetException {
        Inventory clickedInventory = VersionedEvent.getClickedInventory(e);
        Inventory topInventory = VersionedEvent.getTopInventory(e);

        if (clickedInventory != null && topInventory.getType() == InventoryType.MERCHANT) {
            if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
                e.setCancelled(true);
                return;
            }

            if (clickedInventory.getType() == InventoryType.MERCHANT) {
                e.setCancelled(isUnallowed(SlimefunItem.getByItem(e.getCursor())));
            } else {
                e.setCancelled(isUnallowed(SlimefunItem.getByItem(e.getCurrentItem())));
            }

            if (e.getResult() == Result.DENY) {
                Slimefun.getLocalization().sendMessage((Player) e.getWhoClicked(), "villagers.no-trading", true);
            }
        }
    }

    private boolean isUnallowed(@Nullable SlimefunItem item) {
        return item != null
                && !(item instanceof VanillaItem)
                && !(item instanceof SyntheticEmerald)
                && !item.isDisabled();
    }
}
