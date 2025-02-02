package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import city.norain.slimefun4.api.menu.UniversalMenu;

@FunctionalInterface
interface AndroidAction {

    void perform(ProgrammableAndroid android, Block b, UniversalMenu inventory, BlockFace face);
}
