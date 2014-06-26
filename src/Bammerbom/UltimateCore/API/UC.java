package Bammerbom.UltimateCore.API;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class UC {
	/*
	@SuppressWarnings("deprecation")
	public static UCplayer getPlayer(String s){
		if(Bukkit.getOfflinePlayer(s) == null) return null;
		return new UCplayer(s);
	}
	*/
	public static UCplayer getPlayer(UUID u){
		if(Bukkit.getOfflinePlayer(u) == null) return null;
		return new UCplayer(u);
	}
	public static UCplayer getPlayer(OfflinePlayer p){
		if(p == null) return null;
		return new UCplayer(p);
	}
	public static UCworld getWorld(String world){
		return new UCworld(world);
	}
	public static UCserver getServer(){
		return new UCserver();
	}
}
