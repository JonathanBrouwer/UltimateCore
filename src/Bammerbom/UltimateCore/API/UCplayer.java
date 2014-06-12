package Bammerbom.UltimateCore.API;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
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
	
	public UCplayer(Player p){
		name = p.getName();
		uuid = p.getUniqueId();
	}
	public UCplayer(String str){
		new UCplayer(Bukkit.getPlayer(str));
	}
	public UCplayer(UUID uuid){
		new UCplayer(Bukkit.getPlayer(uuid));
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
	public void setBanned(Boolean banned, String reason){
		String timen = r.mes("Mute.Forever");
		//Ban
		Player banp = getPlayer();
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
	}
	public void setBanned(Boolean banned, Long timen, String reason){
		String times = DateUtil.format(timen);
		if(timen == 0){
			times = r.mes("Mute.Forever");
		}
		//Ban
		Player banp = getPlayer();
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
	}
	public Player getPlayer(){
		return Bukkit.getPlayer(uuid);
	}
	public void openEnderchest(Player p){
		p.openInventory(getPlayer().getEnderChest());
	}
	public void clearInventory(){
		Player p = getPlayer();
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
	public void setChatColor(ChatColor c){
		Player p = getPlayer();
		FileConfiguration pconf = null;
	pconf = UltimateFileLoader.getPlayerConfig(p);
	    pconf.set("ChatColor", '&' + c.getChar());
		try {
			pconf.save(UltimateFileLoader.getPlayerFile(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ChatColor getChatColor(){
		Player p = getPlayer();
		FileConfiguration pconf = null;
	    pconf = UltimateFileLoader.getPlayerConfig(p);
	    return ChatColor.getByChar(pconf.getString("ChatColor").replaceFirst("&", ""));
	}
	public void setSpeed(Float f){
		getPlayer().setWalkSpeed(CmdSpeed.getSpeed(f, false));
		getPlayer().setFlySpeed(CmdSpeed.getSpeed(f, true));
	}
	public Float getSpeed(){
		return getSpeed(false);
	}
	public Float getSpeed(Boolean fly){
		return fly ? getPlayer().getWalkSpeed() * 5 : getPlayer().getFlySpeed() * 10;
	}
	public Float getWalkSpeed(){
		return getSpeed(false);
	}
	public Float getFlySpeed(){
		return getSpeed(true);
	}
	public boolean isGhost(){
		return GhostsUtil.isGhost(getPlayer());
	}
	public void setGhost(Boolean b){
		GhostsUtil.setGhost(getPlayer(), b);
	}
}
