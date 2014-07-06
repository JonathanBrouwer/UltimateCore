package Bammerbom.UltimateCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;

public class CmdSeen {
	static Plugin plugin;
	public CmdSeen(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	@SuppressWarnings("deprecation")
	public static void handle(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.seen", false, true)) return;
		if(!r.checkArgs(args, 0)){
			sender.sendMessage(r.mes("Seen.Usage"));
			return;
		}
		if(!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() && !Bukkit.getOfflinePlayer(args[0]).isOnline()){
			sender.sendMessage(r.mes("Seen.NotFound").replaceAll("%Player", args[0]));
			return;
		}
		UCplayer p = UC.getPlayer(Bukkit.getOfflinePlayer(args[0]));
		if(p == null || p.getPlayer() == null){
			sender.sendMessage(r.mes("Seen.NotFound").replaceAll("%Player", args[0]));
			return;
		}
		sender.sendMessage(r.mes("Seen.Message1")
				.replaceAll("%Player", p.getPlayer().getName())
				.replaceAll("%Status", p.getPlayer().isOnline() ?  "online" : "offline")
				.replaceAll("%Time", DateUtil.formatDateDiff(p.getLastConnectMillis())));
		//Last location
		Player tempP = p.getPlayer().getPlayer();
		tempP.loadData();
		String loc;
		if(tempP != null && tempP.getLocation() != null){
		loc = tempP.getWorld().getName() + " " + tempP.getLocation().getBlockX() + " " + tempP.getLocation().getBlockY() + " " + tempP.getLocation().getBlockZ();
		}else{
			loc = "Failed to find";
		}
		
		sender.sendMessage(r.mes("Seen.Location").replaceAll("%Location", loc));
		//Ban
		sender.sendMessage(r.mes("Seen.Banned").replaceAll("%Banned", ((p.isBanned() || p.getPlayer().isBanned()) ? ChatColor.GREEN + r.word("Words.Yes") : ChatColor.RED + r.word("Words.No"))));
		if(p.isBanned() || p.getPlayer().isBanned()){
			sender.sendMessage(r.mes("Seen.Bantime").replaceAll("%Bantime", DateUtil.format(p.getBanTimeLeftMillis()) + ""));
			sender.sendMessage(r.mes("Seen.Banreason").replaceAll("%Reason", p.getBanReason()));
		}
		//Mute
		sender.sendMessage(r.mes("Seen.Muted").replaceAll("%Muted", p.isMuted() ? ChatColor.GREEN + r.word("Words.Yes") : ChatColor.RED + r.word("Words.No")));
		if(p.isMuted()){
			sender.sendMessage(r.mes("Seen.Mutetime").replaceAll("%Mutetime", DateUtil.format(p.getMuteTimeLeftMillis())));
		}
		//Deaf
		sender.sendMessage(r.mes("Seen.Deaf").replaceAll("%Deaf", p.isDeaf() ? ChatColor.GREEN + r.word("Words.Yes") : ChatColor.RED + r.word("Words.No")));
		if(p.isDeaf()){
			sender.sendMessage(r.mes("Seen.Deaftime").replaceAll("%Deaftime", DateUtil.format(p.getDeafTimeLeftMillis())));
		}
		//Jailed
		sender.sendMessage(r.mes("Seen.Jailed").replaceAll("%Jailed", p.isJailed() ? ChatColor.GREEN + r.word("Words.Yes") : ChatColor.RED + r.word("Words.No")));
		if(p.isJailed()){
			sender.sendMessage(r.mes("Seen.Jailtime").replaceAll("%Jailtime", DateUtil.format(p.getJailTimeLeftMillis())));
		}
		//Frozen
		sender.sendMessage(r.mes("Seen.Frozen").replaceAll("%Frozen", p.isFrozen() ? ChatColor.GREEN + r.word("Words.Yes") : ChatColor.RED + r.word("Words.No")));
		
		
	}
}
