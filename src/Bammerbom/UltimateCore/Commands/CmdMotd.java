package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Events.EventMOTD;

public class CmdMotd{
	static Plugin plugin;
	public CmdMotd(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
    	if(!r.perm(sender, "uc.motd", false, true)) return;
    	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', EventMOTD.msg));
	}
}
