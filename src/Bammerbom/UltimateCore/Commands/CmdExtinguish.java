package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdExtinguish{
	static Plugin plugin;
	public CmdExtinguish(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args) {
		if(!(r.isPlayer(sender))){
			return;
		}
		if(!r.perm(sender, "uc.extinguish", false, true)){
			return;
		}
		if(sender instanceof Player){
		Player p = (Player)sender;
		if(r.checkArgs(args, 0) == false){
			p.setFireTicks(0);
			p.sendMessage(r.mes("Extinguish.Message"));
		}
		else{
			if(!r.perm(sender, "uc.extinguish.others", false, true)){
				return;
			}
			Player target = r.searchPlayer(args[0]);
			if(target == null || !target.isOnline()){
				p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
				return;
			}	
			else{
				target.sendMessage(r.mes("Extinguish.ToOther").replaceAll("%Player", p.getName()));
				p.sendMessage(r.mes("Extinguish.ToSelf").replaceAll("%Player", target.getName()));
				target.setFireTicks(0);
			}
		}
		}
	}
}
