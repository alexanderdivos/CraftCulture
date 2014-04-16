package me.mrCookieSlime.CraftCulture;

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

public class Villagers {
	
	static List<Villager> active = new ArrayList<Villager>();
	static Map<Villager, Integer> ids = new HashMap<Villager, Integer>();
	static Map<Villager, List<Block>> inventory = new HashMap<Villager, List<Block>>();
	static Map<Villager, Location> homepoints = new HashMap<Villager, Location>();
	static Map<Villager, String> names = new HashMap<Villager, String>();
	static Map<Villager, Location> locations = new HashMap<Villager, Location>();
	static Map<Villager, List<UUID>> angry = new HashMap<Villager, List<UUID>>();
	static Map<Villager, Map<Material, Integer>> resources = new HashMap<Villager, Map<Material,Integer>>();
	static List<Material> resourceIndex = new ArrayList<Material>();
	static Map<Villager, Map<EntityType, Integer>> drops = new HashMap<Villager, Map<EntityType,Integer>>();
	static List<EntityType> dropIndex = new ArrayList<EntityType>();
	
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
		
		return v;
	}

}
