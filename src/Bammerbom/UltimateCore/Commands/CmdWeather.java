package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Events.EventWeather;

public class CmdWeather implements Listener{
	static Plugin plugin;
	public CmdWeather(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void handle(CommandSender sender, String[] args){
		CommandSender p = sender;
		if(checkArgs(args, 0) == false){
			p.sendMessage(r.default1 + "/weather sun/rain/storm");
			return;
		}else{
			EventWeather.setLast(1);
		Integer weather = 0;
		if(!r.isPlayer(sender, false)){
		for(World world : Bukkit.getWorlds()){
			if("sun".equalsIgnoreCase(args[0]) || "clear".equalsIgnoreCase(args[0])){
				if(r.perm(p, "uc.weather", false, false) == false && r.perm(p, "uc.weather.sun", false, false) == false){
					p.sendMessage(r.mes("NoPermissions"));
					return;
				}
				world.setStorm(false);
				world.setThundering(false);
				weather = 1;
			}else if("rain".equalsIgnoreCase(args[0])){
				if(r.perm(p, "uc.weather", false, false) == false && r.perm(p, "uc.weather.rain", false, false) == false){
					p.sendMessage(r.mes("NoPermissions"));
					return;
				}
				world.setStorm(true);
				world.setThundering(false);
				weather = 2;
			}else if("storm".equalsIgnoreCase(args[0]) || "thunder".equalsIgnoreCase(args[0])){
				if(r.perm(p, "uc.weather", false, false) == false && r.perm(p, "uc.weather.storm", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				world.setStorm(true);
				world.setThundering(true);
				weather = 3;
			}
		}
		}else{
			World world = ((Player) sender).getWorld();
			if("sun".equalsIgnoreCase(args[0]) || "clear".equalsIgnoreCase(args[0])){
				if(r.perm(p, "uc.weather", false, false) == false && r.perm(p, "uc.weather.sun", false, false) == false){
					p.sendMessage(r.mes("NoPermissions"));
					return;
				}
				world.setStorm(false);
				world.setThundering(false);
				weather = 1;
			}else if("rain".equalsIgnoreCase(args[0])){
				if(r.perm(p, "uc.weather", false, false) == false && r.perm(p, "uc.weather.rain", false, false) == false){
					p.sendMessage(r.mes("NoPermissions"));
					return;
				}
				world.setStorm(true);
				world.setThundering(false);
				weather = 2;
			}else if("storm".equalsIgnoreCase(args[0]) || "thunder".equalsIgnoreCase(args[0])){
				if(r.perm(p, "uc.weather", false, false) == false && r.perm(p, "uc.weather.storm", false, false) == false){
					sender.sendMessage(r.mes("NoPermissions"));
					return;
				}
				world.setStorm(true);
				world.setThundering(true);
				weather = 3;
			}
		}
		EventWeather.setLast(0);
		if(weather == 1){
			p.sendMessage(r.mes("Weather").replaceAll("%Weather", "sun"));
		}else if(weather == 2){
			p.sendMessage(r.mes("Weather").replaceAll("%Weather", "rain"));
		}else if(weather == 3){
			p.sendMessage(r.mes("Weather").replaceAll("%Weather", "storm"));
		}else{
			p.sendMessage(r.default1 + "/weather sun/rain/storm");
		}
		}
    }
	public static boolean checkArgs(String[] args, Integer number){
		boolean error = true;
		try{
			if(args[number].contains(" ")){
				
			}
		}catch(ArrayIndexOutOfBoundsException e){
			error = false;
		}
		if(error == true){ return true; }else{ return false; }
		
	}

}

