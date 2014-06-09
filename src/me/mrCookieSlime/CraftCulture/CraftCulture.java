package me.mrCookieSlime.CraftCulture;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLib.Configuration.ConfigSetup;
import me.mrCookieSlime.CSCoreLib.general.Server.Plugins;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CraftCulture extends JavaPlugin {
	
	static List<Bot> bots = new ArrayList<Bot>();
	
	@Override
	public void onEnable() {
		Plugins.load(this);
		ConfigSetup.setup(this);
		
		// Listeners:
		new BotListener(this);
		
		// Moving Timer
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Bot bot: bots) {
					if (!bot.hasMovingTask()) {
						bot.stay();
					}
					else {
						Location next = bot.getNextDestination();
						if (next != null) {
							int height = (int) (next.getY() - bot.getCurrentLocation().getY());
							if (height != 0) {
								bot.teleport(next);
							}
							else {
								Vector move = new Vector(next.getX() - bot.getCurrentLocation().getX(), height, next.getZ() - bot.getCurrentLocation().getZ());
								move.multiply(0.125);
								bot.getEntity().setVelocity(move);
							}
						}
					}
				}
				
			}
		}, 0L, 2L);
		
		// Gathering Timer
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
					
			@Override
			public void run() {
				for (Bot bot: bots) {
					for (Goal goal: bot.listGoals()) {
						int found = 0;
						for (ItemStack item: bot.getSupplies()) {
							if (item.getType() == goal.getType()) {
								found = found + item.getAmount();
							}
						}
						if (found < goal.getAmount()) {
							if (Landscape.getMap().containsKey(goal.getType())) {
								Location target = LocationCalculator.getClosest(bot.getCurrentLocation(), Landscape.getMap().get(goal.getType()));
								bot.walkTo(LocationCalculator.findCloseLocationTo(target, 4));
								bot.breakBlock(target.getBlock());
								List<Location> locs = Landscape.getMap().get(goal.getType());
								if (locs.size() == 1) {
									Landscape.getMap().remove(goal.getType());
								}
								else {
									locs.remove(target);
									Landscape.getMap().put(goal.getType(), locs);
								}
							}
							break;
						}
					}
				}
			}
		}, 0L, 60L);
	}
	
	@Override
	public void onDisable() {
		Plugins.unload(this);
	}
	
	public static List<Bot> listBots() {
		return bots;
	}
	
	public static boolean isBot(LivingEntity n) {
		for (Bot bot: listBots()) {
			if (bot.getEntity() == n) {
				return true;
			}
		}
		return false;
	}
	
	public static Bot getBot(LivingEntity n) {
		for (Bot bot: listBots()) {
			if (bot.getEntity() == n) {
				return bot;
			}
		}
		return null;
	}

}
