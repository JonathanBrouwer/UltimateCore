package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdSetFood {
	static Plugin plugin;
	public CmdSetFood(Plugin instance){
		plugin = instance;
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.checkArgs(args, 0)){
			if(!r.isPlayer(sender)) return;
			if(!r.perm(sender, "uc.sethunger", false, true)){
				return;
			}
			Player p = (Player) sender;
			p.setFoodLevel(20);
			p.sendMessage(r.mes("Sethunger").replaceAll("%Player", p.getName()).replaceAll("%Food", "20"));
		}else if(r.checkArgs(args, 0) && !r.checkArgs(args, 1)){
			if(!r.isPlayer(sender)) return;
			if(r.isNumber(args[0])){
				Integer d = Integer.parseInt(args[0]);
				Player p = (Player) sender;
				p.setFoodLevel(r.normalize(d, 0, 20));
				p.sendMessage(r.mes("Sethunger").replaceAll("%Player", p.getName()).replaceAll("%Food", d + ""));
			}else{
			    sender.sendMessage(r.mes("NumberFormat").replaceAll("%Amount", args[0]));
			}
		}else if(r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.sethunger.others", false, true)){
				return;
			}
			if(r.isNumber(args[0])){
				Integer d = Integer.parseInt(args[0]);
				Player t = Bukkit.getPlayer(args[1]);
				if(t == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
					return;
				}
				t.setFoodLevel(r.normalize(d, 0, 20));
				sender.sendMessage(r.mes("Sethunger").replaceAll("%Player", t.getName()).replaceAll("%Food", args[0]));
			}else if(r.isNumber(args[1])){
				Integer d = Integer.parseInt(args[1]);
				Player t = Bukkit.getPlayer(args[0]);
				if(t == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
					return;
				}
				t.setFoodLevel(r.normalize(d, 0, 20));
				sender.sendMessage(r.mes("Sethunger").replaceAll("%Player", t.getName()).replaceAll("%Food", args[1]));
			}else{
				sender.sendMessage(r.mes("NumberFormat").replaceAll("%Amount", args[0]));
			}
		}
	}
}
