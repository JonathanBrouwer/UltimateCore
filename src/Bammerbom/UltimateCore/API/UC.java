package Bammerbom.UltimateCore.API;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UC {
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
	@SuppressWarnings("deprecation")
	public static Player[] getOnlinePlayers(){
		ArrayList<Player> plz = new ArrayList<Player>();
		for(Player p : Bukkit.getOnlinePlayers()){
		  	plz.add(p);
		}
		return plz.toArray(new Player[plz.size()]);
	}
	@SuppressWarnings("deprecation")
	public static Player searchPlayer(String s){
		if(Bukkit.getPlayer(s) != null){
			return Bukkit.getPlayer(s);
		}
		/*Player f = null;
		for(Player p : getOnlinePlayers()){
			if(ChatColor.stripColor(getPlayer(p).getNick()) != null){
				Integer delta = 2147483647;
				String lowerName = ChatColor.stripColor(getPlayer(p).getNick()).toLowerCase();
				if (p.getName().toLowerCase().startsWith(lowerName)) {
			        int curDelta = p.getName().length() - lowerName.length();
			        if (curDelta < delta) {
			          f = p;
			          delta = curDelta;
			        }
			        if (curDelta == 0)
			          break;
			      }
			}
		}
		if(f != null){
			return f;
		}*/
		return null;
	}
	public static Player searchPlayer(UUID u){
		return Bukkit.getPlayer(u);
	}
	
}
