package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import io.github.thebusybiscuit.slimefun4.api.events.ExplosiveToolBreakBlocksEvent;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockBreakEvent;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.core.networks.NetworkManager;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This {@link Listener} is responsible for all updates to a {@link Network}.
 *
 * @author meiamsome
 * @author TheBusyBiscuit
 *
 * @see Network
 * @see NetworkManager
 *
 */
@EnableAsync
public class NetworkListener implements Listener {

    /**
     * Our {@link NetworkManager} instance.
     */
    private final NetworkManager manager;

    public NetworkListener(@Nonnull Slimefun plugin, @Nonnull NetworkManager manager) {
        this.manager = manager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Async
    @EventHandler
    public void onBlockBreak(SlimefunBlockBreakEvent e) {
        manager.updateAllNetworks(e.getBlockBroken().getLocation());
    }

    @Async
    @EventHandler
    public void onBlockPlace(SlimefunBlockPlaceEvent e) {
        manager.updateAllNetworks(e.getBlockPlaced().getLocation());
    }

    @Async
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplosiveToolUse(ExplosiveToolBreakBlocksEvent e) {
        // Fixes #3013 - Also update networks when using an explosive tool
        for (Block b : e.getAdditionalBlocks()) {
            manager.updateAllNetworks(b.getLocation());
        }
    }
}
