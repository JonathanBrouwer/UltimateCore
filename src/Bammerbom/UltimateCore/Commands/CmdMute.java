package Bammerbom.UltimateCore.Commands;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;

public class CmdMute implements Listener{
	static Plugin plugin;
	public CmdMute(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		playerUpdateEvent();
	}
	@SuppressWarnings({ "unused", "deprecation" })
	public static void handle(CommandSender sender, String[] args) {
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Mute.Usage"));
			return;
		}
		OfflinePlayer banp = Bukkit.getOfflinePlayer(args[0]);
		if(banp == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		if(banp.isOnline()){
		if(banp.getPlayer().hasPermission("uc.antiban")){
			sender.sendMessage(r.mes("AntiBan").replaceAll("%Player", banp.getPlayer().getName()).replaceAll("%Action", "mute"));
			return;
		}
		}
		Long time = 0L;
		//Info
		if(r.checkArgs(args, 1) == false){
		}else if(DateUtil.getTimeMillis(args[1]) != -1){
			time = DateUtil.getTimeMillis(args[1]);
		}
		String timen = DateUtil.format(time);
		if(time == 0){
			timen = r.mes("Mute.Forever");
		}
		//Permcheck
		if(!r.perm(sender, "uc.mute.time", false, false) && !r.perm(sender, "uc.mute", false, false) && time == 0L){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		if(!r.perm(sender, "uc.mute.perm", false, false) && !r.perm(sender, "uc.mute", false, false) && time != 0L){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		    Mute(banp, true, time);
		    sender.sendMessage(r.mes("Mute.Muted").replaceAll("%Player", banp.getName()));
		    if(banp.isOnline()){
		    	Player banp2 = (Player) banp;
		    banp2.sendMessage(r.mes("Mute.Mutetarget"));
		    }
		    return;
		
	}
	@SuppressWarnings("deprecation")
	public static void unmute(CommandSender sender, String[] args){
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Mute.Usage2"));
			return;
		}
		OfflinePlayer banp = Bukkit.getOfflinePlayer(args[0]);
		if(banp == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		if(!r.perm(sender, "uc.mute.perm", false, false) && !r.perm(sender, "uc.mute", false, false) && !r.perm(sender, "uc.mute.time", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		Mute(banp, false, 0L);
		sender.sendMessage(r.mes("Mute.Unmuted").replaceAll("%Player", banp.getName()));
	    if(banp.isOnline()){
	    	Player banp2 = (Player) banp;
	    banp2.sendMessage(r.mes("Mute.Unmutetarget"));
	    }
		
	}
	public static boolean Mute(OfflinePlayer p){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		if(data.get("mute") == null){
			return false;
		}
		return data.getBoolean("mute");
	}
	public static void Mute(OfflinePlayer p, Boolean set, Long time){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		data.set("mute", set);
		data.set("mutetime", time);
		try {
			data.save(UltimateFileLoader.getPlayerFile(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public boolean mutegone(OfflinePlayer p, Boolean directreset){
		final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(p));
		if(conf.getBoolean("mute") == false) return false;
		if(conf.getLong("mutetime") == 0 || conf.getLong("mute") == -1) return false;
		if(System.currentTimeMillis() >= conf.getLong("mutetime")){
			if(directreset == true){
				Mute(p, false, 0L);
			}
			return true;
		}
		return false;
	}
	public void playerUpdateEvent(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()){
					if(mutegone(p, true)){
						p.sendMessage(r.mes("Mute.Unmutetarget"));
					}
					}
				
			}
			
		}, 120L, 60L);
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e){
		if(Mute(e.getPlayer())){
			mutegone(e.getPlayer(), true);
			if(!Mute(e.getPlayer())) return;
			e.setCancelled(true);
			final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(e.getPlayer()));
			Long mutetime = conf.getLong("mutetime");
			if(mutetime == 0 || mutetime == -1){
			e.getPlayer().sendMessage(r.mes("Mute.ChatMessage"));
			}else{
				e.getPlayer().sendMessage(r.mes("Mute.ChatMessageTime").replaceAll("%Time", DateUtil.format(mutetime)));
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChatCmd(PlayerCommandPreprocessEvent e){
		if(Mute(e.getPlayer())){
			@SuppressWarnings("unchecked")
			ArrayList<String> blockedcmds = (ArrayList<String>) r.getCnfg().getList("MuteBlockedCmds");
			if(blockedcmds.contains(e.getMessage()) || blockedcmds.contains(e.getMessage().replaceFirst("/", ""))){
				mutegone(e.getPlayer(), true);
				if(!Mute(e.getPlayer())) return;
				e.setCancelled(true);
				e.getPlayer().sendMessage(r.mes("Mute.ChatMessage"));
			}
		}
	}
}
