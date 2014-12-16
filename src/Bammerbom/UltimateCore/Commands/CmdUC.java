package Bammerbom.UltimateCore.Commands;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.UltimateUpdater;
import Bammerbom.UltimateCore.UltimateUpdater.UpdateType;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;
import Bammerbom.UltimateCore.Resources.Utils.LocationUtil;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IJails;
import com.earth2me.essentials.api.IWarps;

public class CmdUC{
	static Plugin plugin;
	static File file;
	public CmdUC(Plugin instance, File fil){
		plugin = instance;
		file = fil;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	public static void core(final CommandSender sender, String[] args){
		if(args.length == 0){
			if(!r.perm(sender, "uc.menu", false, true)) return;
			sender.sendMessage(r.default1 + "-----------------------------------------");
			sender.sendMessage(r.default2 + "               UltimateCore Menu");
			sender.sendMessage(r.default1 + "-----------------------------------------");
			sender.sendMessage(r.default1 + "UltimateCore commands:");
			sender.sendMessage(r.default1 + "/uc reload  " + r.default2 + "> Reload Ultimate Core");
			sender.sendMessage(r.default1 + "/uc credits " + r.default2 + "> Credits of Ultimate Core");
			sender.sendMessage(r.default1 + "/uc disable " + r.default2 + "> Disable Ultimate Core (If chrashed)");
			sender.sendMessage(r.default1 + "/uc version " + r.default2 + "> Get your, and the newest version of UltimateCore");
			sender.sendMessage(r.default1 + "/uc update  " + r.default2 + "> Update UltimateCore to the newest version.");
			sender.sendMessage(r.default1 + "/uc convert  " + r.default2 + "> Copy data from Essentials.");
		}else if(args[0].equalsIgnoreCase("reload")){
			if(!r.perm(sender, "uc.menu.reload", false, true)) return;
			Plugin uc = plugin;
			Bukkit.getPluginManager().disablePlugin(uc);
			System.gc();
			Bukkit.getPluginManager().enablePlugin(uc);
			sender.sendMessage(r.default1 + "UltimateCore has been reloaded!");
		}else if(args[0].equalsIgnoreCase("disable")){
			if(!r.perm(sender, "uc.menu.disable", false, true)) return;
			Plugin core = plugin.getServer().getPluginManager().getPlugin(plugin.getName());
			plugin.getServer().getPluginManager().disablePlugin(core);
			sender.sendMessage(r.default1 + "UltimateCore has been disabled! :(");
		}else if(args[0].equalsIgnoreCase("credits")){
			if(!r.perm(sender, "uc.menu", false, true)) return;
			sender.sendMessage(r.default1 + "Credits:");
			sender.sendMessage(r.default1 + "Owner: " + r.default2 + "Jhtzb");
			sender.sendMessage(r.default1 + "Test/Translate: " + r.default2 + "Blockbreaker21");
		}else if(args[0].equalsIgnoreCase("noreturn")){
			return;
		}else if(args[0].equalsIgnoreCase("version")){
			if(!r.perm(sender, "uc.menu.version", false, true)) return;
			sender.sendMessage(r.default1 + "Your version of UltimateCore: " + r.default2 + plugin.getDescription().getVersion());
			sender.sendMessage(r.default1 + "Newest version of UltimateCore: " + r.default2 + UltimateUpdater.getLatestUpdate());
		}else if(args[0].equalsIgnoreCase("update")){
			if(!r.perm(sender, "uc.menu.updater", false, true)) return;
			if(r.getCnfg().getBoolean("Updater.check")){
				//final PluginUtil util = new PluginUtil();
				final Thread thr = new Thread(new Runnable(){
					@Override
					public void run() {
						UltimateUpdater up = new UltimateUpdater(plugin, 66979, file, UpdateType.DEFAULT, true);
						UltimateUpdater.waitForThread();
						switch(up.getResult()){
						case DISABLED:
							break;
						case FAIL_APIKEY:
							break;
						case FAIL_BADID:
							break;
						case FAIL_DBO:
							break;
						case FAIL_DOWNLOAD:
							sender.sendMessage(r.default1 + "Failed to download update.");
							break;
						case FAIL_NOVERSION:
							break;
						case NO_UPDATE:
							sender.sendMessage(r.default1 + "No update available.");
							break;
						case SUCCESS:
							if(!(sender instanceof ConsoleCommandSender)){
							    sender.sendMessage(r.default1 + "Update downloaded succesfull.");
							}
							break;
						case UPDATE_AVAILABLE:
							break;
						default:
							break;
						}
					}
				});
				thr.setName("UC Updater (Finishing thread)");
				thr.start();
			}else{
				sender.sendMessage(r.default1 + "The updater is disabled.");
			}
		}else if(args[0].equalsIgnoreCase("convert")){
			if(!r.perm(sender, "uc.updater.convert", false, true)) return;
			sender.sendMessage(r.default1 + "Converting from Essentials...");
			if(!Bukkit.getPluginManager().isPluginEnabled("Essentials")){
				sender.sendMessage(r.default1 + "Converting Failed: Essentials is not installed.");
				return;
			}
			IEssentials es = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
			try{
				r.log("Importing Jails...");
			    IJails j = es.getJails();
			    UltimateConfiguration conf = new UltimateConfiguration(UltimateFileLoader.DFjails);
			    for(String s : j.getList()){
			    	conf.set("Jails." + s, LocationUtil.convertLocationToString(j.getJail(s)));
			    }
			    conf.save(UltimateFileLoader.DFjails);
			    
			    r.log("Importing Warps...");
			    UltimateConfiguration conf2 = new UltimateConfiguration(UltimateFileLoader.DFwarps);
			    IWarps w = es.getWarps();
			    List<String> warps = conf2.getStringList("warpslist");
			    for(String s : w.getList()){
			    	warps.add(s.toLowerCase());
			    	Location loc = w.getWarp(s);
			    	conf2.set("warps." + s.toLowerCase(), loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch());
			    }
			    conf2.set("warpslist", warps);
			    conf2.save();
			    
			    r.log("Importing Player Data...");
			    for(org.bukkit.OfflinePlayer pl : r.getOfflinePlayers()){
			    	User u2 = es.getOfflineUser(pl.getName());
			    	if(u2 == null){
			    		r.log("Failed to import player data of " + pl.getName());
			    		continue;
			    	}
		    		UCplayer uc = UC.getPlayer(pl);
			    	UltimateConfiguration data = UltimateFileLoader.getPlayerConfig(pl);
			    	List<String> homes = data.getStringList("homeslist");
			    	for(String s : u2.getHomes()){
			    		homes.add(s);
			    		Location loc = u2.getHome(s);
			    		data.set("homes." + s.toLowerCase(), loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch());
			    	}
			    	data.set("homeslist", homes);
			    	data.save();
			    	//Jail
			    	if(u2.getJail() != null){
			    		uc.setJailed(true, u2.getJail(), u2.getJailTimeout());
			    	}
			    	uc.setNick(u2.getNickname());
			    }
			    sender.sendMessage(r.default1 + "Converting complete!");
			}catch(Exception ex){
				ex.printStackTrace(); 
			}
		}else{
			if(!r.perm(sender, "uc.menu", false, true)) return;
			sender.sendMessage(r.default1 + "-----------------------------------------");
			sender.sendMessage(r.default2 + "               UltimateCore Menu");
			sender.sendMessage(r.default1 + "-----------------------------------------");
			sender.sendMessage(r.default1 + "UltimateCore commands:");
			sender.sendMessage(r.default1 + "/uc reload  " + r.default2 + "> Reload Ultimate Core");
			sender.sendMessage(r.default1 + "/uc credits " + r.default2 + "> Credits of Ultimate Core");
			sender.sendMessage(r.default1 + "/uc disable " + r.default2 + "> Disable Ultimate Core (If chrashed)");
			sender.sendMessage(r.default1 + "/uc version " + r.default2 + "> Get your, and the newest version of UltimateCore");
			sender.sendMessage(r.default1 + "/uc update  " + r.default2 + "> Update UltimateCore to the newest version.");
			sender.sendMessage(r.default1 + "/uc convert  " + r.default2 + "> Copy data from Essentials.");
		}
	}

}
