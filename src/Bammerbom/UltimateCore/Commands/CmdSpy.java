 package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;

public class CmdSpy{
	static Plugin plugin;
	public CmdSpy(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String label, String[] args){
		if(!r.perm(sender, "uc.spy", true, true)){
			return;
		}
	    if(!r.isPlayer(sender)) return;
	    Player p = (Player) sender;
	    UCplayer up = UC.getPlayer(p);
	    up.setSpy(!up.isSpy());
	    sender.sendMessage(r.default1 + "Spy mode is now " + r.default2 + (up.isSpy() ? "enabled" : "disabled"));
	}
	
	//
	
}
