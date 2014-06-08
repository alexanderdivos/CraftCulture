package me.mrCookieSlime.CraftCulture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.mrCookieSlime.CSCoreLib.Configuration.Config;
import me.mrCookieSlime.CSCoreLib.general.Block.BlockBreaker;
import me.mrCookieSlime.CSCoreLib.general.Inventory.InvUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.pathing.AStar;
import com.adamki11s.pathing.AStar.InvalidPathException;
import com.adamki11s.pathing.PathingResult;
import com.adamki11s.pathing.Tile;

public class Bot {
	
	String name;
	LivingEntity bot;
	Location l;
	List<Location> moves;
	Location origin;
	List<Chest> chests;
	Inventory inventory;
	List<Goal> goals;
	Map<UUID, Integer> playerReputation;
	List<UUID> blacklist;
	
	List<ItemStack> supply;
	
	public Bot(String name) {
		this.name = name;
		this.moves = new ArrayList<Location>();
		this.chests = new ArrayList<Chest>();
		this.goals = new ArrayList<Goal>();
		this.playerReputation = new HashMap<UUID, Integer>();
		this.blacklist = new ArrayList<UUID>();
		this.supply = new ArrayList<ItemStack>();
		this.inventory = Bukkit.createInventory(null, 54);
	}
	
	public String getName() {
		return this.name;
	}
	
	public LivingEntity getEntity() {
		return this.bot;
	}
	
	public Location getCurrentLocation() {
		return this.l;
	}
	
	public Location getHomePoint() {
		return this.origin;
	}
	
	public void spawn(Location l, Block chest) {
		this.l = l;
		LivingEntity entity = (LivingEntity) l.getWorld().spawnEntity(l, EntityType.VILLAGER);
		entity.setMaxHealth(100d);
		entity.setHealth(100d);
		entity.setCustomNameVisible(true);
		entity.setCustomName(this.name);
		this.bot = entity;
		this.origin = l;
		this.chests.add((Chest) chest.getState());
		for (String mission: new Config(new File("plugins/CraftCulture/config.yml")).getStringList("bots.goals")) {
			this.goals.add(new Goal(Material.getMaterial(mission.split(";")[1]), Integer.parseInt(mission.split(";")[0])));
		}
		Landscape.mapOut(l, 40);
		CraftCulture.bots.add(this);
	}
	
	public void teleport(Location l) {
		this.l = l;
		this.bot.teleport(l);
	}
	
	public void stay() {
		this.bot.teleport(this.l);
	}
	
	public boolean hasMovingTask() {
		return moves.size() > 0;
	}
	
	public Location getNextDestination() {
		Location next = null;
		if (this.moves.size() > 0) {
			next = this.moves.get(0).clone();
			this.moves.remove(0);
		}
		
		if (next != null) {
			this.l = next;
		}
		
		return next;
	}
	
	public void walkTo(Location l) {
		
		Location start = this.bot.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
		Location finish = l.getBlock().getRelative(BlockFace.DOWN).getLocation();
		
		AStar path = null;
		try {
			path = new AStar(start, finish, new Config(new File("plugins/CraftCulture/config.yml")).getInt("bots.work-area") * 2);
		} catch (InvalidPathException e) {
			System.out.println("Path Generation failed for Bot \"" + this.bot.getCustomName() + "\"");
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
		        		for (int i = 0; i < 8; i++) {
		        			moves.add(tile.getLocation(start).getBlock().getRelative(BlockFace.UP).getLocation());
		        		}
		        	}
		        }
		        else {
		        	System.out.println("Path Generation failed for Bot \"" + this.bot.getCustomName() + "\"");
		        }
		}
		else {
			System.out.println("Path Generation failed for Bot \"" + this.bot.getCustomName() + "\"");
		}
	}
	
	public void chat(String message) {
		String format = new Config(new File("plugins/CraftCulture/config.yml")).getString("messages.chat");
		format = format.replace("%name%", getName());
		format = format.replace("%message%", message);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', format));
	}
	
	public void askFor(Material m, int amount) {
		String format = new Config(new File("plugins/CraftCulture/config.yml")).getString("messages.ask-item");
		format = format.replace("%amount%", String.valueOf(amount));
		String type = m.toString().toLowerCase().replace("_", " ");
		format = format.replace("%item%", Character.toUpperCase(type.charAt(0)) + type.substring(1) + "/s ");
		chat(format);
	}
	
	public void giveItem(ItemStack item) {
		if (InvUtils.fits(inventory, item)) {
			inventory.addItem(item);
			supply.add(item);
		}
	}
	
	public void depositItem(ItemStack item) {
		for (Chest c: chests) {
			if (InvUtils.fits(c.getInventory(), item)) {
				c.getInventory().addItem(item);
				inventory.remove(item);
			}
		}
	}
	
	public int getReputation(Player p) {
		if (playerReputation.containsKey(p.getUniqueId())) {
			return playerReputation.get(p.getUniqueId());
		}
		else {
			playerReputation.put(p.getUniqueId(), 0);
			return 0;
		}
	}
	
	public boolean hates(Player p) {
		return this.blacklist.contains(p.getUniqueId());
	}
	
	public List<Goal> listGoals() {
		return this.goals;
	}
	
	public List<ItemStack> getSupplies() {
		return this.supply;
	}
	
	public void breakBlock(Block b) {
		for (ItemStack drop: b.getDrops()) {
			giveItem(drop);
		}
		BlockBreaker.nullify(b);
	}
}
