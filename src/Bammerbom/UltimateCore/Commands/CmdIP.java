package Bammerbom.UltimateCore.Commands;

import java.net.UnknownHostException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdIP {
	static Plugin plugin;
	public CmdIP(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings("static-access")
	public static void handle(CommandSender sender, String[] args){
		if(r.checkArgs(args, 0) == false){
			if(!r.perm(sender, "uc.ip.server", false, false) && !r.perm(sender, "uc.ip", false, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
		sender.sendMessage(r.mes("Ip.Server").replaceAll("%IP", Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort()));
		}else{
			if(!r.perm(sender, "uc.ip.players", false, false) && !r.perm(sender, "uc.ip", false, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
			Player p = Bukkit.getPlayer(args[0]);
			if(p == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}
			try {
				sender.sendMessage(r.mes("IP.Player1").replaceAll("%Player", p.getName()).replaceAll("%Hostname", (p.getAddress().getAddress().getLocalHost().toString().split("/")[0])));
				sender.sendMessage(r.mes("IP.Player2").replaceAll("%Player", p.getName()).replaceAll("%IP", (p.getAddress().getAddress().getLocalHost().toString().split("/")[1]) + ":" + p.getAddress().getPort()));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}
}
