package me.mrCookieSlime.CraftCulture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

public class Landscape {
	
	public static Map<Material, List<Location>> map = new HashMap<Material, List<Location>>();
	
	public static void mapOut(Location start, int radius) {
		for (Location l: LocationCalculator.listNearbyLocations(start, radius)) {
			List<Location> list = new ArrayList<Location>();
			if (map.containsKey(l.getBlock().getType())) {
				list = map.get(l.getBlock().getType());
			}
			list.add(l);
			map.put(l.getBlock().getType(), list);
		}
	}
	
	public static Map<Material, List<Location>> getMap() {
		return map;
	}

}
