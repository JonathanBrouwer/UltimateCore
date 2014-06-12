package Bammerbom.UltimateCore.API;

import java.util.UUID;

import org.bukkit.entity.Player;

public class UC {
	public static UCplayer getPlayer(String s){
		return new UCplayer(s);
	}
	public static UCplayer getPlayer(UUID u){
		return new UCplayer(u);
	}
	public static UCplayer getPlayer(Player p){
		return new UCplayer(p);
	}
	public static UCworld getWorld(String world){
		return new UCworld(world);
	}
}
