package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;

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
		Player t = null;
		for(Player p : Bukkit.getOnlinePlayers()){
			UCplayer up = UC.getPlayer(p);
			if(ChatColor.stripColor(up.getNick()).equalsIgnoreCase(args[0])){
				t = p;
			}
		}
		if(t == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		sender.sendMessage(r.mes("Realname.Usage").replaceAll("%Nick", UC.getPlayer(t).getNick()).replaceAll("%Name", t.getName()));
		
	}
}
