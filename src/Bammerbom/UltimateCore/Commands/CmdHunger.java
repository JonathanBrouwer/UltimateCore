package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdHunger {
	static Plugin plugin;
	public CmdHunger(Plugin instance){
		plugin = instance;
	}
	public static void handle(CommandSender sender, String[] args){
		if(!r.checkArgs(args, 0)){
			if(!r.isPlayer(sender)) return;
			if(!r.perm(sender, "uc.hunger", false, true)){
				return;
			}
			sender.sendMessage(r.default1 + "/hunger " + r.default2 + "<Amount>");
		}else if(r.checkArgs(args, 0) && !r.checkArgs(args, 1)){
			if(!r.isPlayer(sender)) return;
			if(!r.perm(sender, "uc.hunger", false, true)){
				return;
			}
			if(r.isNumber(args[0])){
				Integer d = Integer.parseInt(args[0]);
				Player p = (Player) sender;
				p.setFoodLevel(p.getFoodLevel() - r.normalize(d, 0, 20));
				p.sendMessage(r.mes("Hunger").replaceAll("%Player", p.getName()).replaceAll("%Hunger", args[0]));
			}else{
			    sender.sendMessage(r.mes("NumberFormat").replaceAll("%Amount", args[0]));
			}
		}else if(r.checkArgs(args, 1)){
			if(!r.perm(sender, "uc.hunger.others", false, true)){
				return;
			}
			if(r.isNumber(args[0])){
				Integer d = Integer.parseInt(args[0]);
				Player t = Bukkit.getPlayer(args[1]);
				if(t == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[1]));
					return;
				}
				t.setFoodLevel(t.getFoodLevel() - r.normalize(d, 0, 20));
				sender.sendMessage(r.mes("Hunger").replaceAll("%Player", t.getName()).replaceAll("%Hunger", args[0]));
			}else if(r.isNumber(args[1])){
				Integer d = Integer.parseInt(args[1]);
				Player t = Bukkit.getPlayer(args[0]);
				if(t == null){
					sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
					return;
				}
				t.setFoodLevel(t.getFoodLevel() - r.normalize(d, 0, 20));
				sender.sendMessage(r.mes("Hunger").replaceAll("%Player", t.getName()).replaceAll("%Hunger", args[1]));
			}else{
				sender.sendMessage(r.mes("NumberFormat").replaceAll("%Amount", args[0]));
			}
		}
	}
}
