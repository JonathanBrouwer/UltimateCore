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
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Events.EventActionMessage;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;

public class CmdBan implements Listener{
	static Plugin plugin;
	public CmdBan(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings("deprecation")
	public static void ban(CommandSender sender, String[] args) {
		if(!r.perm(sender, "uc.ban", false, true)) return;
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Ban.Usage"));
			return;
		}
		OfflinePlayer banp = Bukkit.getOfflinePlayer(args[0]);
		if(banp == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		if(banp.isOnline()){
			Player p = banp.getPlayer();
			if(p.hasPermission("uc.antiban")){
				sender.sendMessage(r.mes("AntiBan").replaceAll("%Player", p.getName()).replaceAll("%Action", "ban"));
				return;
			}
		}
		Long time = 0L;
		String reason = r.mes("Ban.StandardReason");
		//Info
		if(r.checkArgs(args, 1) == false){
		}else if(DateUtil.getTimeMillis(args[1]) == -1){
			reason = r.getFinalArg(args, 1);
		}else if(DateUtil.getTimeMillis(args[1]) != -1){
			time = DateUtil.getTimeMillis(args[1]);
			if(r.checkArgs(args, 2) == true){
				reason = r.getFinalArg(args, 2);
			}
		}
		String timen = DateUtil.format(time);
		if(time == 0){
			timen = r.mes("Mute.Forever");
		}else{
			timen = "" + timen;
		}
		//Permcheck
		if(!r.perm(sender, "uc.ban.time", false, false) && !r.perm(sender, "uc.ban", false, false) && time == 0L){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		if(!r.perm(sender, "uc.ban.perm", false, false) && !r.perm(sender, "uc.ban", false, false) && time != 0L){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		//Ban
		String reas = r.mes("Ban.Message").replaceAll("%Time", timen).replaceAll("%Reason", reason);
		if(banp.isOnline()){
			EventActionMessage.setEnb(false);
			Bukkit.getPlayer(banp.getName()).kickPlayer(reas);
			EventActionMessage.setEnb(true);
		}
		//pconf
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(banp));
		conf.set("banned", true);
		conf.set("banreason", reason);
		conf.set("bantime", time);
		if(time == 0){
			conf.set("bantime", -1L);
		}
		try {
			conf.save(UltimateFileLoader.getPlayerFile(banp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.broadcastMessage(r.mes("Ban.Broadcast").replaceAll("%Banner", sender.getName()).replaceAll("%Banned", banp.getName()).replaceAll("%Time", timen).replaceAll("%Reason", reason));
		
		
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void joinOnBan(final PlayerJoinEvent e){
		 final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(e.getPlayer()));
		 if(conf.get("banned") == null){ return; }
		 if(!conf.getBoolean("banned") == true) return;
		 if(conf.get("bantime") != null && conf.getLong("bantime") < 1 ){
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 
					new Runnable(){

						@Override
						public void run() {
							EventActionMessage.setEnb(false);
							e.getPlayer().kickPlayer(
									r.mes("Ban.Message")
									.replaceAll("%Time", r.mes("Ban.TimeForever"))
									.replaceAll("%Reason", conf.getString("banreason")));
							EventActionMessage.setEnb(true);
							e.setJoinMessage(null);
						}
				
			}, 3L);
			return;
		}
		if(conf.get("banned") != null && conf.getBoolean("banned") == true){
			if(System.currentTimeMillis() >= conf.getLong("bantime")){
				e.getPlayer().sendMessage(r.mes("Ban.joinAfterUnban"));
				conf.set("banned", false);
				conf.set("banreason", null);
				conf.set("bantime", null);
				try {
					conf.save(UltimateFileLoader.getPlayerFile(e.getPlayer()));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return;
			}else{
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 
						new Runnable(){

							@Override
							public void run() {
								Long l = conf.getLong("bantime");
								EventActionMessage.setEnb(false);
								e.getPlayer().kickPlayer(r.mes("Ban.Message").replaceAll("%Time", DateUtil.format(l)).replaceAll("%Reason", conf.getString("banreason")));
								EventActionMessage.setEnb(true);
								e.setJoinMessage(null);
							}
					
				}, 3L);
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static void unban(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.unban", false, true)){ return; }
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Usage2"));
			return;
		}
		OfflinePlayer banp = Bukkit.getOfflinePlayer(args[0]);
		if(banp == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(banp));
		conf.set("banned", false);
		conf.set("banreason", null);
		conf.set("bantime", null);
		try {
			conf.save(UltimateFileLoader.getPlayerFile(banp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.broadcastMessage(r.mes("Ban.BroadcastUnban").replaceAll("%Unbanner", sender.getName()).replaceAll("%Banned", banp.getName()));
	}
    
}
