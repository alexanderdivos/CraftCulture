package me.mrCookieSlime.CraftCulture;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLib.Configuration.ConfigSetup;
import me.mrCookieSlime.CSCoreLib.general.Server.Plugins;
import me.mrCookieSlime.CraftCulture.Deprecated.BotAI;
import me.mrCookieSlime.CraftCulture.Deprecated.Villagers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class main extends JavaPlugin {
	
	List<Bot> bots = new ArrayList<Bot>();
	
	@Override
	public void onEnable() {
		Plugins.load(this);
		ConfigSetup.setup(this);
		
		// Listeners:
		new BotSpawnListener(this);
		new BotDamageListener(this);
		
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

		// Attack Timer
		
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Villager v: Villagers.getActiveVillagers()) {
					for (Entity e: v.getWorld().getEntities()) {
						if (e instanceof LivingEntity) {
							if (e.getLocation().distance(v.getLocation()) <= 4) {
								if (BotAI.isAngryOn(v, (LivingEntity) e)) {
									EntityDamageEvent event = new EntityDamageEvent(e, DamageCause.ENTITY_ATTACK, 4.0);
									Bukkit.getPluginManager().callEvent(event);
									Bukkit.getPluginManager().callEvent(new EntityDamageByEntityEvent(v, e, DamageCause.ENTITY_ATTACK, 4.0));
									if (!event.isCancelled()) {
										((LivingEntity) e).damage(4.0);
									}
								}
							}
						}
					}
				}
				
			}
		}, 0L, 20L);
		
		// Gathering Timer
		
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Villager v: Villagers.getActiveVillagers()) {
					Material m = Material.AIR;
					int amount = 0;
					m = BotAI.getNextResourceGoal(v);
					amount = BotAI.getResourceAmount(v, m);
					if (m != Material.AIR && amount > 0) {
						BotAI.breakBlock(v, BotAI.findClosestMaterial(v, m));
					}
					
					if (BotAI.getAvailableChest(v) == null) {
						if (BotAI.hasItemAvailable(v, Material.LOG) >= 2) {
							BotAI.placeBlock(v, Material.CHEST, (byte) 0, BotAI.findClosePlace(v, 6));
						}
					}
					
				}
			}
		}, 0L, 50L);
		
	}
	
	@Override
	public void onDisable() {
		System.out.println("CraftCulture v" + getDescription().getVersion() + " disabled!");
	}
	
	public  void loadConfig() {
		FileConfiguration cfg = getConfig();
		cfg.options().copyDefaults(true);
		saveConfig();
	}

}
