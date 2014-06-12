package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdKit{
	static Plugin plugin;
	public CmdKit(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
 
	public static void handle(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.kit", false, true)){
			return;
		}
		@SuppressWarnings("unused")
		Player p = (Player) sender;
	}
}
