package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdKick{
	static Plugin plugin;
	public CmdKick(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Kick.Usage"));
			return;
		}
		CommandSender p = sender;
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null){
			p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		if(target.hasPermission("uc.antiban")){
			sender.sendMessage(r.mes("AntiBan").replaceAll("%Player", target.getName()).replaceAll("%Action", "kick"));
			return;
		}
    	if(r.checkArgs(args, 1) == false){
    		Bukkit.broadcastMessage(r.mes("Kick.Broadcast")
					.replaceAll("%Kicker", sender.getName())
					.replaceAll("%Player", target.getName()));
			Bukkit.broadcastMessage(r.mes("Kick.Broadcast2").replaceAll("%Reason", r.mes("Kick.StandardReason")));
    		target.kickPlayer(r.mes("Kick.KickMessage").replaceAll("%Reason", r.mes("Kick.StandardReason")));
    	}else{
    		Bukkit.broadcastMessage(r.mes("Kick.Broadcast")
					.replaceAll("%Kicker", sender.getName())
					.replaceAll("%Player", target.getName()));
			Bukkit.broadcastMessage(r.mes("Kick.Broadcast2").replaceAll("%Reason", r.getFinalArg(args, 1)));
    		target.kickPlayer(r.mes("Kick.KickMessage").replaceAll("%Reason", r.getFinalArg(args, 1)));
    	}
	}

}

