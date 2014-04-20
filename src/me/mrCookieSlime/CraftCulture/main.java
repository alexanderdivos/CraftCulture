package me.mrCookieSlime.CraftCulture;

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
	
	@Override
	public void onEnable() {
		System.out.println("[CraftCulture] " + "CraftCulture v" + getDescription().getVersion() + " enabled!");
		
		loadConfig();
		new BotAI(this);
		
		// Listeners:
		
		new BotSpawnListener(this);
		new BotDamageListener(this);
		
		// Moving Timer
		
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Villager v: Villagers.getActiveVillagers()) {
					if (!BotAI.hasMovingTask(v)) {
						v.teleport(BotAI.getCurrentLocation(v));
					}
					else {
						Location next = BotAI.getNextPositionToWalk(v);
						if (next != null) {
							int height = (int) (next.getY() - v.getLocation().getY());
							if (height != 0) {
								v.teleport(next);
							}
							else {
								Vector move = new Vector(next.getX() - v.getLocation().getX(), height, next.getZ() - v.getLocation().getZ());
								move.multiply(0.125);
								v.setVelocity(move);
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
