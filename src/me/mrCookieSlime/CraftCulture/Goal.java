package me.mrCookieSlime.CraftCulture;

import org.bukkit.Material;

public class Goal {
	
	Material type;
	int amount;
	
	public Goal(Material type, int amount) {
		this.type = type;
		this.amount = amount;
	}
	
	public Material getType() {
		return this.type;
	}
	
	public int getAmount() {
		return this.amount;
	}

}
