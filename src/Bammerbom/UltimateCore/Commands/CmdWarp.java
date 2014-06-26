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

public class CmdWarp{
	static Plugin plugin;
	public CmdWarp(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void warp(CommandSender sender, String label, String[] args){
		if(r.checkArgs(args, 0) == false){
			if(r.perm(sender, "uc.warplist", true, true) == false){
				return;
			}
			ArrayList<String> warps = (ArrayList<String>) YamlConfiguration.loadConfiguration(UltimateFileLoader.DFwarps).getStringList("warpslist");
			String iets = "";
			Integer hoeveel = 0;
			try{
				Integer amount = warps.toArray().length;
				for(int i = 0; i < amount; i++){
					iets = iets + warps.get(hoeveel) + ", ";
					hoeveel++;
					
				}
				iets = iets.substring(0, iets.length()-1);
				iets = iets.substring(0, iets.length()-1);
			}
			catch(IndexOutOfBoundsException ex){
			}
			if(iets.equalsIgnoreCase("") || iets.equalsIgnoreCase(null)){
				sender.sendMessage(r.mes("Warp.NoWarpsFound"));
				return;
			}else{
				 sender.sendMessage(r.mes("Warp.Warps").replaceAll("%Warps", iets));
				 return;
			}
		   
		}else{
			if(!(r.isPlayer(sender))){
				return;
			}
			//Exist
			List<String> warps = YamlConfiguration.loadConfiguration(UltimateFileLoader.DFwarps).getStringList("warpslist");
			Player p = (Player) sender;
			if(r.perm(p, "uc.warp", true, false) == false && r.perm(p, "uc.warp." + args[0], true, false) == false){
				sender.sendMessage(r.mes("NoPermissions"));
				return;
			}
			if(!warps.contains(args[0].toLowerCase())){
				sender.sendMessage(r.mes("Warp.WarpNotExist").replaceAll("%Warp", args[0].toLowerCase()));
				return;
			}
			
			//Teleport
			YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.DFwarps);
			String[] loc = data.getString("warps." + args[0].toLowerCase()).split(",");
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
			sender.sendMessage(r.mes("Warp.Warped").replaceAll("%Warp", args[0]));
			
			
		}
	}
	public static void setWarp(CommandSender sender, String label, String[] args){
		if(!(r.isPlayer(sender))){
			return;
		}
		Player p = (Player) sender;
		if(r.perm(p, "uc.setwarp", false, true) == false){
			return;
		}
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.default1 + "/setwarp <Name>");
		}else{
			
			List<String> warps = YamlConfiguration.loadConfiguration(UltimateFileLoader.DFwarps).getStringList("warpslist");
			if(warps.contains(args[0])){
		    	 sender.sendMessage(r.mes("Warp.Warpmoved").replaceAll("%Warp", args[0]));
		    }else{
		        sender.sendMessage(r.mes("Warp.Warpset").replaceAll("%Warp", args[0]));
		    }
			if(!warps.contains(args[0].toLowerCase())){
			warps.add(args[0].toLowerCase());
			}
			YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.DFwarps);
			data.set("warpslist", warps);
			Location loc = p.getLocation();
		    String location = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
		    data.set("warps." + args[0].toLowerCase(), location);
		    try {
				 data.save(UltimateFileLoader.DFwarps);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void delWarp(CommandSender sender, String label, String[] args){
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.default1 + "/delwarp <Name>");
		}else{
			if(r.perm(sender, "uc.delwarp", false, true) == false){
				return;
			}
			List<String> warps = YamlConfiguration.loadConfiguration(UltimateFileLoader.DFwarps).getStringList("warpslist");
			if(!warps.contains(args[0].toLowerCase())){
				sender.sendMessage(r.mes("Warp.WarpNotExist").replaceAll("%Warp", args[0].toLowerCase()));
				return;
			}
			YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.DFwarps);
		    data.set("warps." + args[0].toLowerCase(), null);
		    warps.remove(args[0].toLowerCase());
		    data.set("warpslist", warps);
		    sender.sendMessage(r.mes("Warp.WarpDeleted").replaceAll("%Warp", args[0]));
		    try {
				data.save(UltimateFileLoader.DFwarps);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
