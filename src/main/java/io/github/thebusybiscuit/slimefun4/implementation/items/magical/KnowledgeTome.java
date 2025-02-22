package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * The {@link KnowledgeTome} allows you to copy every unlocked {@link Research}
 * from one {@link Player} to another.
 *
 * @author TheBusyBiscuit
 *
 */
public class KnowledgeTome extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public KnowledgeTome(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public @Nonnull ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();

            e.setUseBlock(Result.DENY);

            ItemMeta im = item.getItemMeta();
            List<String> lore = im.getLore();

            if (lore.get(1).isEmpty()) {
                lore.set(0, ChatColors.color("&7主人: &b" + p.getName()));
                lore.set(1, ChatColor.BLACK + "" + p.getUniqueId());
                im.setLore(lore);
                item.setItemMeta(im);
                SoundEffect.TOME_OF_KNOWLEDGE_USE_SOUND.playFor(p);
            } else {
                UUID uuid = UUID.fromString(
                        ChatColor.stripColor(item.getItemMeta().getLore().get(1)));

                if (p.getUniqueId().equals(uuid)) {
                    Slimefun.getLocalization().sendMessage(p, "messages.no-tome-yourself");
                    return;
                }

                PlayerProfile.get(
                        p,
                        profile -> PlayerProfile.fromUUID(uuid, owner -> {
                            for (Research research : owner.getResearches()) {
                                research.unlock(p, true);
                            }
                        }));

                if (p.getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(item, false);
                }
            }
        };
    }
}
