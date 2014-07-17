package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdCompact{
	static Plugin plugin;
	public CmdCompact(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.enchant", false, true)) return;
		if(!r.isPlayer(sender)) return;
		
		
	}
}
