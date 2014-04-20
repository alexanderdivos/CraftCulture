package me.mrCookieSlime.CraftCulture.Utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockAdjacents {
	
	public static Block[] getAdjacentBlocks(Block b) {
		
		
		Location l = b.getLocation();
		
		Location l1 = b.getLocation();
		Location l2 = b.getLocation();
		Location l3 = b.getLocation();
		Location l4 = b.getLocation();
		Location l5 = b.getLocation();
		Location l6 = b.getLocation();
		
		l1.setY(l.getY() + 1.0D);
		l2.setY(l.getY() - 1.0D);
		l3.setZ(l.getZ() + 1.0D);
		l4.setZ(l.getZ() - 1.0D);
		l5.setX(l.getX() + 1.0D);
		l6.setX(l.getX() - 1.0D);
		
		Block[] Adjacents = {l1.getBlock(), l2.getBlock(), l3.getBlock(), l4.getBlock(), l5.getBlock(), l6.getBlock()};
		
		return Adjacents;
	}
	
	public static boolean hasAdjacentMaterial(Block b, Material m) {
		boolean bo = false;
		Block[] adjacents = getAdjacentBlocks(b);
		for (int i = 0; i < adjacents.length; i++) {
			if (adjacents[i].getType() == m) {
				bo = true;
			}
		}
		return bo;
	}
	public static boolean hasMaterialOnBothSides(Block b, Material m) {
		boolean bo = false;
		Block[] adjacents = getAdjacentBlocks(b);
		
		if (adjacents[2].getType() == m && adjacents[3].getType() == m) {
			bo = true;
		}
		else if (adjacents[4].getType() == m && adjacents[5].getType() == m) {
			bo = true;
		}
		return bo;
	}
	
	public static boolean hasMaterialOnAllSides(Block b, Material m) {
		boolean bo = false;
		Block[] adjacents = getAdjacentBlocks(b);
		
		if (adjacents[2].getType() == m && adjacents[3].getType() == m && adjacents[4].getType() == m && adjacents[5].getType() == m) {
			bo = true;
		}
		return bo;
	}
	
	public static boolean hasMaterialOnTop(Block b, Material m) {
		boolean bo = false;
		Block[] adjacents = getAdjacentBlocks(b);
		
		if (adjacents[0].getType() == m) {
			bo = true;
		}
		return bo;
	}

}
