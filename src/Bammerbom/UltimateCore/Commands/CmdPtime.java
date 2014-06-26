package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdPtime{
	static Plugin plugin;
	public CmdPtime(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
			Player p = (Player) sender;
			if(r.checkArgs(args, 0) == false){
				sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
			}else{
				if("day".equalsIgnoreCase(args[0])){
					if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.day", false, false)){
						sender.sendMessage(r.mes("NoPermissions"));
						return;
					}
					p.setPlayerTime(0, true);
					p.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}
				else if("night".equalsIgnoreCase(args[0])){
					if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.night", false, false)){
						sender.sendMessage(r.mes("NoPermissions"));
						return;
					}
					p.setPlayerTime(14000, true);
					p.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}
				
				else if(isNumber(args[0])){
					if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.ticks", false, false)){
						sender.sendMessage(r.mes("NoPermissions"));
						return;
					}
					Integer time = Integer.parseInt(args[0]);
					p.setPlayerTime(time, true);
					p.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));	
				}else if(args[0].equalsIgnoreCase("add")){
					if(isNumber(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.add", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						Integer time = Integer.parseInt(args[1]);
					    p.setPlayerTime(p.getPlayerTime() + time, true);
						p.sendMessage(r.mes("Time").replaceAll("%Time", p.getPlayerTime() + time + ""));	
					}
				}else if(args[0].equalsIgnoreCase("set")){
					if(!r.checkArgs(args, 1)){
						sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
						return;
					}
					if("day".equalsIgnoreCase(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.day", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						p.setPlayerTime(0, true);
						p.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}
					else if("night".equalsIgnoreCase(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.night", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						p.setPlayerTime(14000, true);
						p.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}
					else if(isNumber(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.ticks", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						Integer time = Integer.parseInt(args[1]);
						p.setPlayerTime(time, true);
						p.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));	
					}else{
						sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
					}
				}
				else{
					sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
				}
		}
	}
	
	
	public static boolean isNumber(String arg) {
	    return getNumber(arg) != null;
	}
	 
	public static Integer getNumber(String s) {
	    try {
	        return Integer.parseInt(s);
	    } catch(Exception e) {
	        return null;
	    }
	}

}