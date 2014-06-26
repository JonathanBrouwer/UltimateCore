package Bammerbom.UltimateCore.Commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.API.UC;
import Bammerbom.UltimateCore.API.UCplayer;
import Bammerbom.UltimateCore.Events.EventActionMessage;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;
import Bammerbom.UltimateCore.Resources.Utils.StringUtil;

public class CmdBan implements Listener{
	static Plugin plugin;
	public CmdBan(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
		for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
			if(!p.isBanned()) return;
			UCplayer up = UC.getPlayer(p);
			if(up.isBanned() && !Bukkit.getBanList(Type.NAME).isBanned(up.getPlayer().getName())){
				up.setBanned(true, Bukkit.getBanList(Type.NAME).getBanEntry(p.getName()).getExpiration().getTime() - System.currentTimeMillis(), Bukkit.getBanList(Type.NAME).getBanEntry(p.getName()).getReason());
			}
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
			if(r.perm(p, "uc.antiban", false, false) && !(sender instanceof ConsoleCommandSender)){
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
		BanList list = Bukkit.getBanList(Type.NAME);
		Date date = new Date();
		date.setTime(System.currentTimeMillis() + time);
		list.addBan(banp.getName().toString(), reas, date, null);
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
	public void joinOnBan(final PlayerLoginEvent e){
		 final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(e.getPlayer()));
		 if(conf.get("banned") == null){ return; }
		 if(!conf.getBoolean("banned") == true) return;
		 if(conf.get("bantime") != null && conf.getLong("bantime") < 1 ){
							e.disallow(Result.KICK_BANNED, r.mes("Ban.Message")
									.replaceAll("%Time", r.mes("Ban.TimeForever"))
									.replaceAll("%Reason", conf.getString("banreason")));
			return;
		}
		if(conf.get("banned") != null && conf.getBoolean("banned") == true){
			if(System.currentTimeMillis() >= conf.getLong("bantime")){
				e.getPlayer().sendMessage(r.mes("Ban.joinAfterUnban"));
				UCplayer p = UC.getPlayer(e.getPlayer());
				p.setBanned(false, "");
				Bukkit.getBanList(Type.NAME).pardon(p.getPlayer().getName());
				return;
			}else{
								Long l = conf.getLong("bantime");
								e.disallow(Result.KICK_BANNED, r.mes("Ban.Message").replaceAll("%Time", DateUtil.format(l)).replaceAll("%Reason", conf.getString("banreason")));
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static void unban(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.unban", false, true)){ return; }
		if(r.checkArgs(args, 0) == false){
			sender.sendMessage(r.mes("Ban.Usage2"));
			return;
		}
		OfflinePlayer banp = Bukkit.getOfflinePlayer(args[0]);
		if(banp == null){
			sender.sendMessage(r.mes("PlayerNotFound").replaceAll("%Player", args[0]));
			return;
		}
		BanList list = Bukkit.getBanList(Type.NAME);
		if(!list.isBanned(banp.getName()) && !UC.getPlayer(banp).isBanned()){
			sender.sendMessage(r.mes("Ban.NotBanned").replaceAll("%Player", banp.getName()));
			return;
		}
		if(list.isBanned(banp.getName())) list.pardon(banp.getName().toString());
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
	public static void bans(CommandSender sender, String[] args){
		if(!r.perm(sender, "uc.banlist", false, true)) return;
		ArrayList<String> l = new ArrayList<String>();
		Integer i = 0;
		for(BanEntry ban : Bukkit.getBanList(Type.NAME).getBanEntries()){
			l.add(ban.getTarget());
			i++;
		}
		for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
			if(UC.getPlayer(pl).isBanned() && !l.contains(pl.getName())){
				l.add(pl.getName());
				i++;
			}
		}
		sender.sendMessage(r.mes("Ban.List").replaceAll("%Amount", i + "").replaceAll("%Banned", StringUtil.joinList(l)));
	}
    
}
