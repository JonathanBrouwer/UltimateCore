package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdBurn{
	static Plugin plugin;
	public CmdBurn(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.isPlayer(sender)){
			return;
		}
		if(!r.perm(sender, "uc.burn", false, true)){
			return;
		}
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.default1 + "/burn" + r.default2 + " <Player> [Time]");
			return;
		}
		Double time = 10.0;
		Player t;
		if(r.searchPlayer(args[0]) != null){
			t = r.searchPlayer(args[0]);
		}else{
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		if(r.checkArgs(args, 1) && r.isDouble(args[1])){
			time = Double.parseDouble(args[1]);
		}
		time = time * 20;
		t.setFireTicks(time.intValue());
		sender.sendMessage(r.default1 + "Set " + r.default2 + t.getName() + r.default1 + " on fire for " + r.default2 + Double.valueOf(time / 20).intValue() + r.default1 + " seconds.");
	}
}
