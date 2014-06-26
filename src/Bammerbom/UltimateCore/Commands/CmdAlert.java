package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdAlert{
	static Plugin plugin;
	public CmdAlert(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
    	if(!r.perm(sender, "uc.alert", false, true)) return;
		if(r.checkArgs(args, 0) == false){
    	    sender.sendMessage(r.mes("Alert.Usage"));
    	    return;
    	}
		//TODO customizable
    	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', r.mes("Alert.Format").replaceAll("%Message", r.getFinalArg(args, 0))));
	}
}
