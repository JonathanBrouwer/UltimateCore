package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdEnderchest{
	static Plugin plugin;
	public CmdEnderchest(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			return;
		}
		
		Player p = (Player)sender;
    	if(r.checkArgs(args, 0) == false){
    		if(!r.perm(sender, "uc.enderchest", false, true)){ return; }
    		p.openInventory(p.getEnderChest());
    	}else{
    		if(!r.perm(sender, "uc.enderchest.others", false, true)){ return; }
    		Player target = r.searchPlayer(args[0]);
    		if(target != null){
    			p.openInventory(target.getEnderChest());
    		}
    		else{
    			p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
    		}
    	}
	}
}
