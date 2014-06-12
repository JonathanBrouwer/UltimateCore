package Bammerbom.UltimateCore.Commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateCommands;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;


public class CmdVanish implements Listener{
	static Plugin plugin;
	public CmdVanish(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		playerUpdateEvent();
	}
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args){
		if(r.checkArgs(args, 0) == false){
			UltimateCommands.executecommand(sender, "vanish " + sender.getName());
			return;
		}
		OfflinePlayer banp = Bukkit.getOfflinePlayer(args[0]);
		if(banp == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		Long time = 0L;
		//Info
		if(r.checkArgs(args, 1) == false){
		}else if(DateUtil.getTimeMillis(args[1]) != -1){
			time = DateUtil.getTimeMillis(args[1]);
		}
		String timen = DateUtil.format(time);
		if(time == 0){
			timen = r.mes("Vanish.Forever");
		}
		//Permcheck
		if(!r.perm(sender, "uc.vanish.time", false, false) && !r.perm(sender, "uc.vanish", false, false) && time == 0L){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		if(!r.perm(sender, "uc.vanish.perm", false, false) && !r.perm(sender, "uc.vanish", false, false) && time != 0L){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		if(!Vanish(banp)){
		    Vanish(banp, true, time);
		    sender.sendMessage(r.mes("Vanish.Vanished").replaceAll("%Player", banp.getName()).replaceAll("%Time", timen));
		    if(banp.isOnline()){
		    	Player banp2 = (Player) banp;
		    banp2.sendMessage(r.mes("Vanish.Vanishtarget"));
		    }
		    return;
		}else{
			Vanish(banp, false, 0L);
			sender.sendMessage(r.mes("Vanish.Unvanished").replaceAll("%Player", banp.getName()));
		    if(banp.isOnline()){
		    	Player banp2 = (Player) banp;
		    banp2.sendMessage(r.mes("Vanish.Unvanishtarget"));
		    }
			return;
		}
	}
	public static boolean Vanish(OfflinePlayer p){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		if(data.get("vanish") == null){
			return false;
		}
		if(vanishgone(p, true)) return true;
		return data.getBoolean("vanish");
	}
	public static boolean vanishgone(OfflinePlayer p, Boolean directreset){
		final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		if(conf.getBoolean("vanish") == false) return false;
		if(conf.getLong("vanishtime") == 0 || conf.getLong("vanish") == -1) return false;
		if(System.currentTimeMillis() >= conf.getLong("vanishtime")){
			if(directreset == true){
				Vanish(p, false, 0L);
			}
			return true;
		}
		return false;
	}
	public static void Vanish(OfflinePlayer p, Boolean set, Long time){
		for(Player pl : Bukkit.getOnlinePlayers()){
			if(p.isOnline()){
				Player p2 = (Player) p;
			if(set == true){
				pl.hidePlayer(p2);
			}else{
				pl.showPlayer(p2);
			}
			}
		}
		
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		data.set("vanish", set);
		data.set("vanishtime", time);
		try {
			data.save(UltimateFileLoader.getPlayerFile(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void playerUpdateEvent(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()){
					if(vanishgone(p, true)){
						p.sendMessage(r.mes("Vanish.Unvanishtarget"));
					}
					}
				
			}
			
		}, 120L, 60L);
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e){
		for(Player pl : Bukkit.getOnlinePlayers()){
			if(Vanish(pl)){
				e.getPlayer().hidePlayer(pl);
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent e){
		for(Player pl : Bukkit.getOnlinePlayers()){
			e.getPlayer().showPlayer(pl);
		}
		if(Vanish(e.getPlayer())){
			Vanish(e.getPlayer(), false, 0L);
			for(Player p : Bukkit.getOnlinePlayers()){
				p.showPlayer(e.getPlayer());
			}
		}
		
			
	}
}
