package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class CargoInputNode extends AbstractFilterNode {

    private static final int[] BORDER = { 0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 22, 23, 26, 27, 31, 32, 33, 34, 35, 36, 40, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private static final String ROUND_ROBIN_MODE = "round-robin";
    private static final String SMART_FILL_MODE = "smart-fill";

    @ParametersAreNonnullByDefault
    public CargoInputNode(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @Override
    protected int[] getBorder() {
        return BORDER;
    }

    @Override
    protected void onPlace(BlockPlaceEvent e) {
        super.onPlace(e);

        BlockStorage.addBlockInfo(e.getBlock(), ROUND_ROBIN_MODE, String.valueOf(false));
        BlockStorage.addBlockInfo(e.getBlock(), SMART_FILL_MODE, String.valueOf(false));
    }

    @Override
    protected void updateBlockMenu(BlockMenu menu, Block b) {
        super.updateBlockMenu(menu, b);

        String roundRobinMode = BlockStorage.getLocationInfo(b.getLocation(), ROUND_ROBIN_MODE);
        if (!BlockStorage.hasBlockInfo(b) || roundRobinMode == null || roundRobinMode.equals(String.valueOf(false))) {
            menu.replaceExistingItem(24, new CustomItem(HeadTexture.ENERGY_REGULATOR.getAsItemStack(), "&7平均分配模式: &4\u2718", "", "&e> 点击启用平均分配模式", "&e(物品在该信道中平均分配)"));
            menu.addMenuClickHandler(24, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, ROUND_ROBIN_MODE, String.valueOf(true));
                updateBlockMenu(menu, b);
                return false;
            });
        } else {
            menu.replaceExistingItem(24, new CustomItem(HeadTexture.ENERGY_REGULATOR.getAsItemStack(), "&7平均分配模式: &2\u2714", "", "&e> 点击关闭平均分配模式", "&e(物品在该信道中不会平均分配)"));
            menu.addMenuClickHandler(24, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, ROUND_ROBIN_MODE, String.valueOf(false));
                updateBlockMenu(menu, b);
                return false;
            });
        }

        String smartFillNode = BlockStorage.getLocationInfo(b.getLocation(), SMART_FILL_MODE);
        if (!BlockStorage.hasBlockInfo(b) || smartFillNode == null || smartFillNode.equals(String.valueOf(false))) {
            menu.replaceExistingItem(16, new CustomItem(Material.WRITABLE_BOOK, "&7\"智能填充模式\" Mode: &4\u2718", "", "&e> 点击开启 \"智能填充\"模式", "", "&f在此模式下,货运节点将尝试", "&f使箱子里面的物品数量保持恒定"));
            menu.addMenuClickHandler(16, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, SMART_FILL_MODE, String.valueOf(true));
                updateBlockMenu(menu, b);
                return false;
            });
        } else {
            menu.replaceExistingItem(16, new CustomItem(Material.WRITTEN_BOOK, "&7\"智能填充模式\" Mode: &2\u2714", "", "&e> 点击关闭 \"智能填充\" 模式", "", "&f使箱子里面的物品数量保持恒定"));
            menu.addMenuClickHandler(16, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, SMART_FILL_MODE, String.valueOf(false));
                updateBlockMenu(menu, b);
                return false;
            });
        }
    }

}
