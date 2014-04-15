package me.mrCookieSlime.CraftCulture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

public class BotAI {
	
	public static main plugin;
	
	public BotAI(main instance) {
		plugin = instance;
	}
	
	static Map<Villager, List<Location>> moves = new HashMap<Villager, List<Location>>();
	
	public static void walkTo(Villager v, Location l) {
		
		List<Location> locations = new ArrayList<Location>();
		
		Location start = v.getLocation();
		Location finish = l;
		
		moves.put(v, locations);
	}
	
	public static void chat(Villager v, String message) {
		String msg = plugin.getConfig().getString("messages.chat");
		msg = msg.replace("%message%", message);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	public static void askFor(Villager v, Material m, int amount) {
		String msg = plugin.getConfig().getString("messages.ask-item");
		msg = msg.replace("%amount%", String.valueOf(amount));
		msg = msg.replace("%item%", m.toString().toLowerCase().replace("_", " ") + "/s");
		chat(v, msg);
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
	
	public static boolean hasMovingTask(Villager v) {
		return moves.containsKey(v);
	}
	
	public static List<Location> getNextPositions(Villager v) {
		return moves.get(v);
	}
	
	public static Location getNextPositionToWalk(Villager v) {
		List<Location> locs = getNextPositions(v);
		Location next = locs.get(0).clone();
		locs.remove(0);
		
		return next;
	}
	
	public static void getAvailableItems(Villager v, List<Material> types, Map<Material, Integer> inv) {
		for (Block b: Villagers.getChests(v)) {
			
		}
	}

}