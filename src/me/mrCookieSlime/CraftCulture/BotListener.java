package me.mrCookieSlime.CraftCulture;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BotListener implements Listener {
	
	public CraftCulture plugin;
	
	public BotListener(CraftCulture instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSpawn(EntityDamageEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			if (CraftCulture.isBot((LivingEntity) e.getEntity())) {
				if (e.getCause() == DamageCause.FALL) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	// Just temporary for testing Purposes:
	
	@EventHandler
	public void onSpawn(PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (e.getClickedBlock().getType() == Material.CHEST) {
					if (e.getItem().isSimilar(new ItemStack(Material.CARROT_ITEM))) {
						e.setCancelled(true);
						Bot bot = new Bot(plugin.getConfig().getStringList("names").get(new Random().nextInt(plugin.getConfig().getStringList("names").size())));
						bot.spawn(e.getPlayer().getLocation(), e.getClickedBlock());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void openInventory(PlayerInteractEntityEvent e) {
		if (e.getRightClicked() instanceof LivingEntity) {
			if (CraftCulture.isBot((LivingEntity) e.getRightClicked())) {
				e.setCancelled(true);
				e.getPlayer().openInventory(CraftCulture.getBot((LivingEntity) e.getRightClicked()).getInventory());
			}
		}
	}
}
