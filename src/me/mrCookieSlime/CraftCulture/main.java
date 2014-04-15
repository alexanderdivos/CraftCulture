package me.mrCookieSlime.CraftCulture;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Villager;
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
				}
				
			}
		}, 0L, 10L);
		
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
