package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;

public class CmdDeaf implements Listener{
	static Plugin plugin;
	public CmdDeaf(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		playerUpdateEvent();
	}
	@SuppressWarnings({"deprecation" })
	public static void handle(CommandSender sender, String[] args) {
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Deaf.Usage"));
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
		if(!r.perm(sender, "uc.deaf.time", false, false) && !r.perm(sender, "uc.deaf", false, false) && time == 0L){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		if(!r.perm(sender, "uc.deaf.perm", false, false) && !r.perm(sender, "uc.deaf", false, false) && time != 0L){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		UC.getPlayer(banp).setDeaf(true, time);
		    sender.sendMessage(r.mes("Deaf.Deaf").replaceAll("%Player", banp.getName()));
		    if(banp.isOnline()){
		    	Player banp2 = (Player) banp;
		    banp2.sendMessage(r.mes("Deaf.Deaftarget"));
		    }
		    return;
		
	}
	@SuppressWarnings("deprecation")
	public static void undeaf(CommandSender sender, String[] args){
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Deaf.Usage2"));
			return;
		}
		OfflinePlayer banp = Bukkit.getOfflinePlayer(args[0]);
		if(banp == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		if(!r.perm(sender, "uc.deaf.perm", false, false) && !r.perm(sender, "uc.deaf", false, false) && !r.perm(sender, "uc.deaf.time", false, false)){
			sender.sendMessage(r.mes("NoPermissions"));
			return; 
		}
		UC.getPlayer(banp).setDeaf(false);
		sender.sendMessage(r.mes("Deaf.Undeaf").replaceAll("%Player", banp.getName()));
	    if(banp.isOnline()){
	    	Player banp2 = (Player) banp;
	    banp2.sendMessage(r.mes("Deaf.Undeaftarget"));
	    }
		
	}
	public void playerUpdateEvent(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run() {
				for(Player p : r.getOnlinePlayers()){
					UC.getPlayer(p).isDeaf();
					}
			}
			
		}, 120L, 60L);
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e){
		if(UC.getPlayer(e.getPlayer()).isDeaf()){
			e.getRecipients().remove(e.getPlayer());
		}
	}
	public static void deafs(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.deaflist", false, true)) return;
		StringBuilder deafs = new StringBuilder();
		Integer i = 0;
		for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
			if(UC.getPlayer(pl).isDeaf()){
				if(!deafs.toString().equalsIgnoreCase(""))deafs.append(", ");
				deafs.append(pl.getName());
				i++;
			}
		}
		sender.sendMessage(r.mes("Deaf.List").replaceAll("%Amount", i + "").replaceAll("%List", deafs.toString()));
	}
}
