package me.mrCookieSlime.CraftCulture;

import me.mrCookieSlime.CraftCulture.Deprecated.Villagers;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class BotDamageListener implements Listener {
	
	public main plugin;
	
	public BotDamageListener(main instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSpawn(EntityDamageEvent e) {
		if (e.getEntity() instanceof Villager) {
			if (Villagers.getActiveVillagers().contains(e.getEntity())) {
				if (e.getCause() == DamageCause.FALL) {
					e.setCancelled(true);
				}
			}
		}
	}

}
