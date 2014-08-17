package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdRealname implements Listener{
	static Plugin plugin;
	public CmdRealname(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.realname", false, true)){
			return;
		}
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Realname.Usage"));
			return;
		}
		OfflinePlayer t = null;
		//SEARCH
	    String lowerName = args[0].toLowerCase();
	    int delta = -1;
	    for (Player player : r.getOnlinePlayers()){
	      if (ChatColor.stripColor(UC.getPlayer(player).getNick()).toLowerCase().startsWith(lowerName)) {
	        int curDelta = player.getName().length() - lowerName.length();
	        if (curDelta < delta) {
	          t = player;
	          delta = curDelta;
	        }
	        if (curDelta == 0)
	          break;
	      }
	    }
		if(t == null){
		    for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
		    	if(!player.hasPlayedBefore() && !player.isOnline()) continue;
			      if (ChatColor.stripColor(UC.getPlayer(player).getNick()).toLowerCase().startsWith(lowerName)) {
			        int curDelta = player.getName().length() - lowerName.length();
			        if (curDelta < delta) {
			          t = player;
			          delta = curDelta;
			        }
			        if (curDelta == 0)
			          break;
			      }
			    }
		    if(t == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		    }
		}
		sender.sendMessage(r.mes("Realname.Message").replaceAll("%Nick", UC.getPlayer(t).getNick()).replaceAll("%Name", t.getName()));
		
	}
}
