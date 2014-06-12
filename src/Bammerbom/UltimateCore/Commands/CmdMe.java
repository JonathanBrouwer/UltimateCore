package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdMe{
	static Plugin plugin;
	public CmdMe(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		String chatcolor1 = r.getCnfg().getString("Chat.Color1") + "";
		String chatcolor2 = r.getCnfg().getString("Chat.Color2") + "";
		String cc1 = ChatColor.translateAlternateColorCodes('&', chatcolor1);
		String cc2 = ChatColor.translateAlternateColorCodes('&', chatcolor2);
		if(!r.perm(sender, "uc.me", true, true)){
			return;
		}
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Me.Usage"));
			return;
		}
		Bukkit.broadcastMessage(cc1 + "* " + cc2 + sender.getName() + " " + r.getFinalArg(args, 0));
	}
}
