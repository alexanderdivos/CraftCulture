package me.mrCookieSlime.CraftCulture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Villager;

public class Villagers {
	
	static List<Villager> active = new ArrayList<Villager>();
	static Map<Villager, Integer> ids = new HashMap<Villager, Integer>();
	static Map<Villager, List<Block>> inventory = new HashMap<Villager, List<Block>>();
	static Map<Villager, Location> homepoint = new HashMap<Villager, Location>();
	
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
		return homepoint.get(v);
	}

}
