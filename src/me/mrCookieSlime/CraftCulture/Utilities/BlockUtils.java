package me.mrCookieSlime.CraftCulture.Utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;

public class BlockUtils {
	
	public static List<Chest> castToChest(List<Block> list) {
		List<Chest> chests = new ArrayList<Chest>();
		for (Block b: list) {
			chests.add((Chest)b.getState());
		}
		return chests;
	}

}
