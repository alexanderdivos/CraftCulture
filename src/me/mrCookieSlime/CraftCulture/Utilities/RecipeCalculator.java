package me.mrCookieSlime.CraftCulture.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dropper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeCalculator {
	
	public static List<Material> BUCKETS = new ArrayList<Material>();
	
	public static List<ItemStack> getIngredients(Recipe recipe) {
	    List<ItemStack> ingredients = new ArrayList<ItemStack>();
	    if ((recipe instanceof ShapedRecipe)) {
	      ShapedRecipe sr = (ShapedRecipe)recipe;
	      String[] shape = sr.getShape();

	      for (String row : shape) {
	        for (int i = 0; i < row.length(); i++) {
	          ItemStack stack = (ItemStack)sr.getIngredientMap().get(Character.valueOf(row.charAt(i)));
	          for (ItemStack ing : ingredients) {
	            int mss = ing.getType().getMaxStackSize();
	            if ((ing.isSimilar(stack)) && (ing.getAmount() < mss)) {
	              int canAdd = mss - ing.getAmount();
	              int add = Math.min(canAdd, stack.getAmount());
	              ing.setAmount(ing.getAmount() + add);
	              int remaining = stack.getAmount() - add;
	              if (remaining >= 1) {
	                stack.setAmount(remaining);
	              } else {
	                stack = null;
	                break;
	              }
	            }
	          }
	          if ((stack != null) && (stack.getAmount() > 0))
	            ingredients.add(stack);
	        }
	      }
	    }
	    else if ((recipe instanceof ShapelessRecipe)) {
	      for (ItemStack i : ((ShapelessRecipe)recipe).getIngredientList()) {
	        for (ItemStack ing : ingredients) {
	          int mss = ing.getType().getMaxStackSize();
	          if ((ing.isSimilar(i)) && (ing.getAmount() < mss)) {
	            int canAdd = mss - ing.getAmount();
	            ing.setAmount(ing.getAmount() + Math.min(canAdd, i.getAmount()));
	            int remaining = i.getAmount() - Math.min(canAdd, i.getAmount());
	            if (remaining < 1) break;
	            i.setAmount(remaining);
	          }

	        }

	        if (i.getAmount() > 0) {
	          ingredients.add(i);
	        }
	      }
	    }
	    return ingredients;
	  }
	
	
	public static ItemStack getItem(Location l) {
		ItemStack item = null;
		
		if (l.getBlock().getRelative(BlockFace.UP).getType() == Material.DROPPER) {
			item = ((Dropper) l.getBlock().getRelative(BlockFace.UP).getState()).getInventory().getItem(0);
		}
		
		return item;
    }
	
	@SuppressWarnings("deprecation")
	public static boolean removeItem(Inventory inv, Material type, int data, int amount)
	  {
	    int remd = 0;

	    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	    for (int i = 0; i < inv.getSize(); i++) {
	      ItemStack s = inv.getItem(i);

	      if (s != null)
	      {
	        if (remd < amount)
	        {
	          if ((s.getType().equals(type)) && ((data == -1) || (data == s.getData().getData()))) {
	            int take = Math.min(s.getAmount(), amount - remd);
	            map.put(Integer.valueOf(i), Integer.valueOf(take));
	            remd += take;

	            if (take == s.getAmount())
	              inv.setItem(i, null);
	            else {
	              s.setAmount(s.getAmount() - take);
	            }
	          }
	        }
	      }
	    }
	    if (remd != amount) {
	      return false;
	    }
	    return true;
	  }

}
