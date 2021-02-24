package io.github.thebusybiscuit.slimefun4.core.handlers;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

@FunctionalInterface
public interface BlockBreakHandler extends ItemHandler {

    /**
     * Whether an explosion is allowed to destroy this block.
     */
    private final boolean allowExplosions;

    /**
     * This constructs a new {@link BlockBreakHandler}.
     * 
     * @param allowAndroids
     *            Whether a {@link MinerAndroid} is allowed to break blocks of this type
     * @param allowExplosions
     *            Whether blocks of this type are allowed to be broken by explosions
     */
    public BlockBreakHandler(boolean allowAndroids, boolean allowExplosions) {
        this.allowAndroids = allowAndroids;
        this.allowExplosions = allowExplosions;
    }

    @ParametersAreNonnullByDefault
    public abstract void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops);

    @ParametersAreNonnullByDefault
    public void onExplode(Block b, List<ItemStack> drops) {
        // This can be overridden, if necessary
    }

    @ParametersAreNonnullByDefault
    public void onAndroidBreak(AndroidMineEvent e) {
        // This can be overridden, if necessary
    }

    /**
     * This returns whether an explosion is able to break the given {@link Block}.
     * 
     * @param b
     *            The {@link Block}
     * @return Whether explosions can destroy this {@link Block}
     */
    public boolean isExplosionAllowed(@Nonnull Block b) {
        /*
         * By default our flag is returned, but you can override it
         * to be handled on a per-Block basis.
         */
        return allowExplosions;
    }

    /**
     * This returns whether a {@link MinerAndroid} is allowed to break
     * the given {@link Block}.
     * 
     * @param b
     *            The {@link Block}
     * 
     * @return Whether androids can break the given {@link Block}
     */
    public boolean isAndroidAllowed(@Nonnull Block b) {
        /*
         * By default our flag is returned, but you can override it
         * to be handled on a per-Block basis.
         */
        return allowAndroids;
    }

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return BlockBreakHandler.class;
    }
}
