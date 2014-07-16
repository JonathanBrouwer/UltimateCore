package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdTpall{
	static Plugin plugin;
	public CmdTpall(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!r.perm(sender, "uc.tpall", false, true)){
			return;
		}
		if(!r.isPlayer(sender)){
			return;
		}
	    Player p = (Player) sender;
	    for(Player t : UC.getOnlinePlayers()){
	    	if(!t.equals(p)){
	    		t.teleport(p, TeleportCause.COMMAND);
	    	}
	    }
	}
}
