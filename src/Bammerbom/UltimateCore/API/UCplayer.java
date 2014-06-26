package Bammerbom.UltimateCore.API;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
		final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(conf.get("lastconnect") != null){
			return conf.getLong("lastconnect");
		}else{
			return getPlayer().getLastPlayed();
		}
	}
	public boolean isMuted(){
		final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
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
		final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
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
		final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return (conf.get("godmode") != null) ? conf.getBoolean("godmode") : false;
	}
	public void setMuted(Boolean m){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("mute", m);
		data.set("mutetime", 0);
		try {
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setMuted(Boolean m, Long time){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("mute", m);
		data.set("mutetime", time);
		try {
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setDeaf(Boolean m){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("deaf", m);
		data.set("deaftime", 0);
		try {
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setDeaf(Boolean m, Long time){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("deaf", m);
		data.set("deaftime", time);
		try {
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getNick(){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(data.get("nick") == null) return getPlayer().getName();
		String nick = ChatColor.translateAlternateColorCodes('&', data.getString("nick"));
		if(getPlayer().isOnline() && r.perm((Player)getPlayer(), "uc.rainbow", false, false)) nick = nick.replaceAll("&y", r.getRandomChatColor() + "");
		return nick + ChatColor.RESET;
	}
	public void setNick(String str){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("nick", str);
		try {
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Boolean isSpy(){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(data.get("spy") == null) return false;
		return data.getBoolean("spy");
	}
	public void setSpy(Boolean str){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("spy", str);
		try {
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean isBanned(){
		 final YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		 if(conf.get("banned") == null){ return false; }
		 if(!conf.getBoolean("banned") == true) return false;
		 if(conf.get("bantime") != null && conf.getLong("bantime") < 1 ) return true;
		 if(System.currentTimeMillis() >= conf.getLong("bantime")){
				conf.set("banned", false);
				conf.set("banreason", null);
				conf.set("bantime", null);
				try {
					conf.save(UltimateFileLoader.getPlayerFile(getPlayer()));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return false;
		 }else{
			 return true;
		 }
		 
	}
	public boolean hasTeleportEnabled(){
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(conf.get("tpenabled") == null) return true;
		return conf.getBoolean("tpenabled");
	}
	public void setTeleportEnabled(Boolean b){
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		conf.set("tpenabled", b);
		try {
			conf.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public long getBanTimeLeftMillis(){
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getLong("bantime");
	}
    public String getBanReason(){
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getString("banreason");
    }
    public long getMuteTimeLeftMillis(){
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getLong("mutetime");
    }
    public long getDeafTimeLeftMillis(){
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return conf.getLong("deaftime");
    }
    public long getFreezeTimeLeftMillis(){
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
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
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(banp));
		conf.set("banned", banned);
		conf.set("banreason", reason);
		conf.set("bantime", -1L);
		try {
			conf.save(UltimateFileLoader.getPlayerFile(banp));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(banp));
		conf.set("banned", banned);
		conf.set("banreason", reason);
		conf.set("bantime", timen);
		if(timen == 0){
			conf.set("bantime", -1L);
		}
		try {
			conf.save(UltimateFileLoader.getPlayerFile(banp));
		} catch (IOException e) {
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
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		if(data.get("freeze") == null){
			return false;
		}
		return data.getBoolean("freeze");
	}
	public void setFrozen(Boolean set){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("freeze", set);
		try {
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return data.getLong("jailtime");
	}
	public String getJail(){
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		return data.getString("jail");
	}
	public void setJailed(Boolean b, String str, Long time){
		if(b){
		Random ra = new Random();
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(UltimateFileLoader.DFjails);
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
		YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
		data.set("jail", jail);
		data.set("jailtime", time);
		try {
			data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}else{
			YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(getPlayer()));
			data.set("jail", null);
			try {
				data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
