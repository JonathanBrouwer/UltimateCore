package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Databases.ItemDatabase;

public class CmdRecipe implements Listener{
	static Plugin plugin;
	public CmdRecipe(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	static ArrayList<UUID> inView = new ArrayList<UUID>();
	public static void run(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.recipe", true, true)) return;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.default1 + "/recipe " + r.default2 + "<Item> [Number]");
			return;
		}
		ItemStack item = ItemDatabase.getItem(args[0]);
		if(item == null){
			sender.sendMessage(r.default1 + "Item not found! (" + r.default2 + args[0] + r.default1 + ")");
			return;
		}
	    int recipeNo = 0;
	    if (args.length > 1){
	        if (r.isNumber(args[1])){
	            recipeNo = Integer.parseInt(args[1]) - 1;
	        }else{
	            sender.sendMessage(r.mes("NumberFormat").replaceAll("%Amount", args[1]));
	            return;
	        }
	    }
	    List<Recipe> recipes = Bukkit.getServer().getRecipesFor(item);
	    if(recipes.isEmpty()){
	    	sender.sendMessage(r.default1 + "No recipes found.");
	    	return;
	    }
	    if(recipes.size() <= (recipeNo)){
	    	sender.sendMessage(r.default1 + "There are not " + (recipeNo + 1) + " recipes.");
	    	return;
	    }
	    Recipe selected = recipes.get(recipeNo);
	    if(selected instanceof FurnaceRecipe){
	    	furnaceRecipe(sender, (FurnaceRecipe)selected);
	    }else if(selected instanceof ShapedRecipe){
	    	shapedRecipe(sender, (ShapedRecipe)selected, sender instanceof Player);
	    }else if(selected instanceof ShapelessRecipe){
	    	if(item.getType().equals(Material.FIREWORK)){
	            ShapelessRecipe shapelessRecipe = new ShapelessRecipe(item);
	            shapelessRecipe.addIngredient(Material.SULPHUR);
	            shapelessRecipe.addIngredient(Material.PAPER);
	            shapelessRecipe.addIngredient(Material.FIREWORK_CHARGE);
	            shapelessRecipe(sender, shapelessRecipe, sender instanceof Player);
	    	}
	    	shapelessRecipe(sender, (ShapelessRecipe) selected, sender instanceof Player);
	    }
	    if(recipes.size() > 1){
	    	sender.sendMessage(r.default1 + "Type " + r.default2 + "/recipe " + args[0] + " <Number> " + r.default1 + "to see other recipes for " + item.getType().name().toLowerCase().replaceAll("_", ""));
	    }
	}
	//
	private static void furnaceRecipe(CommandSender p, FurnaceRecipe recipe){
		p.sendMessage(r.default1 + "Smelt: " + r.default2 + recipe.getInput().getType().name().toLowerCase().replaceAll("_", ""));
	}
	private static void shapedRecipe(CommandSender sender, ShapedRecipe recipe, boolean showWindow){
		Map<Character, ItemStack> recipeMap = recipe.getIngredientMap();
		if(showWindow){
			Player p = (Player) sender;
			p.closeInventory();
			InventoryView view = p.openWorkbench(null, true);
			inView.add(p.getUniqueId());
			String[] recipeShape = recipe.getShape();
		      Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
		      for (int j = 0; j < recipeShape.length; j++)
		      {
		        for (int k = 0; k < recipeShape[j].length(); k++)
		        {
		          ItemStack item = (ItemStack)ingredientMap.get(Character.valueOf(recipeShape[j].toCharArray()[k]));
		          if (item != null)
		          {
		            item.setAmount(0);
		            view.getTopInventory().setItem(j * 3 + k + 1, item);
		          }
		        }
		      }
		}else{
			HashMap<Material, String> colorMap = new HashMap<Material, String>();
		      int i = 1;
		      char[] arr$ = "abcdefghi".toCharArray(); int len$ = arr$.length; for (int i$ = 0; i$ < len$; i$++) { Character c = Character.valueOf(arr$[i$]);

		        ItemStack item = (ItemStack)recipeMap.get(c);
		        if (!colorMap.containsKey(item == null ? null : item.getType()))
		        {
		          colorMap.put(item == null ? null : item.getType(), String.valueOf(i++));
		        }
		      }
		      Material[][] materials = new Material[3][3];
		      for (int j = 0; j < recipe.getShape().length; j++)
		      {
		        for (int k = 0; k < recipe.getShape()[j].length(); k++)
		        {
		          ItemStack item = (ItemStack)recipe.getIngredientMap().get(Character.valueOf(recipe.getShape()[j].toCharArray()[k]));
		          materials[j][k] = (item == null ? null : item.getType());
		        }
		      }
		      sender.sendMessage(r.default2 + "" + colorMap.get(materials[0][0]) + r.default1 + " | " + r.default2 + colorMap.get(materials[0][1]) + r.default1 + " | " + r.default2 + colorMap.get(materials[0][2]));
		      sender.sendMessage(r.default2 + "" + colorMap.get(materials[1][0]) + r.default1 + " | " + r.default2 + colorMap.get(materials[1][1]) + r.default1 + " | " + r.default2 + colorMap.get(materials[1][2]));
		      sender.sendMessage(r.default2 + "" + colorMap.get(materials[2][0]) + r.default1 + " | " + r.default2 + colorMap.get(materials[2][1]) + r.default1 + " | " + r.default2 + colorMap.get(materials[2][2]));

		      StringBuilder s = new StringBuilder();
		      /*for (Material items : (Material[])colorMap.keySet().toArray(new Material[colorMap.size()]))
		      {
		    	  String name = (items != null) ? items.name().toLowerCase().replaceAll("_", "") : "nothing";
		    	  String a = (items != null) ? colorMap.get(items).toString() : "null";
		    	  s.append(a + " is " + name + ", ");
		    	  r.log(colorMap);
		      }*/
		      Boolean a = false;
		      List<Material> mats = new ArrayList<>(colorMap.keySet());
		      Collections.reverse(mats);
		      for(Material mat : mats){
		    	  String name = (mat != null && mat != Material.AIR) ? mat.name().toLowerCase().replaceAll("_", "") : "nothing";
		    	  String num = colorMap.get(mat);
		    	  s.append((a ? ", " : "") + num + " is " + name);
		    	  a = true;
		      }
		      sender.sendMessage(r.default1 + "Where: " + r.default2 + s.toString());
		}
	}
	private static void shapelessRecipe(CommandSender sender, ShapelessRecipe recipe, boolean showWindow){
		List<ItemStack> ingredients = recipe.getIngredientList();
	    if (showWindow){
	      Player p = (Player) sender;
	      InventoryView view = p.openWorkbench(null, true);
	      inView.add(p.getUniqueId());
	      for (int i = 0; i < ingredients.size(); i++){
	        view.setItem(i + 1, (ItemStack)ingredients.get(i));
	      }

	    }else{
	      StringBuilder s = new StringBuilder();
	      for (int i = 0; i < ingredients.size(); i++){
	        s.append(((ItemStack)ingredients.get(i)).getType().name().toLowerCase().replaceAll("_", ""));
	        if (i != ingredients.size() - 1){
	          s.append(",");
	        }
	        s.append(" ");
	      }
	      sender.sendMessage(r.default1 + "Combine " + r.default2 + s.toString());
	    }
	}
	//
	@EventHandler
	public void onClick(final InventoryClickEvent e){
		if(inView.contains(e.getWhoClicked().getUniqueId()) && e.getInventory().getType() == InventoryType.WORKBENCH){
			e.setCancelled(true);
		      Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		      {
				public void run()
		        {
		          ((Player)e.getWhoClicked()).updateInventory();
		        }
		      }
		      , 1L);
		}
	}
	@EventHandler
	public void onClose(final InventoryCloseEvent e){
		if(inView.contains(e.getPlayer().getUniqueId())){
			inView.remove(e.getPlayer().getUniqueId());
			e.getInventory().clear();
		}
	}
}
