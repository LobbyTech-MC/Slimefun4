package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.accelerators;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.OrganicFertilizer;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedParticle;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class CropGrowthAccelerator extends AbstractGrowthAccelerator {
    protected CropGrowthAccelerator(
            ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public abstract int getEnergyConsumption();

    public abstract int getRadius();

    public abstract int getSpeed();

    @Override
    public int getCapacity() {
        return 1024;
    }

    @Override
    protected void tick(Block b) {
        BlockMenu inv = StorageCacheUtils.getMenu(b.getLocation());

        if (getCharge(b.getLocation()) >= getEnergyConsumption()) {
            for (int x = -getRadius(); x <= getRadius(); x++) {
                for (int z = -getRadius(); z <= getRadius(); z++) {
                    Block block = b.getRelative(x, 0, z);

                    if (SlimefunTag.CROP_GROWTH_ACCELERATOR_BLOCKS.isTagged(block.getType()) && grow(b, inv, block)) {
                        return;
                    }
                }
            }
        }
    }

    private boolean grow(Block machine, BlockMenu inv, Block crop) {
        Ageable ageable = (Ageable) crop.getBlockData();

        if (ageable.getAge() < ageable.getMaximumAge()) {
            for (int slot : getInputSlots()) {
                var item = inv.getItemInSlot(slot);

                if (item == null || item.isEmpty()) {
                    continue;
                }

                var sfItem = SlimefunItem.getByItem(item);

                if (sfItem == null) {
                    continue;
                }

                if (sfItem instanceof OrganicFertilizer) {
                    removeCharge(machine.getLocation(), getEnergyConsumption());
                    inv.consumeItem(slot);

                    ageable.setAge(ageable.getAge() + 1);
                    crop.setBlockData(ageable);

                    crop.getWorld()
                            .spawnParticle(
                                    VersionedParticle.HAPPY_VILLAGER,
                                    crop.getLocation().add(0.5D, 0.5D, 0.5D),
                                    4,
                                    0.1F,
                                    0.1F,
                                    0.1F);
                    return true;
                }
            }
        }

        return false;
    }
}
