package Bammerbom.UltimateCore.API;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;
import Bammerbom.UltimateCore.Commands.CmdSpeed;
import Bammerbom.UltimateCore.Events.EventActionMessage;
import Bammerbom.UltimateCore.Resources.Utils.DateUtil;
import Bammerbom.UltimateCore.Resources.Utils.GhostsUtil;

public class UCplayer{
	String name = null;
	UUID uuid = null;
	
	public UCplayer(OfflinePlayer p){
		name = p.getName();
		uuid = p.getUniqueId();
	}
	/*
	@SuppressWarnings("deprecation")
	public UCplayer(String str){
		if(UUID.fromString(str) != null){
			UCplayer p = new UCplayer(UUID.fromString(str));
			name = p.getPlayer().getName();
			uuid = p.getPlayer().getUniqueId();
			return;
		}
		UCplayer p = new UCplayer(Bukkit.getOfflinePlayer(str));
		name = p.getPlayer().getName();
		uuid = p.getPlayer().getUniqueId();
	}
	*/
	public UCplayer(UUID uuid){
		UCplayer p = new UCplayer(Bukkit.getOfflinePlayer(uuid));
		name = p.getPlayer().getName();
		uuid = p.getPlayer().getUniqueId();
	}
	public Long getLastConnectMillis(){
		final UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(conf.get("lastconnect") != null){
			return conf.getLong("lastconnect");
		}else{
			return getPlayer().getLastPlayed();
		}
	}
	public boolean isMuted(){
		final UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(conf.getBoolean("mute") == false) return false;
		if(conf.getLong("mutetime") == 0 || conf.getLong("mute") == -1) return true;
		if(System.currentTimeMillis() >= conf.getLong("mutetime")){
			setMuted(false);
			return false;
		}
		if(conf.get("mute") == null){
			return false;
		}
		return conf.getBoolean("mute");
	}
	public boolean isDeaf(){
		final UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(conf.getBoolean("deaf") == false) return false;
		if(conf.getLong("deaftime") == 0 || conf.getLong("deaf") == -1) return true;
		if(System.currentTimeMillis() >= conf.getLong("deaftime")){
			setDeaf(false);
			return false;
		}
		if(conf.get("dead") == null){
			return false;
		}
		return conf.getBoolean("deaf");
	}
	public boolean isGod(){
		final UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return (conf.get("godmode") != null) ? conf.getBoolean("godmode") : false;
	}
	public void setMuted(Boolean m){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("mute", m);
		data.set("mutetime", 0);
		data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
	}
	public void setMuted(Boolean m, Long time){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("mute", m);
		data.set("mutetime", time);
		data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
	}
	public void setDeaf(Boolean m){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("deaf", m);
		data.set("deaftime", 0);
		data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
	}
	public void setDeaf(Boolean m, Long time){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("deaf", m);
		data.set("deaftime", time);
		data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
	}
	public String getNick(){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(data.get("nick") == null) return getPlayer().getName();
		String nick = ChatColor.translateAlternateColorCodes('&', data.getString("nick"));
		if(getPlayer().isOnline() && r.perm((Player)getPlayer(), "uc.rainbow", false, false)) nick = nick.replaceAll("&y", r.getRandomChatColor() + "");
		return nick + ChatColor.RESET;
	}
	public void setNick(String str){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("nick", str);
		data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
	}
	public Boolean isSpy(){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(data.get("spy") == null) return false;
		return data.getBoolean("spy");
	}
	public void setSpy(Boolean str){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("spy", str);
		data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
	}
	public boolean isBanned(){
		 final UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		 if(conf.get("banned") == null){ return false; }
		 if(!conf.getBoolean("banned") == true) return false;
		 if(conf.get("bantime") != null && conf.getLong("bantime") < 1 ) return true;
		 if(System.currentTimeMillis() >= conf.getLong("bantime")){
				conf.set("banned", false);
				conf.set("banreason", null);
				conf.set("bantime", null);
				conf.save(UltimateFileLoader.getPlayerFile(getPlayer()));
				return false;
		 }else{
			 return true;
		 }
		 
	}
	public boolean hasTeleportEnabled(){
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(conf.get("tpenabled") == null) return true;
		return conf.getBoolean("tpenabled");
	}
	public void setTeleportEnabled(Boolean b){
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		conf.set("tpenabled", b);
		conf.save(UltimateFileLoader.getPlayerFile(getPlayer()));
	}
	public long getBanTimeLeftMillis(){
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getLong("bantime");
	}
    public String getBanReason(){
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getString("banreason");
    }
    public long getMuteTimeLeftMillis(){
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getLong("mutetime");
    }
    public long getDeafTimeLeftMillis(){
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getLong("deaftime");
    }
    public long getFreezeTimeLeftMillis(){
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getLong("freezetime");
    }
	public void setBanned(Boolean banned, String reason){
		String timen = r.mes("Mute.Forever");
		//Ban
		OfflinePlayer banp = getPlayer();
		String reas = r.mes("Ban.Message").replaceAll("%Time", timen).replaceAll("%Reason", reason);
		if(banp.isOnline()){
			EventActionMessage.setEnb(false);
			Bukkit.getPlayer(banp.getName()).kickPlayer(reas);
			EventActionMessage.setEnb(true);
		}
		//pconf
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(banp));
		conf.set("banned", banned);
		conf.set("banreason", reason);
		conf.set("bantime", -1L);
		conf.save(UltimateFileLoader.getPlayerFile(banp));
		if(banned == false){
		Bukkit.getBanList(Type.NAME).pardon(name);
		}else{
			BanList list = Bukkit.getBanList(Type.NAME);
			Date date = new Date();
			date.setTime(System.currentTimeMillis() + 999999);
			list.addBan(banp.getName().toString(), reas, date, null);
		}
	}
	public void setBanned(Boolean banned, Long timen, String reason){
		String times = DateUtil.format(timen);
		if(timen == 0){
			times = r.mes("Mute.Forever");
		}
		//Ban
		Player banp = getOnlinePlayer();
		String reas = r.mes("Ban.Message").replaceAll("%Time", times).replaceAll("%Reason", reason);
		if(banp.isOnline()){
			EventActionMessage.setEnb(false);
			Bukkit.getPlayer(banp.getName()).kickPlayer(reas);
			EventActionMessage.setEnb(true);
		}
		//pconf
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(banp));
		conf.set("banned", banned);
		conf.set("banreason", reason);
		conf.set("bantime", timen);
		if(timen == 0){
			conf.set("bantime", -1L);
		}
		try {
			conf.save(UltimateFileLoader.getPlayerFile(banp));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(banned == false){
		Bukkit.getBanList(Type.NAME).pardon(name);
		}else{
			BanList list = Bukkit.getBanList(Type.NAME);
			Date date = new Date();
			date.setTime(System.currentTimeMillis() + timen);
			list.addBan(banp.getName().toString(), reas, date, null);
		}
	}
	public Player getOnlinePlayer(){
		return Bukkit.getPlayer(uuid);
	}
	public OfflinePlayer getPlayer(){
		return Bukkit.getOfflinePlayer(uuid);
	}
	public void clearInventory(){
		Player p = getOnlinePlayer();
	    p.getInventory().clear();
	    p.getInventory().setHelmet(null);
	    p.getInventory().setChestplate(null);
	    p.getInventory().setLeggings(null);
	    p.getInventory().setBoots(null);
	}
	public boolean isFrozen(){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(data.get("freeze") == null){
			return false;
		}
		return data.getBoolean("freeze");
	}
	public void setFrozen(Boolean set){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("freeze", set);
		data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
	}
	public void setSpeed(Float f){
		getOnlinePlayer().setWalkSpeed(CmdSpeed.getSpeed(f, false));
		getOnlinePlayer().setFlySpeed(CmdSpeed.getSpeed(f, true));
	}
	public Float getSpeed(){
		return getSpeed(false);
	}
	public Float getSpeed(Boolean fly){
		return fly ? getOnlinePlayer().getWalkSpeed() * 5 : getOnlinePlayer().getFlySpeed() * 10;
	}
	public Float getWalkSpeed(){
		return getSpeed(false);
	}
	public Float getFlySpeed(){
		return getSpeed(true);
	}
	public boolean isGhost(){
		return GhostsUtil.isGhost(getOnlinePlayer());
	}
	public void setGhost(Boolean b){
		GhostsUtil.setGhost(getOnlinePlayer(), b);
	}
	public boolean isJailed(){
		return getJail() != null;
	}
	public Long getJailTimeLeftMillis(){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return data.getLong("jailtime");
	}
	public String getJail(){
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return data.getString("jail");
	}
	public void setJailed(Boolean b, String str, Long time){
		if(b){
		Random ra = new Random();
		UltimateConfiguration conf = UltimateConfiguration.loadConfiguration(UltimateFileLoader.DFjails);
		String jail = "";
		if(str != null){
			jail = str;
		}else{
			ArrayList<String> jails = new ArrayList<String>();
			   for(String stbr : conf.getConfigurationSection("Jails").getKeys(true)){
				   jails.add(stbr);
			   }	
			   jail = jails.get(ra.nextInt(jails.size()));
		}
		UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("jail", jail);
		data.set("jailtime", time);
		data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		
		}else{
			UltimateConfiguration data = UltimateConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
			data.set("jail", null);
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		}
		
	}
}
