package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;

public class CmdIP {
	static Plugin plugin;
	public CmdIP(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(r.checkArgs(args, 0) == false){
			if(!r.perm(sender, "uc.ip.server", false, false) && !r.perm(sender, "uc.ip", false, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
		sender.sendMessage(r.mes("IP.Server").replaceAll("%IP", Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort()));
		}else{
			if(!r.perm(sender, "uc.ip.players", false, false) && !r.perm(sender, "uc.ip", false, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
			Player p = UC.searchPlayer(args[0]);
			if(p == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}
			sender.sendMessage(r.mes("IP.Player2").replaceAll("%Player", p.getName()).replaceAll("%IP", (p.getAddress().toString().toString().split("/")[1].split(":")[0])));
		}
	}
}
