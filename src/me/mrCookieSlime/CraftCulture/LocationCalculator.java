package me.mrCookieSlime.CraftCulture;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLib.general.Block.BlockAdjacents;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class LocationCalculator {
	
	public static List<Location> listNearbyLocations(Location l, int radius) {
		List<Location> list = new ArrayList<Location>();
		for (int x = -(radius); x <= radius; x++) {
			for (int z = -(radius); z <= radius; z++) {
				for (int y = -(radius); y <= radius; y++) {
					list.add(l.getBlock().getRelative(x, y, z).getLocation());
				}
			}
		}
		return list;
	}
	
	public static Location findCloseLocationTo(Location l, int radius) {
		Location place = null;
		
		for (int x = -(radius); x <= radius; x++) {
			for (int z = -(radius); z <= radius; z++) {
				for (int y = -(radius); y <= radius; y++) {
					Block current = l.getBlock().getRelative(x, y, z);
					if (current.getType().isSolid() && !current.getRelative(BlockFace.UP).getType().isSolid() && !current.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType().isSolid() && !(BlockAdjacents.hasAdjacentMaterial(current.getRelative(BlockFace.UP), Material.CHEST))) {
						if (current.getRelative(BlockFace.UP).getLocation().distanceSquared(l) <= radius) {
							place = current.getRelative(BlockFace.UP).getLocation();
							break;
						}
					}
				}
			}
		}
		return place;
	}
	
	public static Location getClosest(Location start, List<Location> locs) {
		Location closest = null;
		
		for (Location l: locs) {
			if (closest == null) {
				closest = l;
			}
			else if (closest.distanceSquared(start) < l.distanceSquared(start)) {
				closest = l;
			}
		}
		
		return closest;
	}

}
