package me.mrCookieSlime.CSCoreLib.events.Listeners;

import me.mrCookieSlime.CSCoreLib.events.MenuClickEvent;
import me.mrCookieSlime.CSCoreLib.general.Inventory.Maps;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class MenuClickListener implements Listener {
	
	public MenuClickListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (Maps.inv.containsKey(e.getWhoClicked().getUniqueId())) {
			if (e.getSlot() == e.getRawSlot()) {
				if (e.getCurrentItem() != null) {
					e.setCancelled(true);
					Bukkit.getPluginManager().callEvent(new MenuClickEvent((Player) e.getWhoClicked(), e.getInventory(), e.getSlot(), e.getCurrentItem()));
				}
			}	
		}
	}

}
