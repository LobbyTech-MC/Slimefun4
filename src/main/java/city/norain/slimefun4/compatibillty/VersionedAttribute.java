package city.norain.slimefun4.compatibillty;

import org.bukkit.attribute.Attribute;

import city.norain.slimefun4.SlimefunExtended;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VersionedAttribute {
    public static Attribute getMaxHealth() {
        if (SlimefunExtended.getMinecraftVersion().isAtLeast(1, 21, 3)) {
            return Attribute.valueOf("MAX_HEALTH");
        } else {
            return Attribute.valueOf("GENERIC_MAX_HEALTH");
        }
    }
}
