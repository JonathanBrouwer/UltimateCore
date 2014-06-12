package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdHat{
	static Plugin plugin;
	public CmdHat(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			sender.sendMessage(r.mes("Commands.PlayerIsConsoleError"));
			return;
		}
		if(!r.perm(sender, "uc.hat", false, true)){
			return;
		}
		Player p = (Player)sender;
    	ItemStack InHandItem = p.getItemInHand();
    	if(p.getInventory().getHelmet() == null || p.getInventory().getHelmet().getType().equals(Material.AIR)){
    		p.getInventory().setHelmet(InHandItem);
    	    p.getInventory().remove(InHandItem);
    	}else{
    		ItemStack tohand = p.getInventory().getHelmet();
    		p.getInventory().setHelmet(InHandItem);
    	    p.getInventory().remove(InHandItem);
    	    p.getInventory().setItemInHand(tohand);
    	}
    	p.sendMessage(r.mes("Hat"));
    	
    	
	}
}


