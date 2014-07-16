package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdCi{
	static Plugin plugin;
	public CmdCi(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
    	if(r.checkArgs(args, 0) == false){
    		if(!(r.isPlayer(sender))){
    			return;
    		}
			if(!r.perm(sender, "uc.clear", true, true)){
				return;
			}
    		Player p = (Player) sender;
    	    p.getInventory().clear();
    	    p.getInventory().setHelmet(null);
    	    p.getInventory().setChestplate(null);
    	    p.getInventory().setLeggings(null);
    	    p.getInventory().setBoots(null);
    	    p.sendMessage(r.mes("Clear.SelfMessage"));
    		
    	}else{
			if(!r.perm(sender, "uc.clear.others", false, true)){
				return;
			}
    		Player t = UC.searchPlayer(args[0]);
    		t.getInventory().clear();
    	    t.getInventory().setHelmet(null);
    	    t.getInventory().setChestplate(null);
    	    t.getInventory().setLeggings(null);
    	    t.getInventory().setBoots(null);
    		sender.sendMessage(r.mes("Clear.ToSelf").replaceAll("%Player", t.getName()));
    		t.sendMessage(r.mes("Clear.ToOther").replaceAll("%Player", sender.getName()));
    		
    	}

	}
}
