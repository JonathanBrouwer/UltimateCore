package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdCoords{
	static Plugin plugin;
	public CmdCoords(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
    	if(r.checkArgs(args, 0)){
    		if(!r.perm(sender, "uc.coords.others", false, true)){
    			return;
    		}
    		Player p = UC.searchPlayer(args[0]);
    		if(p == null){ sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0])); return; }
    		sender.sendMessage(r.mes("Coords.Others").replaceAll("%Player", p.getName()).replaceAll("%Location", p.getWorld().getName() + " " + p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ()));
    	}else{
    		if(!r.perm(sender, "uc.coords", false, true)){
    			return;
    		}
    		if(!r.isPlayer(sender)) return;
    		Player p = (Player) sender;
    		sender.sendMessage(r.mes("Coords.Self").replaceAll("%Location", p.getWorld().getName() + " " + p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ()));
    	}
	}
}
