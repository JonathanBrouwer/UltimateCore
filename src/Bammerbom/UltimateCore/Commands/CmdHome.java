package Bammerbom.UltimateCore.Commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class CmdHome{
	static Plugin plugin;
	public CmdHome(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void home(CommandSender sender, String label, String[] args){
		if(r.checkArgs(args, 0) == false){
			if(!(r.isPlayer(sender))){
				return;
			}
			Player p = (Player) sender;
			if(r.perm(p, "uc.home", true, true) == false){
				return;
			}
			ArrayList<String> homes = (ArrayList<String>) UltimateFileLoader.getPlayerConfig(p).getStringList("homeslist");
			String iets = "";
			Integer hoeveel = 0;
			try{
				Integer amount = homes.toArray().length;
				for(int i = 0; i < amount; i++){
					iets = iets + homes.get(hoeveel) + ", ";
					hoeveel++;
					
				}
				iets = iets.substring(0, iets.length()-1);
				iets = iets.substring(0, iets.length()-1);
			}
			catch(IndexOutOfBoundsException ex){
			}
			if(iets.equalsIgnoreCase("") || iets.equalsIgnoreCase(null)){
				sender.sendMessage(r.mes("Home.NoHomesFound"));
				return;
			}else{
				 sender.sendMessage(r.mes("Home.Homes").replaceAll("%Homes", iets));
				 return;
			}
		   
		}else{
			if(!(r.isPlayer(sender))){
				return;
			}
			Player p = (Player) sender;
			if(r.perm(p, "uc.home", true, true) == false){
				return;
			}
			//Exist
			List<String> homes = UltimateFileLoader.getPlayerConfig(p).getStringList("homeslist");
			if(!homes.contains(args[0].toLowerCase())){
				sender.sendMessage(r.mes("Home.HomeNotExist").replaceAll("%Home", args[0].toLowerCase()));
				return;
			}
			
			//Teleport
			YamlConfiguration data = UltimateFileLoader.getPlayerConfig(p);
			String[] loc = data.getString("homes." + args[0].toLowerCase()).split(",");
	        World w = Bukkit.getWorld(loc[0]);
	        Double x = Double.parseDouble(loc[1]);
	        Double y = Double.parseDouble(loc[2]);
	        Double z = Double.parseDouble(loc[3]);
	        float yaw = Float.parseFloat(loc[4]);
	        float pitch = Float.parseFloat(loc[5]);
	        Location location = new Location(w, x, y, z, yaw, pitch);
	        if(r.isPlayer(sender)){
	        	p.teleport(location);
	        }
			sender.sendMessage(r.mes("Home.Hometp").replaceAll("%Home", args[0]));
			
			
		}
	}
	public static void setHome(CommandSender sender, String label, String[] args){
		if(!(r.isPlayer(sender))){
			return;
		}
		Player p = (Player) sender;
		if(r.perm(p, "uc.home", true, true) == false){
			return;
		}
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Usage"));
		}else{
			
			List<String> homes = UltimateFileLoader.getPlayerConfig(p).getStringList("homeslist");
			if(homes.contains(args[0])){
		    	 sender.sendMessage(r.mes("Home.Homemoved").replaceAll("%Home", args[0]));
		    }else{
		        sender.sendMessage(r.mes("Home.Homeset").replaceAll("%Home", args[0]));
		    }
			if(!homes.contains(args[0].toLowerCase())){
			homes.add(args[0].toLowerCase());
			}
			YamlConfiguration data = UltimateFileLoader.getPlayerConfig(p);
			data.set("homeslist", homes);
			Location loc = p.getLocation();
		    String location = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
		    data.set("homes." + args[0].toLowerCase(), location);
		    try {
				 data.save(UltimateFileLoader.getPlayerFile(p));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void delHome(CommandSender sender, String label, String[] args){
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Usage2"));
		}else{
			if(!(r.isPlayer(sender))){
				return;
			}
			Player p = (Player) sender;
			if(r.perm(p, "uc.home", true, true) == false){
				return;
			}
			List<String> homes = UltimateFileLoader.getPlayerConfig(p).getStringList("homeslist");
			if(!homes.contains(args[0].toLowerCase())){
				sender.sendMessage(r.mes("Home.HomeNotExist").replaceAll("%Home", args[0].toLowerCase()));
				return;
			}
			YamlConfiguration data = UltimateFileLoader.getPlayerConfig(p);
		    data.set("homes." + args[0].toLowerCase(), null);
		    homes.remove(args[0].toLowerCase());
		    data.set("homeslist", homes);
		    sender.sendMessage(r.mes("Home.HomeDeleted").replaceAll("%Home", args[0]));
		    try {
				data.save(UltimateFileLoader.getPlayerFile(p));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
