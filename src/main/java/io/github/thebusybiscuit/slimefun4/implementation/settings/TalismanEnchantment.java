package io.github.thebusybiscuit.slimefun4.implementation.settings;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.enchantments.Enchantment;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.MagicianTalisman;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TalismanListener;

/**
 * This class is an extension of {@link ItemSetting} that holds an {@link Enchantment} and
 * a level. It is only used by the {@link TalismanListener} to handle the {@link MagicianTalisman}.
 *
 * @author TheBusyBiscuit
 *
 * @see MagicianTalisman
 */
public class TalismanEnchantment extends ItemSetting<Boolean> {

    private final Enchantment enchantment;
    private final int level;

    @ParametersAreNonnullByDefault
    public TalismanEnchantment(MagicianTalisman talisman, Enchantment enchantment, int level) {
        super(
                talisman,
                "allow-enchantments."
                        + enchantment.getKey().getNamespace()
                        + '.'
                        + enchantment.getKey().getKey()
                        + ".level."
                        + level,
                true);

        this.enchantment = enchantment;
        this.level = level;
    }

    /**
     * This returns the actual {@link Enchantment} represented by this {@link ItemSetting}.
     *
     * @return The associated {@link Enchantment}
     */
    @Nonnull
    public Enchantment getEnchantment() {
        return enchantment;
    }

    /**
     * This returns the level for this {@link Enchantment}.
     *
     * @return The level
     */
    public int getLevel() {
        return level;
    }
}
