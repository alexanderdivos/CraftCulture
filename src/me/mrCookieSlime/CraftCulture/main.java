package me.mrCookieSlime.CraftCulture;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		System.out.println("[CraftCulture] " + "CraftCulture v" + getDescription().getVersion() + " enabled!");
		
		loadConfig();
		
		new BotAI(this);
		
		// Listeners:
		
		new BotSpawnListener(this);
		
		// Moving Timer
		
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Villager v: Villagers.getActiveVillagers()) {
					if (BotAI.hasMovingTask(v)) {
						v.teleport(BotAI.getNextPositionToWalk(v));
					}
					else {
						v.teleport(BotAI.getCurrentLocation(v));
					}
				}
				
			}
		}, 0L, 1L);
		
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Villager v: Villagers.getActiveVillagers()) {
					for (Entity e: v.getWorld().getEntities()) {
						if (e instanceof LivingEntity) {
							if (e.getLocation().distance(v.getLocation()) <= 4) {
								if (BotAI.isAngryOn(v, (LivingEntity) e)) {
									Bukkit.getPluginManager().callEvent(new EntityDamageEvent(e, DamageCause.ENTITY_ATTACK, 4.0));
								}
							}
						}
					}
				}
				
			}
		}, 0L, 16L);
		
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
