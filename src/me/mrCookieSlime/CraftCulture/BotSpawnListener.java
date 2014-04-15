package me.mrCookieSlime.CraftCulture;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BotSpawnListener implements Listener {
	
	public main plugin;
	
	public BotSpawnListener(main instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSpawn(PlayerInteractEvent e) {
		if (e.getItem().isSimilar(new ItemStack(Material.CARROT_ITEM))) {
			Villager v = Villagers.spawn(plugin.getConfig().getStringList("names").get(new Random().nextInt(plugin.getConfig().getStringList("names").size())), e.getPlayer().getLocation());
			
			Location test = v.getLocation().clone();
			test.setX(v.getLocation().getX() + 10);
			
			BotAI.walkTo(v, test);
		}
	}

}
