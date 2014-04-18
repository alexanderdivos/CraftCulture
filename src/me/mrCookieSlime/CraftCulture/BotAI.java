package me.mrCookieSlime.CraftCulture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.mrCookieSlime.CraftCulture.Utilities.BlockUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.pathing.AStar;
import com.adamki11s.pathing.AStar.InvalidPathException;
import com.adamki11s.pathing.PathingResult;
import com.adamki11s.pathing.Tile;

public class BotAI {
	
	public static main plugin;
	
	public BotAI(main instance) {
		plugin = instance;
	}
	
	static Map<Villager, List<Location>> moves = new HashMap<Villager, List<Location>>();
	
	public static void setLocation(Villager v, Location l) {
		Villagers.locations.put(v, l);
	}
	
	public static void getCloseTo(Villager v, Location l, int radius) {
		for (int x = -(radius); x <= radius; x++) {
			for (int z = -(radius); z <= radius; z++) {
				for (int y = 0; y < l.getWorld().getMaxHeight(); y++) {
					Block current = l.getBlock().getRelative(x, y, z);
					if (current.getType().isSolid() 
						&& l.getBlock().getRelative(x, y + 1, z).getType() == Material.AIR
						&& l.getBlock().getRelative(x, y + 2, z).getType() == Material.AIR) {
						if (current.getRelative(BlockFace.UP).getLocation().distanceSquared(l) <= radius) {
							
							walkTo(v, current.getRelative(BlockFace.UP).getLocation());
							
							return;
						}
					}
				}
			}
		}
	}
	
	public static void addResourceGoal(Villager v, Material m, int amount) {
		Map<Material, Integer> resources = getResourceTask(v);
		
		if (resources.containsKey(v)) {
			amount = amount + resources.get(v);
		}
		
		resources.put(m, amount);
		
		Villagers.resources.put(v, resources);
		
		if (!Villagers.resourceIndex.contains(m)) {
			Villagers.resourceIndex.add(m);
		}
	}
	
	public static void goHome(Villager v) {
		walkTo(v, Villagers.getHomePoint(v));
	}
	
	public static void walkTo(Villager v, Location l) {
		List<Location> locations = new ArrayList<Location>();
		
		Location start = v.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
		Location finish = l.getBlock().getRelative(BlockFace.DOWN).getLocation();
		
		AStar path = null;
		try {
			path = new AStar(start, finish, plugin.getConfig().getInt("bots.work-area") * 2);
		} catch (InvalidPathException e) {
			System.out.println("Path Generation failed for Villager \"" + v.getCustomName() + "\"");
			System.out.println("InvalidPathException: " + e.getMessage());
			System.out.println(e.getCause());
			
			if(e.isEndNotSolid()){
	            System.out.println("End block is not walkable");
	        }
	        if(e.isStartNotSolid()){
	            System.out.println("Start block is not walkable");
	        }
		}
		if (path != null) {
			 ArrayList<Tile> route = path.iterate();
		        PathingResult result = path.getPathingResult();
		        
		        if (result == PathingResult.SUCCESS)  {
		        	for (Tile tile: route) {
		        		locations.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        		locations.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        		locations.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        		locations.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        		locations.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        		locations.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        		locations.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        		locations.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        	}
		        }
		        else {
		        	System.out.println("Path Generation failed for Villager \"" + v.getCustomName() + "\"");
		        }
		}
		else {
			System.out.println("Path Generation failed for Villager \"" + v.getCustomName() + "\"");
		}
		
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
		if (b != null) {
			if (v.getLocation().distanceSquared(b.getLocation()) <= 4) {
				if (plugin.getConfig().getBoolean("bots.log-activity")) {
					System.out.println(Villagers.getName(v) + " just broke 1 " + b.getType().toString());
				}
				b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
				b.setType(Material.AIR);
				for (ItemStack item: b.getDrops()) {
					depositItem(v, getAvailableChest(v), item);
				}
			}
			else {
				if (!hasMovingTask(v)) {
					getCloseTo(v, b.getLocation(), 4);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void placeBlock(Villager v, Material block, byte data, Location l) {
		if (plugin.getConfig().getBoolean("bots.log-activity")) {
			System.out.println(Villagers.getName(v) + " just placed 1 " + block.toString());
		}
		if (v.getLocation().distanceSquared(l) <= 4) {
			l.getBlock().setType(block);
			l.getBlock().setData(data);
			l.getWorld().playEffect(l, Effect.STEP_SOUND, block);
		}
		else {
			getCloseTo(v, l, 4);
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
			getCloseTo(v, chest.getLocation(), 4);
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
		
		Location next = null;
		if (locs.size() == 0) {
			moves.remove(v);
		}
		else {
			next = locs.get(0).clone();
			locs.remove(0);
			moves.put(v, locs);
		}
		
		if (next != null) {
			setLocation(v, next);
		}
		
		return next;
	}
	
	public static void getAvailableItems(Villager v, List<Material> types, Map<Material, Integer> inv) {
		for (Chest c: BlockUtils.castToChest(Villagers.getChests(v))) {
			for (ItemStack item: c.getInventory().getContents()) {
				if (item != null) {
					if (types.contains(item.getType())) {
						types.add(item.getType());
						inv.put(item.getType(), item.getAmount());
					}
					else {
						inv.put(item.getType(), item.getAmount() + inv.get(item.getType()));
					}
				}
			}
		}
	}
	
	public static Location getCurrentLocation(Villager v) {
		return Villagers.locations.get(v);
	}
	
	public static List<UUID> getAngryPlayers(Villager v) {
		return Villagers.angry.get(v);
	}
	
	public static Map<Material, Integer> getResourceTask(Villager v) {
		return Villagers.resources.get(v);
	}
	
	public static Material getNextResourceGoal(Villager v) {
		if (Villagers.resourceIndex.size() > 0) {
			return Villagers.resourceIndex.get(0);
		}
		else {
			return null;
		}
	}
	
	public static int getResourceAmount(Villager v, Material m) {
		if (Villagers.resourceIndex.size() > 0) {
			return Villagers.resources.get(v).get(m);
		}
		else {
			return 0;
		}
	}
	
	public static Map<EntityType, Integer> getDropTask(Villager v) {
		return Villagers.drops.get(v);
	}
	
	public static void getNextDropGoal(Villager v, EntityType m, Integer amount) {
		m = Villagers.dropIndex.get(0);
		amount = getResourceTask(v).get(m);
	}
	
	public static boolean isAngryOn(Villager v, LivingEntity n) {
		if (n instanceof Player) {
			return getAngryPlayers(v).contains(n.getUniqueId());
		}
		else if (n instanceof Monster){
			return true;
		}
		else if (n instanceof Animals) {
			EntityType goal = null;
			getNextDropGoal(v, goal, null);
			if (goal == n.getType()) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static Block findClosestMaterial(Villager v, Material type) {
		int radius = plugin.getConfig().getInt("bots.work-area");
		Location center = Villagers.getHomePoint(v);
		
		Block closest = null;
		
		for (int x = -(radius); x <= radius; x++) {
			for (int y = -(radius); y <= radius; y++) {
				for (int z = -(radius); z <= radius; z++) {
					if (center.getBlock().getRelative(x, y, z).getType() == type) {
						if (closest == null) {
							closest = center.getBlock().getRelative(x, y, z);
						}
						else if (center.getBlock().getRelative(x, y, z).getLocation().distanceSquared(center) < closest.getLocation().distanceSquared(center)) {
							closest = center.getBlock().getRelative(x, y, z);
						}
					}
				}
			}
		}
		
		return closest;
	}

}