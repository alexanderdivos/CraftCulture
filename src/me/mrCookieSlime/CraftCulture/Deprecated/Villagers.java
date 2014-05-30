package me.mrCookieSlime.CraftCulture.Deprecated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

@Deprecated
public class Villagers {
	
	static List<Villager> active = new ArrayList<Villager>();
	static Map<Villager, Integer> ids = new HashMap<Villager, Integer>();
	static Map<Villager, List<Block>> inventory = new HashMap<Villager, List<Block>>();
	static Map<Villager, Location> homepoints = new HashMap<Villager, Location>();
	static Map<Villager, String> names = new HashMap<Villager, String>();
	static Map<Villager, Location> locations = new HashMap<Villager, Location>();
	static Map<Villager, List<UUID>> angry = new HashMap<Villager, List<UUID>>();
	static Map<Villager, Map<Material, Integer>> resources = new HashMap<Villager, Map<Material,Integer>>();
	static Map<Villager, List<Material>> resourceIndex = new HashMap<Villager, List<Material>>();
	static Map<Villager, Map<EntityType, Integer>> drops = new HashMap<Villager, Map<EntityType,Integer>>();
	static Map<Villager, List<EntityType>> dropIndex = new HashMap<Villager, List<EntityType>>();
	static Map<Villager, ItemStack> item = new HashMap<Villager, ItemStack>();
	static Map<Villager, Map<Location, Material>> mapping = new HashMap<Villager, Map<Location,Material>>();
	static Map<Material, List<Location>> map = new HashMap<Material, List<Location>>();
	static Map<Villager, List<Location>> mapped = new HashMap<Villager, List<Location>>();
	static Map<Villager, List<Material>> foundmats = new HashMap<Villager, List<Material>>();
	
	public static List<Villager> getActiveVillagers() {
		return active;
	}
	
	public static int getId(Villager v) {
		return ids.get(v);
	}
	
	public static List<Block> getChests(Villager v) {
		return inventory.get(v);
	}
	
	public static Location getHomePoint(Villager v) {
		return homepoints.get(v);
	}
	
	public static String getName(Villager v) {
		return names.get(v);
	}
	
	public static Villager spawn(String name, Location homepoint) {
		Villager v = (Villager) homepoint.getWorld().spawnEntity(homepoint, EntityType.VILLAGER);
		v.setCustomNameVisible(true);
		v.setCustomName(name);
		
		active.add(v);
		homepoints.put(v, homepoint);
		names.put(v, name);
		locations.put(v, homepoint);
		angry.put(v, new ArrayList<UUID>());
		resources.put(v, new HashMap<Material, Integer>());
		drops.put(v, new HashMap<EntityType, Integer>());
		inventory.put(v, new ArrayList<Block>());
		item.put(v, null);
		
		return v;
	}

}
