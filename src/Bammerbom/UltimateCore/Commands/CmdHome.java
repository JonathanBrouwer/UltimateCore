package Bammerbom.UltimateCore.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.LocationUtil;

public class CmdHome{
	static Plugin plugin;
	public CmdHome(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void home(CommandSender sender, String label, String[] args){
		if(r.checkArgs(args, 0) == false){
			if(!(r.isPlayer(sender))){
				return;
			}
			Player p = (Player) sender;
			if(r.perm(p, "uc.home", true, true) == false){
				return;
			}
			ArrayList<String> homes = (ArrayList<String>) UltimateFileLoader.getPlayerConfig(p).getList("homeslist");
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
			OfflinePlayer t;
			if(r.perm(p, "uc.home", true, true) == false){
				return;
			}
			if(args[0].contains(":")){
				if(!r.perm(p, "uc.home.others", false, true)) return;
				if(args[0].endsWith(":") || args[0].endsWith(":list")){
					t = Bukkit.getOfflinePlayer(args[0].split(":")[0]);
					if(t == null || (!t.hasPlayedBefore() && !t.isOnline())){
						p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
						return;
					}
					ArrayList<String> homes = (ArrayList<String>) UltimateFileLoader.getPlayerConfig(t).getList("homeslist");
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
					//return;
				}
				t = Bukkit.getOfflinePlayer(args[0].split(":")[0]);
				if(t == null || (!t.hasPlayedBefore() && !t.isOnline())){
					p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
					return;
				}
				List<String> homes = UltimateFileLoader.getPlayerConfig(t).getStringList("homeslist");
				if(!homes.contains(args[0].split(":")[1].toLowerCase())){
					sender.sendMessage(r.mes("Home.HomeNotExist").replaceAll("%Home", args[0].toLowerCase()));
					r.log("C");
					return;
				}
				try{
				//Teleport
				UltimateConfiguration data = UltimateFileLoader.getPlayerConfig(t);
				String[] loc = data.getString("homes." + args[0].toLowerCase().split(":")[1]).split(",");
		        World w = Bukkit.getWorld(loc[0]);
		        Double x = Double.parseDouble(loc[1]);
		        Double y = Double.parseDouble(loc[2]);
		        Double z = Double.parseDouble(loc[3]);
		        float yaw = Float.parseFloat(loc[4]);
		        float pitch = Float.parseFloat(loc[5]);
		        Location location = new Location(w, x, y, z, yaw, pitch);
		        if(r.isPlayer(sender)){
		        	LocationUtil.teleport(p, location, TeleportCause.COMMAND);
		        }
				sender.sendMessage(r.mes("Home.Hometp").replaceAll("%Home", args[0]));
				}catch(Exception ex){
					sender.sendMessage(r.error + "Invalid home: " + args[0]);
					ex.printStackTrace();
				}
				return;
			}
			//Exist TODO
			List<String> homes = UltimateFileLoader.getPlayerConfig(p).getStringList("homeslist");
			if(!homes.contains(args[0].toLowerCase())){
				sender.sendMessage(r.mes("Home.HomeNotExist").replaceAll("%Home", args[0].toLowerCase()));
				return;
			}
			try{
			//Teleport
			UltimateConfiguration data = UltimateFileLoader.getPlayerConfig(p);
			String[] loc = data.getString("homes." + args[0].toLowerCase()).split(",");
	        World w = Bukkit.getWorld(loc[0]);
	        Double x = Double.parseDouble(loc[1]);
	        Double y = Double.parseDouble(loc[2]);
	        Double z = Double.parseDouble(loc[3]);
	        float yaw = Float.parseFloat(loc[4]);
	        float pitch = Float.parseFloat(loc[5]);
	        Location location = new Location(w, x, y, z, yaw, pitch);
	        if(r.isPlayer(sender)){
	        	LocationUtil.teleport(p, location, TeleportCause.COMMAND);
	        }
			sender.sendMessage(r.mes("Home.Hometp").replaceAll("%Home", args[0]));
			}catch(Exception ex){
				sender.sendMessage(r.error + "Invalid home: " + args[0]);
				ex.printStackTrace();
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static void setHome(CommandSender sender, String label, String[] args){
		if(!(r.isPlayer(sender))){
			return;
		}
		Player p = (Player) sender;
		if(r.perm(p, "uc.sethome", true, true) == false){
			return;
		}
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Home.Usage"));
		}else{
			if(args[0].contains(":")){
				if(r.perm(p, "uc.sethome.others", true, true) == false){
					return;
				}
				OfflinePlayer t = Bukkit.getOfflinePlayer(args[0].split(":")[0]);
				if(t == null || (!t.hasPlayedBefore() && !t.isOnline())){
					p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
					return;
				}
				List<String> homes = UltimateFileLoader.getPlayerConfig(t).getStringList("homeslist");
				if(homes.contains(args[0])){
			    	 sender.sendMessage(r.mes("Home.Homemoved").replaceAll("%Home", args[0]));
			    }else{
			        sender.sendMessage(r.mes("Home.Homeset").replaceAll("%Home", args[0]));
			    }
				if(!homes.contains(args[0].toLowerCase().split(":")[1])){
				homes.add(args[0].toLowerCase().split(":")[1]);
				}
				UltimateConfiguration data = UltimateFileLoader.getPlayerConfig(t);
				data.set("homeslist", homes);
				Location loc = p.getLocation();
			    String location = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
			    data.set("homes." + args[0].toLowerCase().split(":")[1], location);
			    data.save(UltimateFileLoader.getPlayerFile(t));
				return;
			}
			List<String> homes = UltimateFileLoader.getPlayerConfig(p).getStringList("homeslist");
			if(homes.contains(args[0])){
		    	 sender.sendMessage(r.mes("Home.Homemoved").replaceAll("%Home", args[0]));
		    }else{
		        sender.sendMessage(r.mes("Home.Homeset").replaceAll("%Home", args[0]));
		    }
			if(!homes.contains(args[0].toLowerCase())){
			homes.add(args[0].toLowerCase());
			}
			UltimateConfiguration data = UltimateFileLoader.getPlayerConfig(p);
			data.set("homeslist", homes);
			Location loc = p.getLocation();
		    String location = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
		    data.set("homes." + args[0].toLowerCase(), location);
		    data.save(UltimateFileLoader.getPlayerFile(p));
		}
	}
	@SuppressWarnings("deprecation")
	public static void delHome(CommandSender sender, String label, String[] args){
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Home.Usage2"));
		}else{
			if(!(r.isPlayer(sender))){
				return;
			}
			Player p = (Player) sender;
			if(r.perm(p, "uc.delhome", true, true) == false){
				return;
			}
			if(args[0].contains(":")){
				OfflinePlayer t = Bukkit.getOfflinePlayer(args[0].split(":")[0]);
				if(t == null || (!t.hasPlayedBefore() && !t.isOnline())){
					p.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
					return;
				}
				List<String> homes = UltimateFileLoader.getPlayerConfig(t).getStringList("homeslist");
				if(!homes.contains(args[0].toLowerCase().split(":")[1])){
					sender.sendMessage(r.mes("Home.HomeNotExist").replaceAll("%Home", args[0].toLowerCase()));
					return;
				}
				UltimateConfiguration data = UltimateFileLoader.getPlayerConfig(t);
			    data.set("homes." + args[0].toLowerCase().split(":")[1], null);
			    homes.remove(args[0].toLowerCase().split(":")[1]);
			    data.set("homeslist", homes);
			    sender.sendMessage(r.mes("Home.HomeDeleted").replaceAll("%Home", args[0]));
			    data.save(UltimateFileLoader.getPlayerFile(t));
			    return;
			}
			List<String> homes = UltimateFileLoader.getPlayerConfig(p).getStringList("homeslist");
			if(!homes.contains(args[0].toLowerCase())){
				sender.sendMessage(r.mes("Home.HomeNotExist").replaceAll("%Home", args[0].toLowerCase()));
				return;
			}
			UltimateConfiguration data = UltimateFileLoader.getPlayerConfig(p);
		    data.set("homes." + args[0].toLowerCase(), null);
		    homes.remove(args[0].toLowerCase());
		    data.set("homeslist", homes);
		    sender.sendMessage(r.mes("Home.HomeDeleted").replaceAll("%Home", args[0]));
		    data.save(UltimateFileLoader.getPlayerFile(p));
		}
	}
}
