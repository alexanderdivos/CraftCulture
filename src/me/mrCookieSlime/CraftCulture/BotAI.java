package me.mrCookieSlime.CraftCulture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

public class BotAI {
	
	static Map<Villager, List<Location>> moves = new HashMap<Villager, List<Location>>();
	
	public static void walkTo(Villager v, Location l) {
		Location start = v.getLocation();
		Location finish = l;
	}
	
	public static void breakBlock(Villager v, Block b) {
		if (v.getLocation().distanceSquared(b.getLocation()) <= 4) {
			b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
			b.setType(Material.AIR);
			for (ItemStack item: b.getDrops()) {
				depositItem(v, getAvailableChest(v), item);
			}
		}
		else {
			walkTo(v, b.getLocation());
			breakBlock(v, b);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void placeBlock(Villager v, Material block, byte data, Location l) {
		if (v.getLocation().distanceSquared(l) <= 4) {
			l.getBlock().setType(block);
			l.getBlock().setData(data);
			l.getWorld().playEffect(l, Effect.STEP_SOUND, block);
		}
		else {
			walkTo(v, l);
			placeBlock(v, block, data, l);
		}
	}
	
	public static Chest getAvailableChest(Villager v) {
		Chest chest = null;
		
		for (Block b: Villagers.getChests(v)) {
			if (b.getType() == Material.CHEST) {
				Chest c = (Chest) b.getState();
				if (c.getInventory().getContents().length < c.getInventory().getSize()) {
					chest = c;
					break;
				}
			}
		}
		
		return chest;
	}
	
	public static void depositItem(Villager v, Chest chest, ItemStack item) {
		if (v.getLocation().distanceSquared(chest.getLocation()) <= 4) {
			chest.getWorld().playSound(chest.getLocation(), Sound.CHEST_OPEN, 1, 1);
			
			chest.getInventory().addItem(item);
			
			chest.getWorld().playSound(chest.getLocation(), Sound.CHEST_CLOSE, 1, 1);
		}
		else {
			walkTo(v, chest.getLocation());
			depositItem(v, chest, item);
		}
	}

}