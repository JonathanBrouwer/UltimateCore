package Bammerbom.UltimateCore.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdSetHealth {
	static Plugin plugin;
	public CmdSetHealth(Plugin instance){
		plugin = instance;
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.checkArgs(args, 0)){
			if(!r.isPlayer(sender)) return;
			if(!r.perm(sender, "uc.sethealth", false, true)){
				return;
			}
			Player p = (Player) sender;
			p.setHealth(20.0);
			p.sendMessage(r.mes("Sethealth").replaceAll("%Player", p.getName()).replaceAll("%Health", "20"));
		}else if(r.checkArgs(args, 0) && !r.checkArgs(args, 1)){
			if(!r.isPlayer(sender)) return;
			if(r.isDouble(args[0])){
				Double d = Double.parseDouble(args[0]);
				Player p = (Player) sender;
				p.setHealth(r.normalize(d, 0.0, 20.0));
				p.sendMessage(r.mes("Sethealth").replaceAll("%Player", p.getName()).replaceAll("%Health", d + ""));
			}else{
			    sender.sendMessage(r.mes("NumberFormat").replaceAll("%Amount", args[0]));
			}
		}else if(r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.sethealth.others", false, true)){
				return;
			}
			if(r.isDouble(args[0])){
				Double d = Double.parseDouble(args[0]);
				Player t = r.searchPlayer(args[1]);
				if(t == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
					return;
				}
				t.setHealth(r.normalize(d, 0.0, 20.0));
				sender.sendMessage(r.mes("Sethealth").replaceAll("%Player", t.getName()).replaceAll("%Health", args[0]));
			}else if(r.isDouble(args[1])){
				Double d = Double.parseDouble(args[1]);
				Player t = r.searchPlayer(args[0]);
				if(t == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
					return;
				}
				t.setHealth(r.normalize(d, 0.0, 20.0));
				sender.sendMessage(r.mes("Sethealth").replaceAll("%Player", t.getName()).replaceAll("%Health", args[1]));
			}else{
				sender.sendMessage(r.mes("NumberFormat").replaceAll("%Amount", args[0]));
			}
		}
	}
}
