package me.mrCookieSlime.CraftCulture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public class Villagers {
	
	static List<Villager> active = new ArrayList<Villager>();
	static Map<Villager, Integer> ids = new HashMap<Villager, Integer>();
	static Map<Villager, List<Block>> inventory = new HashMap<Villager, List<Block>>();
	static Map<Villager, Location> homepoints = new HashMap<Villager, Location>();
	static Map<Villager, String> names = new HashMap<Villager, String>();
	
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
		
		return v;
	}

}
