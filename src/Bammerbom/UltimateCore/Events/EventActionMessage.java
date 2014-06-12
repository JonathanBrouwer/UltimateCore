package Bammerbom.UltimateCore.Events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import Bammerbom.UltimateCore.UltimateFileLoader;
import Bammerbom.UltimateCore.r;

public class EventActionMessage implements Listener{
	Plugin plugin;
	public EventActionMessage(Plugin instance){
		plugin = instance;
		if(this instanceof Listener){
			Bukkit.getPluginManager().registerEvents((Listener) this, instance);
		}
	}
	static Boolean enb = true;
	@EventHandler(priority = EventPriority.LOW)
	public void JoinMessage(PlayerJoinEvent e){
		if(enb == true){
			if(UltimateFileLoader.getPlayerConfig(e.getPlayer()).get("banned") != null && UltimateFileLoader.getPlayerConfig(e.getPlayer()).getBoolean("banned") == true ){
				e.setJoinMessage(null);
				return;
			}
			YamlConfiguration data = YamlConfiguration.loadConfiguration(UltimateFileLoader.getPlayerFile(e.getPlayer()));
			String nick = data.getString("nick");
			if(nick == null) nick = e.getPlayer().getName();
			e.getPlayer().setDisplayName(nick);
		e.setJoinMessage(r.mes("JoinMessage").replaceAll("%Player", e.getPlayer().getDisplayName()));
		}else{
			e.setJoinMessage(null);
		}
		}
	@EventHandler(priority = EventPriority.LOW)
	public void QuitMessage(PlayerQuitEvent e){
		if(enb == true){
			if(UltimateFileLoader.getPlayerConfig(e.getPlayer()).get("banned") != null && UltimateFileLoader.getPlayerConfig(e.getPlayer()).getBoolean("banned") == true ){
				e.setQuitMessage(null);
				return;
			}
		    e.setQuitMessage(r.mes("LeaveMessage").replaceAll("%Player", e.getPlayer().getDisplayName()));
		}else{
			e.setQuitMessage(null);
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void KickMessage(PlayerKickEvent e){
		if(enb == true){
		    e.setLeaveMessage(r.mes("LeaveMessage").replaceAll("%Player", e.getPlayer().getDisplayName()));
		}else{
			e.setLeaveMessage(null);
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void JoinFail(PlayerLoginEvent e){
		if(e.getResult().equals(PlayerLoginEvent.Result.KICK_WHITELIST)){
			e.setKickMessage(r.mes("WhitelistMessage"));
		}else if(e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)){
			e.setKickMessage(r.mes("FullMessage"));
		}
	}
	public static void setEnb(Boolean b){
		enb = b;
	}
	
	
}
