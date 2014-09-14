package Bammerbom.UltimateCore.Commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.InventoryUtil;

public class CmdCompact{
	static Plugin plugin;
	public CmdCompact(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.compact", false, true)) return;
		if(!r.isPlayer(sender)) return;
		Player p = (Player) sender;
		Integer a = 0;
		if(r.checkArgs(args, 0) && args[0].equalsIgnoreCase("hand")){
			condenseStack(p, p.getItemInHand(), false);
		}else{
			for(ItemStack s : p.getInventory().getContents()){
				if(condenseStack(p, s, true)){
					a++;
				}
			}
		}
		p.updateInventory();
		if(a == 0){
			p.sendMessage(r.default1 + "No items to compact.");
		}else{
		    p.sendMessage(r.default1 + "Compacted inventory. ");
		}
		
		
	}
	//
	private static Map<ItemStack, SimpleRecipe> condenseList = new HashMap<ItemStack, SimpleRecipe>();
	 private static boolean condenseStack(Player p, ItemStack stack, boolean validateReverse)
	  {
	    SimpleRecipe condenseType = getCondenseType(stack);
	    if (condenseType != null)
	    {
	      ItemStack input = condenseType.getInput();
	      ItemStack result = condenseType.getResult();

	      if (validateReverse)
	      {
	        boolean pass = false;
	        for (Recipe revRecipe : Bukkit.getServer().getRecipesFor(input))
	        {
	          if (getStackOnRecipeMatch(revRecipe, result) != null)
	          {
	            pass = true;
	            break;
	          }
	        }
	        if (!pass)
	        {
	          return false;
	        }
	      }

	      int amount = 0;

	      for (ItemStack contents : p.getInventory().getContents())
	      {
	        if ((contents != null) && (contents.isSimilar(stack)))
	        {
	          amount += contents.getAmount();
	        }
	      }

	      int output = amount / input.getAmount() * result.getAmount();
	      amount -= amount % input.getAmount();

	      if (amount > 0)
	      {
	        input.setAmount(amount);
	        result.setAmount(output);
	        if(p.getInventory().containsAtLeast(input, input.getAmount())){
	        	p.getInventory().removeItem(new ItemStack[]{ input });
	        }
	        InventoryUtil.addItem(p.getInventory(), result);
	        return true;
	      }
	    }
	    return false;
	  }

	  private static SimpleRecipe getCondenseType(ItemStack stack)
	  {
	    if (condenseList.containsKey(stack))
	    {
	      return (SimpleRecipe)condenseList.get(stack);
	    }

	    Iterator<Recipe> intr = Bukkit.getServer().recipeIterator();
	    while (intr.hasNext())
	    {
	      Recipe recipe = (Recipe)intr.next();
	      Collection<ItemStack> recipeItems = getStackOnRecipeMatch(recipe, stack);

	      if ((recipeItems != null) && ((recipeItems.size() == 4) || (recipeItems.size() == 9)) && (recipeItems.size() > recipe.getResult().getAmount()))
	      {
	        ItemStack input = stack.clone();
	        input.setAmount(recipeItems.size());
	        SimpleRecipe newRecipe = new SimpleRecipe(recipe.getResult(), input);
	        condenseList.put(stack, newRecipe);
	        return newRecipe;
	      }
	    }

	    condenseList.put(stack, null);
	    return null;
	  }

	  private static Collection<ItemStack> getStackOnRecipeMatch(Recipe recipe, ItemStack stack)
	  {
	    Collection<ItemStack> inputList;
	    if ((recipe instanceof ShapedRecipe))
	    {
	      ShapedRecipe sRecipe = (ShapedRecipe)recipe;
	      inputList = sRecipe.getIngredientMap().values();
	    }
	    else
	    {
	      if ((recipe instanceof ShapelessRecipe))
	      {
	        ShapelessRecipe slRecipe = (ShapelessRecipe)recipe;
	        inputList = slRecipe.getIngredientList();
	      }
	      else
	      {
	        return null;
	      }
	    }
	    boolean match = true;
	    Iterator<ItemStack> iter = inputList.iterator();
	    while (iter.hasNext())
	    {
	      ItemStack inputSlot = (ItemStack)iter.next();
	      if (inputSlot == null)
	      {
	        iter.remove();
	      }
	      else
	      {
	        if (inputSlot.getDurability() == 32767)
	        {
	          inputSlot.setDurability((short)0);
	        }
	        if (!inputSlot.isSimilar(stack))
	        {
	          match = false;
	        }
	      }
	    }
	    if (match)
	    {
	      return inputList;
	    }
	    return null;
	  }
	
	
	//
}

class SimpleRecipe implements Recipe
{
  private ItemStack result;
  private ItemStack input;

  protected SimpleRecipe(ItemStack result, ItemStack input)
  {
    this.result = result;
    this.input = input;
  }

  public ItemStack getResult()
  {
    return result.clone();
  }

  public ItemStack getInput()
  {
    return input.clone();
  }
}
