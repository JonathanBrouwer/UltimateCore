package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdRepair implements Listener{
	static Plugin plugin;
	public CmdRepair(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!r.isPlayer(sender)){
			return;
		}
		Player p = (Player)sender;
		Boolean tb = false;
		Player t = p;
		if(r.checkArgs(args, 0) == true){
			if(r.perm(sender, "uc.repair.others", false, true) == false){
				return;
			}
			Player tl = r.searchPlayer(args[0]);
			if(tl == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}
			t = tl;
			tb = true;
		}else{
			if(r.perm(sender, "uc.repair", false, true) == false){
				return;
			}
		}
		if(r.getCnfg().getBoolean("RepairMode") == false){
			repair(t);
			if(tb == false){
				p.sendMessage(r.mes("Repair.forSelf"));
			}else{
				p.sendMessage(r.mes("Repair.selfMessage").replaceAll("%Player", t.getName()));
				t.sendMessage(r.mes("Repair.otherMessage"));
			}
		}else{
			UltimateConfiguration data = new UltimateConfiguration(UltimateFileLoader.getPlayerFile(t));
			
			if(UltimateFileLoader.getPlayerConfig(t).get("repairmode") != null && UltimateFileLoader.getPlayerConfig(t).getBoolean("repairmode") == true){
				data.set("repairmode", false);
				if(tb == false){
					p.sendMessage(r.mes("Repair.forSelfMode").replaceAll("%Status", "off"));
				}else{
					p.sendMessage(r.mes("Repair.selfMessageMode").replaceAll("%Status", "off").replaceAll("%Player", t.getName()));
					t.sendMessage(r.mes("Repair.otherMessageMode").replaceAll("%Status", "off"));
				}
			}else{
				data.set("repairmode", true);
				if(tb == false){
					p.sendMessage(r.mes("Repair.forSelfMode").replaceAll("%Status", "on"));
				}else{
					p.sendMessage(r.mes("Repair.selfMessageMode").replaceAll("%Status", "on").replaceAll("%Player", t.getName()));
					t.sendMessage(r.mes("Repair.otherMessageMode").replaceAll("%Status", "on"));
				}
			}
			
			data.save(UltimateFileLoader.getPlayerFile(t));
		}
		
		
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void onItemDamage(PlayerInteractEvent e){
        Player p = (Player) e.getPlayer();
        if(r.getCnfg().getBoolean("RepairMode") == false){
			return;
		}
        if(UltimateFileLoader.getPlayerConfig(p).get("repairmode") != null && UltimateFileLoader.getPlayerConfig(p).getBoolean("repairmode") == true){
        	repair(p);
        }   	         
    }
	@EventHandler(priority = EventPriority.LOWEST)
    public void onArmorDamage(EntityDamageEvent e){
    	if(e.getEntity() instanceof Player){
    		if(r.getCnfg().getBoolean("RepairMode") == false){
    			return;
    		}
    		Player p = (Player) e.getEntity();
    		if(UltimateFileLoader.getPlayerConfig(p).get("repairmode") != null && UltimateFileLoader.getPlayerConfig(p).getBoolean("repairmode") == true){
    			repair(p);
            }   
    		
    	}
    }
	
	
	
	
	
	
	
	//
	public static void repair(Player p){
		//
		try{
		if(p.getInventory().getHelmet().getType().name().contains("HELMET")){	
			p.getInventory().getHelmet().setDurability((short) 0);
		}else{
		}
		}catch(Exception e){}
		//
		try{
			if(p.getInventory().getChestplate().getType().name().contains("CHESTPLATE")){	
				p.getInventory().getChestplate().setDurability((short) 0);
			}}catch(Exception e){}
		//
		try{
			if(p.getInventory().getLeggings().getType().name().contains("LEGGINGS")){	
				p.getInventory().getLeggings().setDurability((short) 0);
			}}catch(Exception e){}
		//
		try{
			if(p.getInventory().getBoots().getType().name().contains("BOOTS")){	
				p.getInventory().getBoots().setDurability((short) 0);
			}}catch(Exception e){}
		//
        for(ItemStack i : p.getInventory().getContents()){
        	if(i == null){
        		continue;
        	}else{
        		
        	
        	if(
        			i.getType() == Material.BOW ||
        			i.getType() == Material.FLINT_AND_STEEL ||
        			i.getType() == Material.FISHING_ROD ||
        			i.getType() == Material.SHEARS ||
        			i.getType() == Material.CARROT_STICK ||
        			
        			i.getType() == Material.WOOD_SWORD ||
        			i.getType() == Material.WOOD_PICKAXE ||
        			i.getType() == Material.WOOD_SPADE ||
        			i.getType() == Material.WOOD_AXE ||
        			i.getType() == Material.WOOD_HOE ||
        			
        			i.getType() == Material.STONE_SWORD ||
        			i.getType() == Material.STONE_PICKAXE ||
        			i.getType() == Material.STONE_SPADE ||
        			i.getType() == Material.STONE_AXE ||
        			i.getType() == Material.STONE_HOE ||
        			
        			i.getType() == Material.IRON_SWORD ||
        			i.getType() == Material.IRON_PICKAXE ||
        			i.getType() == Material.IRON_SPADE ||
        			i.getType() == Material.IRON_AXE ||
        			i.getType() == Material.IRON_HOE ||
        			
        			i.getType() == Material.GOLD_SWORD ||
        			i.getType() == Material.GOLD_PICKAXE ||
        			i.getType() == Material.GOLD_SPADE ||
        			i.getType() == Material.GOLD_AXE ||
        			i.getType() == Material.GOLD_HOE ||
        			
        			i.getType() == Material.DIAMOND_SWORD ||
        			i.getType() == Material.DIAMOND_PICKAXE ||
        			i.getType() == Material.DIAMOND_SPADE ||
        			i.getType() == Material.DIAMOND_AXE ||
        			i.getType() == Material.DIAMOND_HOE ||
        			
        			i.getType() == Material.IRON_SWORD ||
        			i.getType() == Material.IRON_PICKAXE ||
        			i.getType() == Material.IRON_SPADE ||
        			i.getType() == Material.IRON_AXE ||
        			i.getType() == Material.IRON_HOE ||
        			
        			i.getType().name().contains("HELMET") ||
        			i.getType().name().contains("CHESTPLATE") ||
        			i.getType().name().contains("LEGGINGS") ||
        			i.getType().name().contains("BOOTS")
        			
        			
        	){
                try {
                	if(!(i.getDurability() == (short) 0)){
                    i.setDurability((short) 0);
                	}
                } catch (Exception e1){
                   e1.printStackTrace();
                }
        		
        	}else{
        		

        	}
        	}
        }
	}
}
