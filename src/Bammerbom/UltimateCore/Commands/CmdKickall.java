package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdKickall{
	static Plugin plugin;
	public CmdKickall(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.kickall", false, true)){
			return;
		}
		if(!r.checkArgs(args, 0)){
			for(Player p : r.getOnlinePlayers()){
				if(!r.perm(p, "uc.antiban", false, false) && !p.equals(sender)){
					p.kickPlayer("");
				}
			}
			return;
		}else{
			for(Player p : r.getOnlinePlayers()){
				if(!r.perm(p, "uc.antiban", false, false) && !p.equals(sender)){
					p.kickPlayer(r.getFinalArg(args, 0));
				}
			}
			return;
		}
		
		
	}
}
