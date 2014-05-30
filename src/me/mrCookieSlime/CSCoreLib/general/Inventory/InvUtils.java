package me.mrCookieSlime.CSCoreLib.general.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvUtils {
	
	public static boolean fits(Inventory inv, ItemStack item) {
		Inventory inv2 = Bukkit.createInventory(null, inv.getSize());
		for (int i = 0; i < inv.getContents().length; i++) {
			inv2.setItem(i, inv.getContents()[i]);
		}
		return inv2.addItem(item).isEmpty();
	}

}
