package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.CoolantCell;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * The {@link ReactorAccessPort} is a block which acts as an interface
 * between a {@link Reactor} and a {@link CargoNet}.
 * Any item placed into the port will get transferred to the {@link Reactor}.
 * 
 * @author TheBusyBiscuit
 * @author AlexLander123
 *
 */
public class ReactorAccessPort extends SlimefunItem {

    private static final int INFO_SLOT = 49;

    private final int[] background = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 12, 13, 14, 21, 23 };
    private final int[] fuelBorder = { 9, 10, 11, 18, 20, 27, 29, 36, 38, 45, 46, 47 };
    private final int[] inputBorder = { 15, 16, 17, 24, 26, 33, 35, 42, 44, 51, 52, 53 };
    private final int[] outputBorder = { 30, 31, 32, 39, 41, 48, 50 };

    @ParametersAreNonnullByDefault
    public ReactorAccessPort(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(onBreak());

        new BlockMenuPreset(getId(), "&2反应堆访问接口") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK);
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
                BlockMenu reactor = getReactor(b.getLocation());

                if (reactor != null) {
                    menu.replaceExistingItem(INFO_SLOT, new CustomItem(Material.GREEN_WOOL, "&7反应堆", "", "&6已连接", "", "&7> 点击浏览反应堆"));
                    menu.addMenuClickHandler(INFO_SLOT, (p, slot, item, action) -> {
                        if (reactor != null) {
                            reactor.open(p);
                        }

                        newInstance(menu, b);

                        return false;
                    });
                } else {
                    menu.replaceExistingItem(INFO_SLOT, new CustomItem(Material.RED_WOOL, "&7反应堆", "", "&c未找到", "", "&7反应堆必须在", "&7反应堆访问接口", "&7下方第三格处"));
                    menu.addMenuClickHandler(INFO_SLOT, (p, slot, item, action) -> {
                        newInstance(menu, b);
                        return false;
                    });
                }
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return getInputSlots();
                } else {
                    return getOutputSlots();
                }
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.INSERT) {
                    if (SlimefunItem.getByItem(item) instanceof CoolantCell) {
                        return getCoolantSlots();
                    } else {
                        return getFuelSlots();
                    }
                } else {
                    return getOutputSlots();
                }
            }
        };
    }

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                BlockMenu inv = BlockStorage.getInventory(b);

                if (inv != null) {
                    inv.dropItems(b.getLocation(), getFuelSlots());
                    inv.dropItems(b.getLocation(), getCoolantSlots());
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }
            }
        };
    }

    private void constructMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(ChestMenuUtils.getBackground(), background);

        preset.drawBackground(new CustomItem(Material.LIME_STAINED_GLASS_PANE, " "), fuelBorder);
        preset.drawBackground(new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), inputBorder);
        preset.drawBackground(new CustomItem(Material.GREEN_STAINED_GLASS_PANE, " "), outputBorder);

        preset.addItem(1, new CustomItem(SlimefunItems.URANIUM, "&7燃料槽", "", "&r这个槽位放放射性燃料", "&r如:&2铀&r或&a镎"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(22, new CustomItem(SlimefunItems.PLUTONIUM, "&7产品槽", "", "&r这个槽位包含反应堆产品", "&r如&a镎&r或&7钚"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(7, new CustomItem(SlimefunItems.REACTOR_COOLANT_CELL, "&b冷却剂槽", "", "&r这个槽位放冷却剂", "&4如果没有冷却剂, 你的反应堆", "&4将会爆炸"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Nonnull
    public int[] getInputSlots() {
        return new int[] { 19, 28, 37, 25, 34, 43 };
    }

    @Nonnull
    public int[] getFuelSlots() {
        return new int[] { 19, 28, 37 };
    }

    @Nonnull
    public int[] getCoolantSlots() {
        return new int[] { 25, 34, 43 };
    }

    @Nonnull
    public static int[] getOutputSlots() {
        return new int[] { 40 };
    }

    @Nullable
    private BlockMenu getReactor(@Nonnull Location l) {
        Location location = new Location(l.getWorld(), l.getX(), l.getY() - 3, l.getZ());
        SlimefunItem item = BlockStorage.check(location.getBlock());

        if (item instanceof Reactor) {
            return BlockStorage.getInventory(location);
        }

        return null;
    }

}
