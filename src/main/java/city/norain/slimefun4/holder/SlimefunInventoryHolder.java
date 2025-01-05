package city.norain.slimefun4.holder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SlimefunInventoryHolder implements InventoryHolder {
    @Setter
    protected Inventory inventory;

	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return inventory;
	}
}
