package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdKick{
	static Plugin plugin;
	public CmdKick(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!r.perm(sender, "uc.kick", false, true)) return;
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Kick.Usage"));
			return;
		}
		CommandSender p = sender;
		Player target = UC.searchPlayer(args[0]);
		if(target == null){
			p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		if(p.getName().equalsIgnoreCase(target.getName())){
			sender.sendMessage(r.mes("AntiBan").replaceAll("%Player", sender.getName()).replaceAll("%Action", "kick"));
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

