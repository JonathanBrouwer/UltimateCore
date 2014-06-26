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
			sender.sendMessage(r.default1 + "The player " + r.default2 + args[0] + r.default1 + " has never joined this server.");
			return;
		}
		UCplayer p = UC.getPlayer(Bukkit.getOfflinePlayer(args[0]));
		if(p == null || p.getPlayer() == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		sender.sendMessage(r.mes("Seen.Message1")
				.replaceAll("%Player", p.getPlayer().getName())
				.replaceAll("%Status", p.getPlayer().isOnline() ?  "online" : "offline")
				.replaceAll("%Time", DateUtil.formatDateDiff(p.getLastConnectMillis())));
		//Last location
		Player tempP = p.getPlayer().getPlayer();
		String loc;
		if(tempP != null){
		loc = tempP.getWorld().getName() + " " + tempP.getLocation().getBlockX() + " " + tempP.getLocation().getBlockY() + " " + tempP.getLocation().getBlockZ();
		}else{
			loc = "Failed to find";
		}
		sender.sendMessage(r.default1 + "- Last location: " + r.default2 + loc);
		//Ban
		sender.sendMessage(r.default1 + "- Banned: " + ((p.isBanned() || p.getPlayer().isBanned()) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
		if(p.isBanned() || p.getPlayer().isBanned()){
			sender.sendMessage(r.default1 + "- Bantime left: " + r.default2 + DateUtil.format(p.getBanTimeLeftMillis()));
			sender.sendMessage(r.default1 + "- Banreason: " + r.default2 + p.getBanReason());
		}
		//Mute
		sender.sendMessage(r.default1 + "- Muted: " + (p.isMuted() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
		if(p.isMuted()){
			sender.sendMessage(r.default1 + "- Mutetime left: " + r.default2 + DateUtil.format(p.getMuteTimeLeftMillis()));
		}
		//Deafe
		sender.sendMessage(r.default1 + "- Deaf: " + (p.isDeaf() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
		if(p.isDeaf()){
			sender.sendMessage(r.default1 + "- Deaftime left: " + r.default2 + DateUtil.format(p.getDeafTimeLeftMillis()));
		}
		//Jailed
		sender.sendMessage(r.default1 + "- Jailed: " + (p.isJailed() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
		if(p.isJailed()){
			sender.sendMessage(r.default1 + "- Jailtime left: " + r.default2 + DateUtil.format(p.getJailTimeLeftMillis()));
		}
		//Frozen
		sender.sendMessage(r.default1 + "- Frozen: " + (p.isFrozen() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
		
		
	}
}
