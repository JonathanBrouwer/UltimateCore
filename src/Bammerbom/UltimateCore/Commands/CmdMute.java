package Bammerbom.UltimateCore.Commands;

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
import Bammerbom.UltimateCore.API.UC;
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
	@SuppressWarnings({"deprecation" })
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
		Long time = 0L;
		//Info
		if(r.checkArgs(args, 1) == false){
		}else if(DateUtil.getTimeMillis(args[1]) != -1){
			time = DateUtil.getTimeMillis(args[1]);
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
		    UC.getPlayer(banp).setMuted(true, time);
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
		UC.getPlayer(banp).setMuted(false);
		sender.sendMessage(r.mes("Mute.Unmuted").replaceAll("%Player", banp.getName()));
	    if(banp.isOnline()){
	    	Player banp2 = (Player) banp;
	    banp2.sendMessage(r.mes("Mute.Unmutetarget"));
	    }
		
	}
	public void playerUpdateEvent(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()){
					if(UC.getPlayer(p).isMuted()){
					}
					}
				
			}
			
		}, 120L, 60L);
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onChat(AsyncPlayerChatEvent e){
		if(UC.getPlayer(e.getPlayer()).isMuted()){
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
	@EventHandler(priority = EventPriority.LOW)
	public void onChatCmd(PlayerCommandPreprocessEvent e){
		if(UC.getPlayer(e.getPlayer()).isMuted()){
			@SuppressWarnings("unchecked")
			ArrayList<String> blockedcmds = (ArrayList<String>) r.getCnfg().getList("MuteBlockedCmds");
			if(blockedcmds.contains(e.getMessage().split(" ")[0]) || blockedcmds.contains(e.getMessage().replaceFirst("/", "").split(" ")[0])){
				e.setCancelled(true);
				e.getPlayer().sendMessage(r.mes("Mute.ChatMessage"));
			}
		}
	}
	public static void mutes(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.mutelist", false, true)) return;
		StringBuilder mutes = new StringBuilder();
		Integer i = 0;
		for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
			if(UC.getPlayer(pl).isMuted()){
				if(!mutes.toString().equalsIgnoreCase(""))mutes.append(", ");
				mutes.append(pl.getName());
				i++;
			}
		}
		sender.sendMessage(r.mes("Mute.List").replaceAll("%Amount", i + "").replaceAll("%List", mutes.toString()));
	}
}
