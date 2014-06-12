package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdStop{
	static Plugin plugin;
	public CmdStop(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void run(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.stop", false, true)){
			return;
		}
		try{
		Bukkit.broadcastMessage(r.mes("Stop.Stop").replaceAll("%Player", sender.getName()));
		for(Player p : Bukkit.getOnlinePlayers()){
			if(r.checkArgs(args, 0) == true){
			    p.kickPlayer(r.default1 + r.getFinalArg(args, 0));
		    }else{
		    	p.kickPlayer(r.mes("Stop.StandardKickMessage").replaceAll("%Player", sender.getName()));
		    }
		}
		Bukkit.getServer().shutdown();
		}catch(Exception ex){
			Bukkit.getServer().shutdown();
		}
	}
}
