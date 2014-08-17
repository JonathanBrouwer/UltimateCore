package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.ReflectionUtil;

public class CmdPing {
	static Plugin plugin;
	public CmdPing(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
			if(!r.perm(sender, "uc.ping", false, false)){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
			Player pl;
			if(r.checkArgs(args, 0)){
			    pl = r.searchPlayer(args[0]);
			}else{
				if(!r.isPlayer(sender)){
					return;
				}
				Player p = (Player) sender;
				pl = p;
			}
			if(pl == null){
				sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}
			if(r.checkArgs(args, 0) && !r.perm(sender, "uc.ping.others", false, true)) return;
			sender.sendMessage(r.mes("Ping.Message").replaceAll("%Player", pl.getName()).replaceAll("%Ping", getPing(pl) + ""));
			
	}
	
	public static int getPing(Player p){
		try {
			Integer ping = (Integer) ReflectionUtil.execute("getHandle().ping", p);
			return ping;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
