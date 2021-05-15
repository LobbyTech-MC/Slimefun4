package io.github.thebusybiscuit.slimefun4.implementation.items.altar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.AncientAltarCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.AncientAltarTask;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link AncientAltar} is a multiblock structure.
 * The altar itself stands in the center, surrounded by {@link AncientPedestal Pedestals}, it is used
 * to craft various magical items.
 * 
 * @author TheBusyBiscuit
 * 
 * @see AncientAltarListener
 * @see AncientAltarTask
 * @see AncientAltarCraftEvent
 * @see AncientPedestal
 *
 */
public class AncientAltar extends SlimefunItem {

    private static final int DEFAULT_STEP_DELAY = 8;

    private final List<AltarRecipe> recipes = new ArrayList<>();

    private final ItemSetting<Integer> stepDelay = new IntRangeSetting(this, "step-delay", 0, DEFAULT_STEP_DELAY, Integer.MAX_VALUE);

    public AncientAltar(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(stepDelay);
    }

    /**
     * This returns the speed of this {@link AncientAltar}.
     * This number determines how many ticks happen in between a step in the ritual animation.
     * The default is {@value #DEFAULT_STEP_DELAY} ticks.
     * 
     * @return The speed of this {@link AncientAltar}
     */
    public int getSpeed() {
        return stepDelay.getValue();
    }

    public List<AltarRecipe> getRecipes() {
        return recipes;
    }

}
