package me.mrCookieSlime.CraftCulture;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		System.out.println("[CraftCulture] " + "CraftCulture v" + getDescription().getVersion() + " enabled!");
		
		loadConfig();
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
