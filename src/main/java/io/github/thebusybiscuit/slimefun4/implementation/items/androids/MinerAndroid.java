package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.InfiniteBlockGenerator;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedParticle;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link MinerAndroid} is a variant of the {@link ProgrammableAndroid} which
 * is able to break blocks.
 * The core functionalities boil down to {@link #dig(Block, BlockMenu, Block)} and
 * {@link #moveAndDig(Block, BlockMenu, BlockFace, Block)}.
 * Otherwise the functionality is similar to a regular android.
 * <p>
 * The {@link MinerAndroid} will also fire an {@link AndroidMineEvent} when breaking a {@link Block}.
 *
 * @author TheBusyBiscuit
 * @author creator3
 * @author poma123
 * @author Sfiguz7
 * @author CyberPatriot
 * @author Redemption198
 * @author Poslovitch
 *
 * @see AndroidMineEvent
 *
 */
public class MinerAndroid extends ProgrammableAndroid {

    // Determines the drops a miner android will get
    private final ItemStack effectivePickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

    private final ItemSetting<Boolean> firesEvent = new ItemSetting<>(this, "trigger-event-for-generators", false);
    private final ItemSetting<Boolean> applyOptimizations = new ItemSetting<>(this, "reduced-block-updates", true);

    @ParametersAreNonnullByDefault
    public MinerAndroid(
            ItemGroup itemGroup, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, tier, item, recipeType, recipe);

        addItemSetting(firesEvent, applyOptimizations);
    }

    @Override
    @Nonnull
    public AndroidType getAndroidType() {
        return AndroidType.MINER;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void dig(Block b, BlockMenu menu, Block block) {
        Collection<ItemStack> drops = block.getDrops(effectivePickaxe);

        if (!SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(block.getType()) && !drops.isEmpty()) {
            OfflinePlayer owner =
                    Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));

            if (Slimefun.getProtectionManager().hasPermission(owner, block.getLocation(), Interaction.BREAK_BLOCK)) {
                AndroidMineEvent event = new AndroidMineEvent(block, new AndroidInstance(this, b));
                Bukkit.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                // We only want to break non-Slimefun blocks
                if (!StorageCacheUtils.hasBlock(block.getLocation())) {
                    breakBlock(menu, drops, block);
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void moveAndDig(Block b, BlockMenu menu, BlockFace face, Block block) {
        Collection<ItemStack> drops = block.getDrops(effectivePickaxe);

        if (!SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(block.getType()) && !drops.isEmpty()) {
            OfflinePlayer owner =
                    Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));

            if (Slimefun.getProtectionManager().hasPermission(owner, block.getLocation(), Interaction.BREAK_BLOCK)) {
                AndroidMineEvent event = new AndroidMineEvent(block, new AndroidInstance(this, b));
                Bukkit.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                // We only want to break non-Slimefun blocks
                if (!StorageCacheUtils.hasBlock(block.getLocation())) {
                    breakBlock(menu, drops, block);
                    move(b, face, block);
                }
            } else {
                move(b, face, block);
            }
        } else {
            move(b, face, block);
        }
    }

    @ParametersAreNonnullByDefault
    private void breakBlock(BlockMenu menu, Collection<ItemStack> drops, Block block) {

        if (!block.getWorld().getWorldBorder().isInside(block.getLocation())) {
            return;
        }

        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());

        // Push our drops to the inventory
        for (ItemStack drop : drops) {
            menu.pushItem(drop, getOutputSlots());

            if (block instanceof Container container) {
                for (ItemStack content : container.getInventory().getContents()) {
                    block.getWorld().dropItemNaturally(block.getLocation(), content);
                }
            }
        }

        // Check if Block Generator optimizations should be applied.
        if (applyOptimizations.getValue()) {
            InfiniteBlockGenerator generator = InfiniteBlockGenerator.findAt(block);

            // If we found a generator, continue.
            if (generator != null) {
                if (firesEvent.getValue()) {
                    generator.callEvent(block);
                }

                // "poof" a "new" block was generated
                SoundEffect.MINER_ANDROID_BLOCK_GENERATION_SOUND.playAt(block);
                block.getWorld()
                        .spawnParticle(
                                VersionedParticle.SMOKE,
                                block.getX() + 0.5,
                                block.getY() + 1.25,
                                block.getZ() + 0.5,
                                8,
                                0.5,
                                0.5,
                                0.5,
                                0.015);
            } else {
                block.setType(Material.AIR);
            }
        } else {
            block.setType(Material.AIR);
        }
    }
}
