package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class EventTimber implements Listener{
	static Plugin plugin;
	public EventTimber(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Material block = event.getBlock().getType();
		if(!block.equals(Material.LOG) && !block.equals(Material.LOG_2)){
			return;
		}
		if(plugin.getConfig().getBoolean("Timber") == false){
			return;
		}
	    Player player = event.getPlayer();
	    Location brokenBlock = event.getBlock().getLocation();
	    if(r.perm(player, "uc.timber", true, false)){
	    	Timb(player, block, brokenBlock);
	    }
	}
	
	public boolean isTool(ItemStack handItem){
	if((!handItem.getType().name().toLowerCase().contains("axe")) || (handItem.getType().name().toLowerCase().contains("pickaxe")) ){
		return false;
	}
	return true;
	}
	public void Timb(Player player, Material block, Location brokenBlock){
	    ItemStack handItem = player.getItemInHand();
	    World world = player.getWorld();    

	    if ((player.isSneaking()))
	    {
	    	return;
	    }
	    if(isTool(handItem) || plugin.getConfig().getBoolean("TimberNeedAxe") == false)
	    {
	      double x = brokenBlock.getBlockX();
	      double x2 = brokenBlock.getBlockX() - 3;
	      double x3 = brokenBlock.getBlockX() - 3;
	      
	      double y = brokenBlock.getBlockY();
	      double y2 = brokenBlock.getBlockY() - 2;
	      
	      double z = brokenBlock.getBlockZ();
	      double z2 = brokenBlock.getBlockZ() - 3;
	      double z3 = brokenBlock.getBlockZ() - 3;
	      
	      double height = 1D;
	      
	      boolean logsLeft = true;
	      boolean isTree = false;
	      

	      while (logsLeft) {
	        y += 1D;
	        height += 1D;
	        Location blockAbove = new Location(world, x, y, z);
	        
	        Material blockAboveType = blockAbove.getBlock().getType();
	        
	        if (blockAboveType == Material.LOG || blockAboveType == Material.LOG_2) {
	          blockAbove.getBlock().breakNaturally();
	          player.getWorld().playEffect(blockAbove, Effect.SMOKE, 4);
	          
	          if ((!player.getGameMode().equals(GameMode.CREATIVE))) {
	            int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
	            long random = Math.round(Math.random() * enchLvl);
	            
	            if ((random == 0L) && 
	              (handItem.getType().getMaxDurability() > handItem.getDurability())) {
	              handItem.setDurability((short)(handItem.getDurability() + 1));
	            } 
	          } 
	          
	          logsLeft = true;
	        } else if (blockAboveType == Material.LEAVES || blockAboveType == Material.LEAVES_2) {
	          logsLeft = false;
	          isTree = true;
	        } else {
	          logsLeft = false;
	          isTree = false;
	        } 
	      } 
	      

	      if (isTree == true)
	      {


	        for (int yVec = 0; yVec < height + 6.0D; yVec++) {
	          x2 = x3;
	          z2 = z3;
	          for (int xVec = 0; xVec < 7; xVec++) {
	            z2 = z3;
	            for (int zVec = 0; zVec < 7; zVec++)
	            {
	              Location surround = new Location(world, x2, y2, z2);
	              Material surroundType = surround.getBlock().getType();
	              
	              if (surroundType == Material.LEAVES || surroundType == Material.LEAVES_2) {
	            	  if(plugin.getConfig().getBoolean("TimberLeaves") == true){
	  	                surround.getBlock().breakNaturally();
	            	  }
	              } 
	              

	              if (surroundType == Material.LOG || surroundType == Material.LOG_2)
	              {
	                surround.getBlock().breakNaturally();               
	                player.getWorld().playEffect(surround, Effect.SMOKE, 4);
	                if(plugin.getConfig().getBoolean("TimberAllDurability") == true){
	          
	                if (!player.getGameMode().equals(GameMode.CREATIVE)) {
	                  int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
	                  long random = Math.round(Math.random() * enchLvl);
	                  
	                  if (random == 0L) {
	                    if (handItem.getType().getMaxDurability() > handItem.getDurability()) {
	                      handItem.setDurability((short)(handItem.getDurability() + 1));
	                    } else if (handItem.getDurability() == handItem.getType().getMaxDurability()) {
	                      player.getInventory().setItemInHand(null);
	                    } 
	                  } 
	                } 
	                }
	              } 
	              z2 += 1D;
	            } 
	            x2 += 1D;
	          } 
	          y2 += 1D;
	        } 
	      } 
	    } 
	}


}
