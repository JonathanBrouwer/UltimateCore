package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdSay {
	static Plugin plugin;
	public CmdSay(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(r.getCnfg().getBoolean("Chat.EnableCustomChat") == false){
			return;
		}
		if(r.perm(sender, "uc.say", false, true) == false){
			return;
		}
		String message = r.getFinalArg(args, 0);
		if(message == null || message.equalsIgnoreCase(" ") || message.equalsIgnoreCase("")){
			sender.sendMessage(r.mes("Say.Usage"));
			return;
		}
		message = ChatColor.translateAlternateColorCodes('&', message);
		String chatcolor1 = r.getCnfg().getString("Chat.Color1") + "";
		String chatcolor2 = r.getCnfg().getString("Chat.Color2") + "";
		String cc1 = ChatColor.translateAlternateColorCodes('&', chatcolor1);
		String cc2 = ChatColor.translateAlternateColorCodes('&', chatcolor2);
		Bukkit.broadcastMessage(cc1 + "[" + cc2 + sender.getName() + cc1 + "] " + cc2 + message);
	}
}
