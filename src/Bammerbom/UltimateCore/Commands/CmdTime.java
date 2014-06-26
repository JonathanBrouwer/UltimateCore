package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;

public class CmdTime{
	static Plugin plugin;
	public CmdTime(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		if(!(sender instanceof Player)){
			Boolean perm = true;
			for(World w : Bukkit.getWorlds()){
				if(r.checkArgs(args, 0) == false){
					sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
					return;
				}else{
					if(!r.checkArgs(args, 1)){
						sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
						return;
					}
					if("day".equalsIgnoreCase(args[0])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.day", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						w.setTime(0);
					
					}
					else if("night".equalsIgnoreCase(args[0])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.night", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						w.setTime(14000);
					
					}else if("disable".equalsIgnoreCase(args[0])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.disabe", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						w.setGameRuleValue("doDaylightCycle", "false");
					}else if("enable".equalsIgnoreCase(args[0])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.enable", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						w.setGameRuleValue("doDaylightCycle", "true");
					}
					else if(isNumber(args[0])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.ticks", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						Integer time = Integer.parseInt(args[0]);
						w.setTime(time);
					}else if(args[0].equalsIgnoreCase("add")){
						if(isNumber(args[1])){
							if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.add", false, false)){
								sender.sendMessage(r.mes("NoPermissions"));
								return;
							}
							Integer time = Integer.parseInt(args[1]);
							w.setTime(w.getTime() + time);
						}
					}
					else if(args[0].equalsIgnoreCase("set")){
						if("day".equalsIgnoreCase(args[1])){
							if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.day", false, false)){
								sender.sendMessage(r.mes("NoPermissions"));
								return;
							}
							w.setTime(0);
						
						}
						else if("night".equalsIgnoreCase(args[1])){
							if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.night", false, false)){
								sender.sendMessage(r.mes("NoPermissions"));
								return;
							}
							w.setTime(14000);
						
						}else if("disable".equalsIgnoreCase(args[1])){
							if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.disabe", false, false)){
								sender.sendMessage(r.mes("NoPermissions"));
								return;
							}
							w.setGameRuleValue("doDaylightCycle", "false");
						}else if("enable".equalsIgnoreCase(args[1])){
							if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.enable", false, false)){
								sender.sendMessage(r.mes("NoPermissions"));
								return;
							}
							w.setGameRuleValue("doDaylightCycle", "true");
						}
						else if(isNumber(args[1])){
							if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.ticks", false, false)){
								sender.sendMessage(r.mes("NoPermissions"));
								return;
							}
							Integer time = Integer.parseInt(args[1]);
							w.setTime(time);
						}
					}
				}
			}
			
			if(r.checkArgs(args, 0) == false){
				sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
				return;
			}else{
				if(perm == false){
					return;
				}
				if("day".equalsIgnoreCase(args[0])){
					sender.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}
				else if("night".equalsIgnoreCase(args[0])){
					
					sender.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}else if("disable".equalsIgnoreCase(args[0])){
					sender.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}else if("enable".equalsIgnoreCase(args[0])){
					sender.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}
				else if(isNumber(args[0])){
					sender.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));	
				}else if(args[0].equalsIgnoreCase("add")){
					if(isNumber(args[1])){
						sender.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));	
					}
				}else if(args[0].equalsIgnoreCase("set")){
					if("day".equalsIgnoreCase(args[1])){
						sender.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}
					else if("night".equalsIgnoreCase(args[1])){
						
						sender.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}else if("disable".equalsIgnoreCase(args[1])){
						sender.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}else if("enable".equalsIgnoreCase(args[1])){
						sender.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}
					else if(isNumber(args[1])){
						sender.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));	
					}else{
						sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
					}
				}
				else{
					sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
				}
			}
		}else{
			
			Player p = (Player) sender;
			World world = p.getWorld();
			if(r.checkArgs(args, 0) == false){
				sender.sendMessage(r.default1 + "/time (set) day/night/ticks/disable/enable");
			}else{
				if("day".equalsIgnoreCase(args[0])){
					if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.day", false, false)){
						sender.sendMessage(r.mes("NoPermissions"));
						return;
					}
					world.setTime(0);
					p.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}
				else if("night".equalsIgnoreCase(args[0])){
					if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.night", false, false)){
						sender.sendMessage(r.mes("NoPermissions"));
						return;
					}
					world.setTime(14000);
					p.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}else if("disable".equalsIgnoreCase(args[0])){
					if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.disabe", false, false)){
						sender.sendMessage(r.mes("NoPermissions"));
						return;
					}
					world.setGameRuleValue("doDaylightCycle", "false");
					sender.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}else if("enable".equalsIgnoreCase(args[0])){
					if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.enable", false, false)){
						sender.sendMessage(r.mes("NoPermissions"));
						return;
					}
					world.setGameRuleValue("doDaylightCycle", "true");
					sender.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));
				}
				
				else if(isNumber(args[0])){
					if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.ticks", false, false)){
						sender.sendMessage(r.mes("NoPermissions"));
						return;
					}
					Integer time = Integer.parseInt(args[0]);
					world.setTime(time);
					p.sendMessage(r.mes("Time").replaceAll("%Time", args[0]));	
				}else if(args[0].equalsIgnoreCase("add")){
					if(isNumber(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.add", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						Integer time = Integer.parseInt(args[1]);
						world.setTime(world.getTime() + time);
						p.sendMessage(r.mes("Time").replaceAll("%Time", world.getTime() + time + ""));	
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
						world.setTime(0);
						p.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}
					else if("night".equalsIgnoreCase(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.night", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						world.setTime(14000);
						p.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}else if("disable".equalsIgnoreCase(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.disabe", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						world.setGameRuleValue("doDaylightCycle", "false");
						sender.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}else if("enable".equalsIgnoreCase(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.enable", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						world.setGameRuleValue("doDaylightCycle", "true");
						sender.sendMessage(r.mes("Time").replaceAll("%Time", args[1]));
					}
					
					else if(isNumber(args[1])){
						if(!r.perm(sender, "uc.time", false, false) && !r.perm(sender, "uc.time.ticks", false, false)){
							sender.sendMessage(r.mes("NoPermissions"));
							return;
						}
						Integer time = Integer.parseInt(args[1]);
						world.setTime(time);
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